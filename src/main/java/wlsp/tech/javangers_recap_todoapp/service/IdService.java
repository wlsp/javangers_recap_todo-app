package wlsp.tech.javangers_recap_todoapp.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdService {
  public String generateTodoId(){
    return UUID.randomUUID().toString();
  }
}
