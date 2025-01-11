package cz.jeme.programu.florianapi.rest.error;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public final class FlorianErrorController implements ErrorController {
    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(FlorianErrorController.class);

    private FlorianErrorController() {
        // bean
    }

    @ExceptionHandler(Exception.class)
    public @NotNull ResponseEntity<FlorianError> handle(final @NotNull Exception exception) {
        if (exception instanceof final ErrorResponse response)
            return new FlorianError(response.getStatusCode()).toResponse();
        LOGGER.error("Unhandled exception", exception);
        return new FlorianError(HttpStatus.INTERNAL_SERVER_ERROR).toResponse();
    }
}
