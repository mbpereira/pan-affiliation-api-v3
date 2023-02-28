package pan.affiliation.application.usecases.customers;

import java.util.UUID;

public record RemoveAddressInput(UUID customerId, UUID addressId) {
}
