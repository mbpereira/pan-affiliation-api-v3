package pan.affiliation.infrastructure.gateways.shared;

import pan.affiliation.shared.exceptions.QueryException;

import java.util.List;

public interface Http {
    void setBaseUrl(String baseUrl);

    <T> T get(String path, Class<T> responseClass) throws QueryException;
}
