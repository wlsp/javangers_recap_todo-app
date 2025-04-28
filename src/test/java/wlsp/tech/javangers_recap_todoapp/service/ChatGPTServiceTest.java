package wlsp.tech.javangers_recap_todoapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(ChatGPTService.class)
@AutoConfigureMockRestServiceServer
@TestPropertySource(properties = {
        "OPEN_AI_URI=https://api.openai.com",
        "OPEN_AI_KEY=dummy-api-key"
})
class ChatGPTServiceTest {

  @Autowired
  private ChatGPTService chatGPTService;

  @Autowired
  private MockRestServiceServer mockServer;

  @Test
  void checkSpelling_shouldReturnCorrectedSentence_whenSpellCheckingIsCalled() {
    // GIVEN
    String correctedText = "This is the text.";

    String openAiResponseJson = """
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
                """.formatted(correctedText);

    mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(openAiResponseJson, MediaType.APPLICATION_JSON));

    // WHEN
    String result = chatGPTService.checkSpelling("Ths is teh text.");

    // THEN
    assertThat(result).isEqualTo(correctedText);

    mockServer.verify();
  }

}