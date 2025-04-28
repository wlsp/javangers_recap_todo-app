package wlsp.tech.javangers_recap_todoapp.model;

import java.util.List;

public record SpellCheckingReq(
        String model,
        List<OpenAiMessages> messages
) {
}
