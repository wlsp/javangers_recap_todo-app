package wlsp.tech.javangers_recap_todoapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import wlsp.tech.javangers_recap_todoapp.model.Enum.TodoStatus;
import wlsp.tech.javangers_recap_todoapp.model.Todo;
import wlsp.tech.javangers_recap_todoapp.repository.TodoRepository;


@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {
@Autowired
private MockMvc mockMvc;

@Autowired
private TodoRepository todoRepository;

  @BeforeEach
  void setUp() {
    todoRepository.deleteAll();
  }

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
  @Test
  void updateTodo() throws Exception {
    Todo todo = new Todo("id", "Test", TodoStatus.OPEN);
    todoRepository.save(todo);

    mockMvc.perform(put("/api/todo/" + todo.id())
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

//  @Test
//  void searchTodosByStatus_withValidLowercaseStatus_shouldReturnTodos() throws Exception {
//    Todo todo1 = new Todo("1", "Task 1", TodoStatus.DONE);
//    Todo todo2 = new Todo("2", "Task 2", TodoStatus.DONE);
//
//    todoRepository.saveAll(List.of(todo1, todo2));
//    mockMvc.perform(get("/search")
//                    .param("status", "done"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$", hasSize(2))) // 2 DONE-Todos
//            .andExpect((ResultMatcher) jsonPath("$[0].status", is("DONE")))
//            .andExpect((ResultMatcher) jsonPath("$[1].status", is("DONE")));
//  }

}