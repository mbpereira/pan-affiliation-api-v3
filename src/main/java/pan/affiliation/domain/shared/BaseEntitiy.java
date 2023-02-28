package pan.affiliation.domain.shared;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class BaseEntitiy {
    @NotNull
    private UUID id;

    public void generateId() {
        this.id = UUID.randomUUID();
    }

    protected void setId(UUID id) {
        if (id == null) {
            this.generateId();
            return;
        }

        this.id = id;
    }
}