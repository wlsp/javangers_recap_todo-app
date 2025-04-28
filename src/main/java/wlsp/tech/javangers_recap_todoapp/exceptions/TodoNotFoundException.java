package wlsp.tech.javangers_recap_todoapp.exceptions;

public class TodoNotFoundException extends RuntimeException {
  public TodoNotFoundException(String message) {
    super(message);
  }
}
