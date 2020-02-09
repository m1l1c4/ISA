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

import tim31.pswisa.constants.DoctorConstants;
import tim31.pswisa.constants.RoomConstants;
import tim31.pswisa.constants.UserConstants;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.Room;
import tim31.pswisa.repository.MedicalWorkerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DoctorServiceTest {
	@MockBean
	private MedicalWorkerRepository doctorRepositoryMocked;
	
	@Autowired
	private MedicalWorkerService doctorService;
	
	@Test
	public void findOneByIdTest() {
		MedicalWorker doctorTest = new MedicalWorker();
		doctorTest.setId(DoctorConstants.DOCTOR_ID);				
		Mockito.when(doctorRepositoryMocked.findOneById(doctorTest.getId())).thenReturn(doctorTest);
		MedicalWorker doctor = doctorService.findOneById(DoctorConstants.DOCTOR_ID);
		assertEquals(DoctorConstants.DOCTOR_ID, doctor.getId());		
	}
}
