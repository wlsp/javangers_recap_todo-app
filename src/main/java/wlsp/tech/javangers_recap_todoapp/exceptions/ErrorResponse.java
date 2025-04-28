package wlsp.tech.javangers_recap_todoapp.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, int status, String error, String message) {
}
