package wlsp.tech.javangers_recap_todoapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wlsp.tech.javangers_recap_todoapp.model.Enum.TodoStatus;
import wlsp.tech.javangers_recap_todoapp.model.Todo;

import java.util.List;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {
  List<Todo> findAllByStatus(TodoStatus status);
}
