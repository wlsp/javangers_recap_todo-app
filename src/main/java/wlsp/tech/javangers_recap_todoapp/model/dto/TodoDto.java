package wlsp.tech.javangers_recap_todoapp.model.dto;

public record TodoDto(String title, String description, wlsp.tech.javangers_recap_todoapp.model.Enum.TodoStatus status) {
}
