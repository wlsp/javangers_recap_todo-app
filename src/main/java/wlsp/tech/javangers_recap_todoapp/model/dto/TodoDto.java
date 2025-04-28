package wlsp.tech.javangers_recap_todoapp.model.dto;

import wlsp.tech.javangers_recap_todoapp.model.Enum.TodoStatus;

public record TodoDto(String description, TodoStatus status) {
}
