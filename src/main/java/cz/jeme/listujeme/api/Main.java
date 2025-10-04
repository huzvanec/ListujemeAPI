package cz.jeme.listujeme.api;

import com.artifex.mupdf.fitz.Context;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Main {
    public static void main(final String @NotNull [] args) throws ClassNotFoundException {
        Class.forName(FlorianUtils.class.getName()); // initialize FlorianUtils

        try {
            Context.init(); // initialize MuPDF
        } catch (final UnsatisfiedLinkError e) {
            throw new IllegalStateException(
                    "Failed to load MuPDF native library. " +
                    "Ensure that the library is located next to the ListujemeAPI jar " +
                    "and that \"-Djava.library.path=.\" is present in the VM arguments"
            );
        }

        SpringApplication.run(Main.class, args);
    }

    @Bean
    protected @NotNull WebMvcConfigurer cors() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(final @NotNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
//                                "http://localhost:5173",
//                                "http://192.168.1.70:5173",
                                "https://listu.jeme.cz"
                        );
            }
        };
    }
}
