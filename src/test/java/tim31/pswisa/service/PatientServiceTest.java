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

import tim31.pswisa.constants.PatientConstants;
import tim31.pswisa.constants.UserConstants;
import tim31.pswisa.model.Patient;
import tim31.pswisa.model.User;
import tim31.pswisa.repository.PatientRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class PatientServiceTest {

	@Autowired
	private PatientService patientService;
	
	@MockBean
	private PatientRepository patientRepositoryMocked;
	
	@Test
	public void testFindOneByUserId() {
		User testUser = new User();
		testUser.setEmail(UserConstants.USER1_EMAIL);
		testUser.setName(UserConstants.USER1_NAME);
		testUser.setSurname(UserConstants.USER1_SURNAME);
		testUser.setPassword(UserConstants.USER1_PASS);
		testUser.setId(100L);
		
		Patient testPatient = new Patient();
		testPatient.setUser(testUser);
		testPatient.setJbo(PatientConstants.PATIENT1_JBO);
		testPatient.setPhoneNumber(PatientConstants.PATIENT1_PHONE);
		testPatient.setCity(PatientConstants.PATIENT1_CITY);
		testPatient.setState(PatientConstants.PATIENT1_STATE);
		testPatient.setAddress(PatientConstants.PATIENT1_ADDRESS);
		testPatient.setProcessed(PatientConstants.PATIENT1_PROCESSED);
		testPatient.setId(100L);
		
		Mockito.when(patientRepositoryMocked.findByUserId(testPatient.getUser().getId())).thenReturn(testPatient);

		Patient patient = patientService.findOneByUserId(testPatient.getUser().getId());
		
		assertEquals(testPatient.getUser().getId(), patient.getUser().getId());
		assertEquals(testPatient.getId(), patient.getId());
		assertEquals(testPatient.getJbo(), patient.getJbo());
		verify(patientRepositoryMocked,times(1)).findByUserId(testPatient.getUser().getId());
	}
}
