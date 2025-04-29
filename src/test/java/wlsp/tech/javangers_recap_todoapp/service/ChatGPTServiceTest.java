package wlsp.tech.javangers_recap_todoapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(ChatGPTService.class)
@SuppressWarnings("SpringBootApplicationProperties")
@TestPropertySource(properties = {"OPEN_AI_URI=https://api.dummy-api.com", "OPEN_AI_KEY=dummy-api-key"})
@AutoConfigureMockRestServiceServer
@ActiveProfiles("test")
class ChatGPTServiceTest {

  @Autowired
  private ChatGPTService chatGPTService;

  @Autowired
  private MockRestServiceServer mockServer;

  @Value("${OPEN_AI_URI}")
  private String OPEN_AI_URI;

  @Test
  void checkSpelling_shouldReturnCorrectedSentence() {
    String expectedText = "This is the text.";
    String responseJson = """
            {
              "choices": [
                {
                  "message": {
                    "role": "assistant",
                    "content": "%s"
                  }
                }
              ]
            }
            """.formatted(expectedText);

    mockServer.expect(requestTo(OPEN_AI_URI + "/v1/chat/completions"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(header("Authorization", "Bearer dummy-api-key"))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

    String result = chatGPTService.checkSpelling("Ths is teh text.");

    assertThat(result).isEqualTo(expectedText);
    mockServer.verify();
  }
}