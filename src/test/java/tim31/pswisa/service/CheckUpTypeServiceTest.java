package tim31.pswisa.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tim31.pswisa.constants.CheckupTypeConstants;
import tim31.pswisa.constants.DoctorConstants;
import tim31.pswisa.model.CheckUpType;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.repository.CheckUpTypeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class CheckUpTypeServiceTest {
	
	@MockBean
	private CheckUpTypeRepository checkUpRepositoryMocked;
	
	@Autowired
	private CheckUpTypeService checkUpTypeService;
	
	@Test
	public void findOneByNameTest() {
		CheckUpType chTypeTest = new CheckUpType();
		chTypeTest.setName(CheckupTypeConstants.CHECK_UP_TYPE_NAME);				
		Mockito.when(checkUpRepositoryMocked.findOneByName(chTypeTest.getName())).thenReturn(chTypeTest);
		CheckUpType chType = checkUpTypeService.findOneByName(CheckupTypeConstants.CHECK_UP_TYPE_NAME);
		assertEquals(DoctorConstants.DOCTOR_ID, chType.getName());		
	}
}
