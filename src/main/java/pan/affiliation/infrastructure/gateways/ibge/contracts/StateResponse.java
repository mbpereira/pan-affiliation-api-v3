package pan.affiliation.infrastructure.gateways.ibge.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public record StateResponse(
        @JsonProperty("id") int id,
        @JsonProperty("nome")  String name,
        @JsonProperty("sigla") String acronym)
{
}
