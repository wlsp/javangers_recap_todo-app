package wlsp.tech.javangers_recap_todoapp.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class IdServiceTest {

  private final IdService idService = new IdService();

  @Test
  void generateTodoId() {
    //WHEN
    String id = idService.generateTodoId();
    assertThat(id).isNotNull();
    assertThat(id).matches("^[0-9a-fA-F\\\\-]{36}$");
  }
}