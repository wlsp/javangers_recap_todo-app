package wlsp.tech.javangers_recap_todoapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wlsp.tech.javangers_recap_todoapp.model.Enum.TodoStatus;
import wlsp.tech.javangers_recap_todoapp.model.Todo;
import wlsp.tech.javangers_recap_todoapp.exceptions.TodoNotFoundException;
import wlsp.tech.javangers_recap_todoapp.model.dto.TodoDto;
import wlsp.tech.javangers_recap_todoapp.repository.TodoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class TodoServiceTest {

  IdService mockIdService = mock(IdService.class);
  TodoRepository mockTodoRepository = mock(TodoRepository.class);
  ChatGPTService mockChatGPTService = mock(ChatGPTService.class);

  @BeforeEach
  void setUp() {
    openMocks(this);
  }

  @Test
  void getAllTodos_shouldReturnListOfTodos_whenCalled() {
    //GIVEN
    TodoService service = new TodoService(mockTodoRepository, mockIdService, mockChatGPTService);
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
    TodoService service = new TodoService(mockTodoRepository, mockIdService, mockChatGPTService);
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
  void updateTodo() {
    TodoService todoService = new TodoService(mockTodoRepository, mockIdService, mockChatGPTService);
    Todo todo = new Todo("1", "Todo", TodoStatus.OPEN);

    when(mockTodoRepository.findById("1")).thenReturn(Optional.of(todo));

    Todo actual = todoService.updateTodo("1", todo);

    assertEquals(todo, actual);
    verify(mockTodoRepository).save(todo);
  }

  @Test
  void addTodo_shouldReturnTodo_whenCalledWithDto() {
    // GIVEN
    TodoService service = new TodoService(mockTodoRepository, mockIdService, mockChatGPTService);

    TodoDto todoDto = new TodoDto("YOUR NOT THE SECOND", TodoStatus.DONE);
    String generatedId = "1";
    String correctedDescription = "You're not the second";

    Todo expected = new Todo(generatedId, correctedDescription, TodoStatus.DONE);

    when(mockIdService.generateTodoId()).thenReturn(generatedId);
    when(mockChatGPTService.checkSpelling(anyString())).thenReturn(correctedDescription);
    when(mockTodoRepository.save(any(Todo.class))).thenReturn(expected);

    // WHEN
    Todo actual = service.addTodo(todoDto);

    // THEN
    assertEquals(expected, actual);
    verify(mockTodoRepository).save(any(Todo.class));
  }


  @Test
  void deleteTodo_shouldCallDelete_whenTodoExists() {
    // GIVEN
    Todo todo = new Todo("1", "Test-Todo", TodoStatus.OPEN);
    when(mockTodoRepository.findById("1")).thenReturn(Optional.of(todo));
    TodoService service = new TodoService(mockTodoRepository, mockIdService, mockChatGPTService);

    // WHEN
    service.deleteTodo("1");

    // THEN
    verify(mockTodoRepository).delete(todo);
  }

  @Test
  void searchTodosByStatus_shouldReturnFilteredTodos() {
    // GIVEN
    TodoService service = new TodoService(mockTodoRepository, mockIdService, mockChatGPTService);
    Todo todo1 = new Todo("1", "Task 1", TodoStatus.DONE);
    List<Todo> expected = List.of(todo1);

    when(mockTodoRepository.findAllByStatus(TodoStatus.DONE)).thenReturn(expected);

    // WHEN
    List<Todo> actual = service.getAlTodosByStatus(TodoStatus.DONE);

    // THEN
    assertEquals(expected, actual);
    verify(mockTodoRepository).findAllByStatus(TodoStatus.DONE);
  }

}