package wlsp.tech.javangers_recap_todoapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wlsp.tech.javangers_recap_todoapp.model.Enum.TodoStatus;
import wlsp.tech.javangers_recap_todoapp.model.Todo;
import wlsp.tech.javangers_recap_todoapp.model.TodoNotFoundException;
import wlsp.tech.javangers_recap_todoapp.model.dto.TodoDto;
import wlsp.tech.javangers_recap_todoapp.repository.TodoRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

  private final TodoRepository todoRepository;
  private final IdService idService;

  public List<Todo> getAllTodos(){
    return todoRepository.findAll();
  }

  public Optional<Todo> getTodoById(String id) {
    return todoRepository.findById(id);
  }

  public Todo addTodo(TodoDto todo) {
    if(todo.description().isEmpty()) {
      return null;
    }
    Todo newTodo = new Todo(
            idService.generateTodoId(),
            todo.description(),
            todo.status()
    );
    return todoRepository.save(newTodo);
  }

  public Todo updateTodo(String id, Todo todo){
    Todo updatedTodo = todoRepository.findById(id).orElseThrow(
            () -> new TodoNotFoundException("Could not update Todo: " + todo +
                    ". Todo with the id: " + id + " not found!")
    );

    todoRepository.save(todo);

    return updatedTodo;
  }

  public void deleteTodo(String id){
    Optional<Todo> todo = todoRepository.findById(id);
    todo.ifPresent(todoRepository::delete);
  }

  public List<Todo> getAlTodosByStatus(TodoStatus status) {
    return todoRepository.findAllByStatus(status);
  }

}
