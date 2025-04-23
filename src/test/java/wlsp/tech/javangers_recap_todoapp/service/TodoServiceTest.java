package wlsp.tech.javangers_recap_todoapp.service;

import org.junit.jupiter.api.Test;
import wlsp.tech.javangers_recap_todoapp.model.Enum.TodoStatus;
import wlsp.tech.javangers_recap_todoapp.model.Todo;
import wlsp.tech.javangers_recap_todoapp.repository.TodoRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

  IdService mockIdService = mock(IdService.class);
  TodoRepository mockTodoRepository = mock(TodoRepository.class);

  @Test
  void getAllTodos_shouldReturnListOfTodos_whenCalled() {
    //GIVEN
    TodoService service = new TodoService(mockTodoRepository, mockIdService);
    Todo todo1 = new Todo("1", "I'M THE ONE!", TodoStatus.OPEN);
    Todo todo2 = new Todo("2", "I'M THE SECOND! - DONE ME", TodoStatus.DONE);
    List<Todo> expected = List.of(todo1, todo2);

    when(mockTodoRepository.findAll()).thenReturn(List.of(todo1, todo2));

    //WHEN
    List<Todo> actual = service.getAllTodos();

    //THEN
    assertEquals(expected, actual);
  }

  @Test
  void getTodoById() {
  }

  @Test
  void addTodo() {
  }

  @Test
  void updateTodo() {
  }

  @Test
  void deleteTodo() {
  }

  @Test
  void getTodosByStatus() {
  }
}