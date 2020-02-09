package tim31.pswisa.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import tim31.pswisa.constants.CheckupTypeConstants;
import tim31.pswisa.model.CheckUpType;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class CheckupTypeRepositoryTest {

	@Autowired
	private CheckUpTypeRepository checkupTypeRepository;

	@Test
	public void testFindOneByName() {
		CheckUpType checkUpType = checkupTypeRepository.findOneByName(CheckupTypeConstants.CHECK_UP_TYPE_NAME);
		assertEquals(CheckupTypeConstants.CHECK_UP_TYPE_NAME, checkUpType.getName());
		assertEquals(CheckupTypeConstants.CHECK_UP_TYPE_ID, checkUpType.getId());
		assertNotSame(CheckupTypeConstants.CHECK_UP_TYPE_ID_FALSE, checkUpType.getId());
	}

}
