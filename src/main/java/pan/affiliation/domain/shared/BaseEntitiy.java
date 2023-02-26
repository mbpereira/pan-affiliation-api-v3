package pan.affiliation.domain.shared;

import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class BaseEntitiy {
    protected UUID id;

    public void generateId() {
        this.id = UUID.randomUUID();
    }
}