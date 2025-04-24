package wlsp.tech.javangers_recap_todoapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import wlsp.tech.javangers_recap_todoapp.model.Enum.TodoStatus;
import wlsp.tech.javangers_recap_todoapp.model.Todo;
import wlsp.tech.javangers_recap_todoapp.model.TodoNotFoundException;
import wlsp.tech.javangers_recap_todoapp.model.dto.TodoDto;
import wlsp.tech.javangers_recap_todoapp.service.TodoService;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class TodoController {

  private final TodoService todoService;

  @GetMapping
  public ResponseEntity<List<Todo>> getAllTodos() {
    return ResponseEntity.status(HttpStatus.OK).body(todoService.getAllTodos());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Todo> getTodoById(@PathVariable String id) throws TodoNotFoundException {
    return ResponseEntity.status(HttpStatus.OK).body(
            todoService.getTodoById(id).orElseThrow(
                    () -> new TodoNotFoundException("Could not found todo with  the id: " + id + ".")
            )
    );
  }

  @PostMapping
  public ResponseEntity<Todo> addTodo(@RequestBody TodoDto todoReq) {

    Todo createdTodo = todoService.addTodo(todoReq);

    if(createdTodo == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).body(createdTodo);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Todo> updateTodo(@PathVariable String id, @RequestBody Todo todo)
          throws TodoNotFoundException {
    Todo todoRes;
    try {
      todoRes = todoService.updateTodo(id, todo);
      return ResponseEntity.status(HttpStatus.OK).body(todoRes);
    } catch (TodoNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @DeleteMapping("/{id}")
  public void deleteTodo(@PathVariable String id) {
    todoService.deleteTodo(id);
  }

  //@TODO fix methode
  @GetMapping("/search")
  public ResponseEntity<List<Todo>> getTodosByStatus(@RequestParam TodoStatus status) {
    return ResponseEntity.ok(todoService.getAlTodosByStatus(status));
  }

}
