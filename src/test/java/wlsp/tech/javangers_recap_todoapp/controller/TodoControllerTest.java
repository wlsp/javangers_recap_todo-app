package wlsp.tech.javangers_recap_todoapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import wlsp.tech.javangers_recap_todoapp.model.Enum.TodoStatus;
import wlsp.tech.javangers_recap_todoapp.model.Todo;
import wlsp.tech.javangers_recap_todoapp.repository.TodoRepository;

@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest(properties = { "OPEN_AI_URI=https://api.openai.com", "OPEN_AI_KEY=12345" })
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TodoControllerTest {

@Autowired
private MockMvc mockMvc;

@Autowired
private MockRestServiceServer mockRestServiceServer;

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
  void getTodoById_shouldReturnNotFound_whenTodoDoesNotExist() throws Exception {
    mockMvc.perform(get("/api/todo/12345"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Resource Not Found"))
            .andExpect(jsonPath("$.message").value("Could not found todo with  the id: 12345."))
            .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void addTodo_shouldAddTodo_whenCalledWithValidData() throws Exception {
    mockRestServiceServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess("""
                      {
                       "choices": [
                                   {
                                     "message": {
                                       "content": "desc"
                                     }
                                   }
                                ]
                      }
                    """, MediaType.APPLICATION_JSON));

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

  @Test
  void updateTodo() throws Exception {
    Todo todo = new Todo("id", "Test", TodoStatus.OPEN);
    todoRepository.save(todo);

    mockMvc.perform(put("/api/todo/id", todo.id())
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
{
                                "description": "Test",
                                "status": "OPEN"
                              }""")).andExpect(status().isOk())
            .andExpect(content().json("""

                    
{
                                "description": "Test",
                                "status": "OPEN"
                              }
"""));
  }

  @Test
  void searchTodosByStatus_shouldReturnListOfTodos() throws Exception {
    // GIVEN
    Todo todo1 = new Todo("1", "Test Task 1", TodoStatus.OPEN);
    Todo todo2 = new Todo("2", "Test Task 2", TodoStatus.OPEN);
    Todo todo3 = new Todo("3", "Test Task 3", TodoStatus.DONE);
    todoRepository.save(todo1);
    todoRepository.save(todo2);
    todoRepository.save(todo3);

    // WHEN + THEN
    mockMvc.perform(get("/api/todo/search")
                    .param("status", "OPEN"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value("1"))
            .andExpect(jsonPath("$[1].id").value("2"))
            .andExpect(jsonPath("$[0].status").value("OPEN"))
            .andExpect(jsonPath("$[1].status").value("OPEN"));
  }


}