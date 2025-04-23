package wlsp.tech.javangers_recap_todoapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import wlsp.tech.javangers_recap_todoapp.model.Enum.TodoStatus;

@Document("Todos")
public record Todo(@Id String id, String description, TodoStatus status) {
}
