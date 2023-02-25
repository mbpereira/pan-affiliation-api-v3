package pan.affiliation.domain.modules.localization.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class State {
    private int id;
    private String acronym;
    private String name;
}
