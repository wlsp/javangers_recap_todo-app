package wlsp.tech.javangers_recap_todoapp.service;

import org.junit.jupiter.api.Test;
import wlsp.tech.javangers_recap_todoapp.model.Enum.TodoStatus;
import wlsp.tech.javangers_recap_todoapp.model.Todo;
import wlsp.tech.javangers_recap_todoapp.model.TodoNotFoundException;
import wlsp.tech.javangers_recap_todoapp.model.dto.TodoDto;
import wlsp.tech.javangers_recap_todoapp.repository.TodoRepository;

import java.util.List;
import java.util.Optional;

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
  void getTodoById_shouldReturnTodoWithSpecificId_WhenCalledAndTodoExist() throws TodoNotFoundException {
    //GIVEN
    TodoService service = new TodoService(mockTodoRepository, mockIdService);
    Todo expected = new Todo("2", "I AM THE ONE", TodoStatus.OPEN);
    when(mockTodoRepository.findById("2")).thenReturn(Optional.of(expected));

    //WHEN
    Optional<Todo> actual = service.getTodoById("2");

    //THEN
    assertTrue(actual.isPresent());
    assertEquals(expected, actual.get());
    verify(mockTodoRepository).findById("2");

  }

  @Test
  void addTodo_shouldReturnTodo_whenCalledWithDto() {
    // GIVEN
    TodoService service = new TodoService(mockTodoRepository, mockIdService);
    Todo expected = new Todo("1", "YOUR NOT THE SECOUND", TodoStatus.DONE);
    TodoDto todoDto = new TodoDto("YOUR NOT THE SECOUND", TodoStatus.DONE);

    when(mockIdService.generateTodoId()).thenReturn("1");
    when(mockTodoRepository.save(expected)).thenReturn(expected);

    // WHEN
    Todo actual = service.addTodo(todoDto);

    // THEN
    assertEquals(expected, actual);
    verify(mockTodoRepository).save(expected);
  }

  @Test
  void updateTodo() {
  }

  @Test
  void deleteTodo_shouldCallDelete_whenTodoExists() {
    // GIVEN
    Todo todo = new Todo("1", "Test-Todo", TodoStatus.OPEN);
    when(mockTodoRepository.findById("1")).thenReturn(Optional.of(todo));
    TodoService service = new TodoService(mockTodoRepository, mockIdService);

    // WHEN
    service.deleteTodo("1");

    // THEN
    verify(mockTodoRepository).delete(todo);
  }

  @Test
  void searchTodosByStatus_shouldReturnAListWithTodosByStatus_whenCalled() {

  }
}