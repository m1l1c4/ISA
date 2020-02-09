package tim31.pswisa.repository;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tim31.pswisa.constants.UserConstants;
import tim31.pswisa.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testFindOneByEmail() {
		User testUser = new User();
		testUser.setEmail(UserConstants.USER1_EMAIL);
		testUser.setName(UserConstants.USER1_NAME);
		testUser.setSurname(UserConstants.USER1_SURNAME);
		testUser.setPassword(UserConstants.USER1_PASS);
		String email = UserConstants.USER1_EMAIL;
		testUser = entityManager.persistAndFlush(testUser);
		User user = userRepository.findOneByEmail(email);
		assertEquals(testUser.getEmail(), user.getEmail());
		assertEquals(testUser.getId(), user.getId());
	}

}
