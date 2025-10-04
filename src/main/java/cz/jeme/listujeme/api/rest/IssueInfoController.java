package cz.jeme.listujeme.api.rest;

import cz.jeme.listujeme.api.FlorianUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/issues")
public final class IssueInfoController {
    private IssueInfoController() {
        // bean
    }

    public record IssueInfo(
            @NotNull String name,
            int year,
            @NotNull String period,
            @Nullable String typesetter
    ) {
        public static @NotNull IssueInfo parse(final @NotNull String name) {
            final String[] parts = name.split("_");
            try {
                final int year = Integer.parseInt(parts[0]);
                final String typesetter = parts.length < 3 ? null : parts[2].replace("-", " ");
                return new IssueInfo(name, year, parts[1], typesetter);
            } catch (final NumberFormatException e) {
                throw new IllegalArgumentException("Invalid name: " + name);
            }
        }
    }

    @GetMapping("/{name}")
    public @NotNull IssueInfo issueInfo(final @NotNull @PathVariable String name) {
        FlorianUtils.getPdfFile(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Issue not found!"));
        return IssueInfo.parse(name);
    }

    private static final @NotNull Pattern PDF_PATTERN = Pattern.compile("\\.pdf$", Pattern.CASE_INSENSITIVE);

    @GetMapping
    public @NotNull List<IssueInfo> issueInfos() {
        return Arrays.stream(Objects.requireNonNull(FlorianUtils.PDF_FOLDER.listFiles(), "Cannot list PDF folder"))
                .map(File::getName)
                .filter(fileName -> PDF_PATTERN.matcher(fileName).find())
                .map(fileName -> fileName.substring(0, fileName.lastIndexOf(".")))
                .map(IssueInfo::parse)
                .toList();
    }
}
