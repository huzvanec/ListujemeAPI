package cz.jeme.programu.florianapi.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class RootController {
    private RootController() {
        // bean
    }

    public static final class RootResponse {
        @JsonProperty
        private final @NotNull String status = "OK";
    }

    @GetMapping
    public @NotNull RootResponse getRoot() {
        return new RootResponse();
    }
}
