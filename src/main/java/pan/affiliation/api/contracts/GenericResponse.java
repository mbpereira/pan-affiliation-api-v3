package pan.affiliation.api.contracts;

import pan.affiliation.shared.validation.Error;

import java.util.List;

public record GenericResponse<T>(T data, List<Error> errors) {
}