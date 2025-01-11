package cz.jeme.programu.florianapi;

import com.artifex.mupdf.fitz.Context;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(final String @NotNull [] args) throws ClassNotFoundException {
        Class.forName(FlorianUtils.class.getName()); // initialize FlorianUtils

        try {
            Context.init(); // initialize MuPDF
        } catch (final UnsatisfiedLinkError e) {
            throw new IllegalStateException(
                    "Failed to load MuPDF native library. " +
                    "Ensure that the library is located next to the FloriApi jar " +
                    "and that \"-Djava.library.path=.\" is present in the VM arguments"
            );
        }

        SpringApplication.run(Main.class, args);
    }
}
