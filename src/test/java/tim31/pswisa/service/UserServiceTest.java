package tim31.pswisa.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tim31.pswisa.constants.UserConstants;
import tim31.pswisa.model.User;
import tim31.pswisa.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@MockBean
	private UserRepository userRepositoryMocked;
	
	@Test
	public void testFindOneByEmail() {
		User testUser = new User();
		String email = UserConstants.USER1_EMAIL;
		testUser.setEmail(email);
		testUser.setName(UserConstants.USER1_NAME);
		testUser.setSurname(UserConstants.USER1_SURNAME);
		testUser.setPassword(UserConstants.USER1_PASS);
		testUser.setId(UserConstants.USER1_ID);
		
        Mockito.when(userRepositoryMocked.findOneByEmail(email)).thenReturn(testUser);
        
		User user = userService.findOneByEmail(email);
		assertEquals(testUser.getEmail(), user.getEmail());
		assertEquals(testUser.getId(), user.getId());
		verify(userRepositoryMocked, times(1)).findOneByEmail(email);
	}
}
