package pan.affiliation.domain.shared;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class BaseEntitiy {
    @NotNull
    protected UUID id;

    public void generateId() {
        this.id = UUID.randomUUID();
    }
}