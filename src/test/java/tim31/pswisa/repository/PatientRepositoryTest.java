package tim31.pswisa.repository;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tim31.pswisa.constants.PatientConstants;
import tim31.pswisa.constants.UserConstants;
import tim31.pswisa.model.Patient;
import tim31.pswisa.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class PatientRepositoryTest {

	@Autowired PatientRepository patientRepository;
	
	@Autowired TestEntityManager entityManager;
	
	@Test
	public void testFindByUserId() {
		User testUser = new User();
		testUser.setEmail(UserConstants.USER1_EMAIL);
		testUser.setName(UserConstants.USER1_NAME);
		testUser.setSurname(UserConstants.USER1_SURNAME);
		testUser.setPassword(UserConstants.USER1_PASS);
		testUser = entityManager.persistAndFlush(testUser);
		
		Patient testPatient = new Patient();
		testPatient.setUser(testUser);
		testPatient.setJbo(PatientConstants.PATIENT1_JBO);
		testPatient.setPhoneNumber(PatientConstants.PATIENT1_PHONE);
		testPatient.setCity(PatientConstants.PATIENT1_CITY);
		testPatient.setState(PatientConstants.PATIENT1_STATE);
		testPatient.setAddress(PatientConstants.PATIENT1_ADDRESS);
		testPatient.setProcessed(PatientConstants.PATIENT1_PROCESSED);
		testPatient = entityManager.persistAndFlush(testPatient);
		Patient patient = patientRepository.findByUserId(testPatient.getUser().getId());
		assertEquals(testPatient.getUser().getId(), patient.getUser().getId());
		assertEquals(testPatient.getId(), patient.getId());
		assertEquals(testPatient.getJbo(), patient.getJbo());
	}
}
