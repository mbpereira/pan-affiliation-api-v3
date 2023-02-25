package pan.affiliation.api.contracts;

import pan.affiliation.shared.validation.Error;

import java.util.List;

public class GenericResponse<T> {
    public T data;
    public List<Error> errors;

    public GenericResponse(T data, List<Error> errors) {
        this.data = data;
        this.errors = errors;
    }
}
