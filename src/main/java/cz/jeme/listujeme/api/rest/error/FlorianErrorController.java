package cz.jeme.listujeme.api.rest.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class FlorianErrorController implements ErrorController {
    private FlorianErrorController() {
        // bean
    }

    @GetMapping
    public @NotNull ResponseEntity<FlorianError> handleError(final @NotNull HttpServletRequest request) {
        return new FlorianError(HttpStatusCode.valueOf(
                (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE))
        ).toResponse();
    }
}