package pan.affiliation.infrastructure.shared.http.abstractions;

import pan.affiliation.shared.exceptions.QueryException;

public interface HttpService {
    <T> T get(String path, Class<T> responseClass) throws QueryException;
}
