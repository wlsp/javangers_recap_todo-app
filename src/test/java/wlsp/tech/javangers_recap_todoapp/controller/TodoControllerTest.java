package wlsp.tech.javangers_recap_todoapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import wlsp.tech.javangers_recap_todoapp.model.Enum.TodoStatus;
import wlsp.tech.javangers_recap_todoapp.model.Todo;
import wlsp.tech.javangers_recap_todoapp.repository.TodoRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {
@Autowired
private MockMvc mockMvc;

@Autowired
private TodoRepository todoRepository;

  @Test
  void getAllTodos_shouldReturnListWIthTodos_whenCalled() throws Exception {
    //GIVEN
    Todo todo = new Todo("1", "description", TodoStatus.OPEN);
    todoRepository.save(todo);

    mockMvc.perform(get("/api/todo"))
            .andExpect(status().isOk())
            .andExpect(content().json(
                    """
                                [
                                  {
                                    "id": "1",
                                    "description": "description",
                                    "status": "OPEN"
                                  }
                                ]
                                """
            ));

  }

  @Test
  void getTodoById_shouldReturnTodoWithId() throws Exception {
    //Given
    Todo todo = new Todo("2", "description2", TodoStatus.IN_PROGRESS);
    todoRepository.save(todo);

    mockMvc.perform(get("/api/todo/2"))
            .andExpect(status().isOk())
            .andExpect(content().json(
                    """
                               {
                               "id": "2",
                               "description": "description2",
                               "status": "IN_PROGRESS"
                               }
                               """
            ));
  }

  @Test
  void addTodo_shouldAddTodo_whenCalledWithValidData() throws Exception {
    mockMvc.perform(post("/api/todo")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                              {
                                "description": "desc",
                                "status": "OPEN"
                              }
                            """)
            )
            .andExpect(status().isOk())
            .andExpect(content().json("""
{
                                "description": "desc",
                                "status": "OPEN"
                              }
""")
            )
            .andExpect(jsonPath("$.id").isNotEmpty());
  }

}