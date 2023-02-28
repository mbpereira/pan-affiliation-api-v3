package pan.affiliation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pan.affiliation.shared.caching.providers.redis.RedisCacheProvider;

@SpringBootTest
@MockBean(classes = RedisCacheProvider.class)
class AffiliationApplicationTests {

	@Test
	void contextLoads() {
	}

}
