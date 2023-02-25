package pan.affiliation.infrastructure.gateways.viacep.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostalCodeInformationResponse {
    @JsonProperty("cep")
    private String postalCode;
    @JsonProperty("logradouro")
    private String street;
    @JsonProperty("bairro")
    private String neighborhood;
    @JsonProperty("uf")
    private String state;
    @JsonProperty("localidade")
    private String city;
}
