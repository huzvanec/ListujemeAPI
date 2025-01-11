package cz.jeme.programu.florianapi.rest.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public final class FlorianError {
    private final @NotNull HttpStatusCode statusCode;
    @JsonProperty
    private final @NotNull String status;
    @JsonProperty
    private final int code;

    public FlorianError(final @NotNull HttpStatusCode statusCode) {
        this.statusCode = statusCode;
        this.code = statusCode.value();
        this.status = statusCode instanceof final HttpStatus s ? s.name() : "UNKNOWN";
    }

    public @NotNull HttpStatusCode statusCode() {
        return statusCode;
    }

    public @NotNull ResponseEntity<FlorianError> toResponse() {
        return new ResponseEntity<>(this, statusCode);
    }
}
