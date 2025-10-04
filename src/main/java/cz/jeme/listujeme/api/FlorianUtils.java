package cz.jeme.listujeme.api;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public final class FlorianUtils {
    public static final @NotNull File PDF_FOLDER = new File("pdfs");
    public static final @NotNull File PREVIEW_FOLDER = Path.of("cache", "previews").toFile();
    public static final @NotNull File CONTENT_FOLDER = Path.of("cache", "contents").toFile();

    static {
        if (!PDF_FOLDER.exists() && !PDF_FOLDER.mkdirs())
            throw new RuntimeException("Could not create folder: " + PDF_FOLDER.getAbsolutePath());

        if (!PREVIEW_FOLDER.exists() && !PREVIEW_FOLDER.mkdirs())
            throw new RuntimeException("Could not create folder: " + PREVIEW_FOLDER.getAbsolutePath());

        if (!CONTENT_FOLDER.exists() && !CONTENT_FOLDER.mkdirs())
            throw new RuntimeException("Could not create folder: " + CONTENT_FOLDER.getAbsolutePath());
    }

    public static @NotNull Optional<File> getPdfFile(final @NotNull String name) {
        final File pdf = Path.of(PDF_FOLDER.getAbsolutePath(), name + ".pdf").toFile();
        if (pdf.exists()) return Optional.of(pdf);
        return Optional.empty();
    }

    private FlorianUtils() {
        throw new AssertionError();
    }
}
