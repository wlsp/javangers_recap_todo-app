package wlsp.tech.javangers_recap_todoapp.model;

import java.util.List;

public record OpenAiRes(List<OpenAiChoice> choices) {
}
