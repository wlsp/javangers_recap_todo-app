package wlsp.tech.javangers_recap_todoapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import wlsp.tech.javangers_recap_todoapp.model.OpenAiMessages;
import wlsp.tech.javangers_recap_todoapp.model.OpenAiRes;
import wlsp.tech.javangers_recap_todoapp.model.SpellCheckingReq;

import java.util.List;

@Service
public class ChatGPTService {

  private final RestClient restClient;

  public ChatGPTService(
          RestClient.Builder restClientBuilder,
          @Value("${OPEN_AI_URI}") String OPEN_AI_URI,
          @Value("${OPEN_AI_KEY}") String OPEN_AI_KEY
  ) {
    this.restClient = restClientBuilder
            .baseUrl(OPEN_AI_URI)
            .defaultHeader("Authorization", "Bearer " + OPEN_AI_KEY)
            .build();
  }

  public String checkSpelling(String text){
    SpellCheckingReq req = new SpellCheckingReq(
            "gpt-4.1",
            List.of(new OpenAiMessages("user", "Please check and correct the spelling of the following text and return only the corrected text without explaining: " + text))
    );

    OpenAiRes response = restClient.post()
            .uri("/v1/chat/completions")
            .contentType(MediaType.APPLICATION_JSON)
            .body(req)
            .retrieve()
            .body(OpenAiRes.class);


    assert response != null;
    return response.choices().getFirst().message().content();

  }

}
