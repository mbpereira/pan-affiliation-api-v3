package pan.affiliation.infrastructure.shared.http.helpers;

import pan.affiliation.infrastructure.shared.http.abstractions.HttpServiceFactory;

public interface ServiceCreator<T> {
    T createService(HttpServiceFactory factory);
}
