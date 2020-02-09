package tim31.pswisa.repository;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tim31.pswisa.constants.CheckupConstants;
import tim31.pswisa.constants.DoctorConstants;
import tim31.pswisa.model.Checkup;
import tim31.pswisa.model.MedicalWorker;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class DoctorRepositoryTest {
	@Autowired
	MedicalWorkerRepository medicalworkerRepository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void testFindOneById() {
		MedicalWorker doctorTest = new MedicalWorker();	
		doctorTest.setPhone(DoctorConstants.DOCTOR_PHONE);
		entityManager.persistAndFlush(doctorTest);		
		MedicalWorker doctor = medicalworkerRepository.findOneById(doctorTest.getId());
		assertEquals(doctorTest.getId(), doctor.getId());
		assertEquals(doctorTest.getPhone(), doctor.getPhone());
	}
}
