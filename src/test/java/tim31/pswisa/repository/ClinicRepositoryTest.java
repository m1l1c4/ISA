package tim31.pswisa.repository;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tim31.pswisa.constants.ClinicConstants;
import tim31.pswisa.constants.DoctorConstants;
import tim31.pswisa.model.Clinic;
import tim31.pswisa.model.MedicalWorker;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class ClinicRepositoryTest {
	@Autowired
	ClinicRepository clinicRepository;

	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void testFindOneByName() {
		Clinic clinicTest = new Clinic();	
		clinicTest.setCity(ClinicConstants.GRAD_1);
		entityManager.persistAndFlush(clinicTest);		
		Clinic clinic = clinicRepository.findOneByName(clinicTest.getName());
		assertEquals(clinicTest.getId(), clinic.getId());
		assertEquals(clinicTest.getCity(), clinic.getCity());
	}

}
