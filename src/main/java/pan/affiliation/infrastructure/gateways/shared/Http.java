package pan.affiliation.infrastructure.gateways.shared;

import pan.affiliation.shared.exceptions.HttpException;

import java.io.IOException;

public interface Http {
    void setBaseUrl(String baseUrl);

    <TOutput> TOutput get(String path, Class<TOutput> responseClass) throws IOException, InterruptedException, HttpException;
}
