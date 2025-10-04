package cz.jeme.listujeme.api.rest;

import com.artifex.mupdf.fitz.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jeme.listujeme.api.FlorianUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/issues/{name}")
public final class PdfController {
    private PdfController() {
        // bean
    }

    private static @NotNull ResponseEntity<Resource> readPdf(final @NotNull String name, final boolean download) {
        final File pdfFile = FlorianUtils.getPdfFile(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Issue not found"));

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.builder(download ? "attachment" : "inline")
                        .filename(URLEncoder.encode(pdfFile.getName(), StandardCharsets.UTF_8))
                        .build()
        );
        return ResponseEntity.ok()
                .headers(headers)
                .body(new FileSystemResource(pdfFile));
    }

    @GetMapping("/view")
    public @NotNull ResponseEntity<Resource> view(final @NotNull @PathVariable String name) {
        return readPdf(name, false);
    }

    @GetMapping("/download")
    public @NotNull ResponseEntity<Resource> download(final @NotNull @PathVariable String name) {
        return readPdf(name, true);
    }

    private static final @NotNull Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static final @NotNull Pattern HYPHENATION_PATTERN = Pattern.compile("\\s*\u00AD\\s*");
    private static final @NotNull ObjectMapper JSON_MAPPER = new ObjectMapper();

    private static void generateContent(final @NotNull String name) {
        final File pdfFile = FlorianUtils.getPdfFile(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Issue not found"));
        final List<String> content = new ArrayList<>();
        final Document document = Document.openDocument(pdfFile.getAbsolutePath());
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < document.countPages(); i++) {
            final Page page = document.loadPage(i);
            builder.setLength(0);

            final StructuredText.TextBlock[] blocks = page.toStructuredText().getBlocks();
            for (final StructuredText.TextBlock block : blocks) {
                for (final StructuredText.TextLine line : block.lines) {
                    for (final StructuredText.TextChar textChar : line.chars)
                        builder.append((char) textChar.c);
                    builder.append(' ');
                }
                builder.append(' ');
            }

            page.destroy();

            String text = builder.toString();
            text = WHITESPACE_PATTERN.matcher(text).replaceAll(" ");
            text = HYPHENATION_PATTERN.matcher(text).replaceAll("");
            content.add(text.trim());
        }
        document.destroy();
        try {
            final File contentFile = Path.of(
                    FlorianUtils.CONTENT_FOLDER.getAbsolutePath(),
                    name + ".json"
            ).toFile();
            if (!contentFile.exists() && !contentFile.createNewFile())
                throw new IOException("Could not create PDF content file");
            JSON_MAPPER.writeValue(
                    contentFile,
                    content
            );
        } catch (final IOException e) {
            throw new IllegalStateException("Could not save generated PDF content", e);
        }
    }

    @GetMapping("/content")
    public @NotNull Object content(final @NotNull @PathVariable String name) {
        final File contentFile = Path.of(FlorianUtils.CONTENT_FOLDER.getAbsolutePath(), name + ".json").toFile();
        try {
            if (!contentFile.exists()) generateContent(name);
            return JSON_MAPPER.readValue(contentFile, Object.class);
        } catch (final IllegalStateException | IOException e) {
            throw new RuntimeException("Could not obtain PDF content", e);
        }
    }

    private static final float PREVIEW_SCALE = 0.5F; // 50 % PDF DPI

    private static void generatePreview(final @NotNull String name) {
        final File pdfFile = FlorianUtils.getPdfFile(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Issue not found"));
        final Document document = Document.openDocument(pdfFile.getAbsolutePath());
        final Page titlePage = document.loadPage(0);
        final Pixmap pixmap = titlePage.toPixmap(
                Matrix.Scale(PREVIEW_SCALE),
                ColorSpace.DeviceRGB,
                false,
                true
        );
        pixmap.saveAsPNG(Path.of(
                FlorianUtils.PREVIEW_FOLDER.getAbsolutePath(),
                name + ".png"
        ).toString());
        pixmap.destroy();
        titlePage.destroy();
        document.destroy();
    }

    @GetMapping("/preview")
    public @NotNull Object preview(final @NotNull @PathVariable String name) {
        final File previewFile = Path.of(
                FlorianUtils.PREVIEW_FOLDER.getAbsolutePath(),
                name + ".png"
        ).toFile();
        if (!previewFile.exists()) generatePreview(name);
        final String nameEncoded = URLEncoder.encode(previewFile.getName(), StandardCharsets.UTF_8);
        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + nameEncoded)
                    .contentType(MediaType.IMAGE_PNG)
                    .body(new InputStreamResource(new FileInputStream(previewFile)));
        } catch (final IOException e) {
            throw new RuntimeException("Could not read PDF", e);
        }
    }
}
