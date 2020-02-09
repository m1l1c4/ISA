package tim31.pswisa.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tim31.pswisa.constants.CheckupConstants;
import tim31.pswisa.constants.CheckupTypeConstants;
import tim31.pswisa.constants.ClinicConstants;
import tim31.pswisa.constants.DoctorConstants;
import tim31.pswisa.constants.PatientConstants;
import tim31.pswisa.constants.RoomConstants;
import tim31.pswisa.constants.UserConstants;
import tim31.pswisa.dto.CheckUpTypeDTO;
import tim31.pswisa.dto.CheckupDTO;
import tim31.pswisa.dto.ClinicDTO;
import tim31.pswisa.dto.MedicalWorkerDTO;
import tim31.pswisa.dto.RoomDTO;
import tim31.pswisa.model.CheckUpType;
import tim31.pswisa.model.Checkup;
import tim31.pswisa.model.Clinic;
import tim31.pswisa.model.ClinicAdministrator;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.Patient;
import tim31.pswisa.model.Room;
import tim31.pswisa.model.User;
import tim31.pswisa.repository.CheckUpRepository;
import tim31.pswisa.repository.MedicalWorkerRepository;
import tim31.pswisa.repository.RoomRepository;
import tim31.pswisa.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class CheckupServiceIntegracioniTest {
	@Autowired
	private CheckUpService checkupService;
	
	@MockBean
	private CheckUpRepository checkupRepositoryMocked;
	
	@MockBean
	private UserRepository userRepositoryMocked;
	
	@MockBean
	private PatientService patientServiceMocked;
	
	@MockBean
	private RoomRepository roomRepositoryMocked;
	
	@MockBean
	private MedicalWorkerRepository doctorRepositoryMocked;
	
	@MockBean
	private EmailService emailServiceMocked;
	
	@MockBean
	private ClinicService clinicServiceMocked;
	
	@MockBean
	private CheckUpTypeService checkuptypeServiceMocked;
	
	@MockBean
	private ClinicAdministratorService clAdminsServiceMocked;
	
	@MockBean
	private MedicalWorkerService medicalWorkerServiceMocked;
	
	@Test
	public void testBookQuickAppFalseIntegracioni() throws Exception {
		
		Checkup checkupTest = new Checkup();
		checkupTest.setScheduled(CheckupConstants.CHECKUP_SCHEDULED);
		checkupTest.setDate(CheckupConstants.CHECKUP_DATE);
		checkupTest.setId(CheckupConstants.CHECKUP_ID_FALSE);		
		Checkup ret = checkupService.bookQuickApp(checkupTest.getId(), UserConstants.USER1_EMAIL);
		assertNull(ret);
	}
	
	@Test
	public void testBookQuickAppIntegracioni() throws Exception {
		Checkup ret = checkupService.bookQuickApp(CheckupConstants.ID_2, UserConstants.USER2_EMAIL);
		assertEquals(CheckupConstants.ID_2, ret.getId());
	}
	
	@Test
	public void testUpdate() {
		CheckupDTO checkupTest = new CheckupDTO();	// input
		Checkup retCheckup = new Checkup();
		MedicalWorkerDTO doctorTest = new MedicalWorkerDTO();		
		doctorTest.setId(DoctorConstants.DOCTOR_ID);
		checkupTest.setScheduled(CheckupConstants.CHECKUP_SCHEDULED);
		checkupTest.setDate(CheckupConstants.CHECKUP_DATE);
		checkupTest.setTime(CheckupConstants.CHECKUP_TIME);
		checkupTest.setId(CheckupConstants.CHECKUP_ID);
		checkupTest.setMedicalWorker(doctorTest);
				
		RoomDTO testRoom = new RoomDTO();
		Room tempRoom = new Room();
		testRoom.setName(RoomConstants.ROOM_NAME);;
		testRoom.setNumber(RoomConstants.ROOM_NUMBER);
		testRoom.setTypeRoom(RoomConstants.ROOM_TYPE);
		testRoom.setId(RoomConstants.ROOM_ID);
		checkupTest.setRoom(testRoom);
		        
		try {
			retCheckup = checkupService.update(checkupTest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertNotNull(retCheckup);
		
	}
	
	@Test
	public void testUpdateFalse() {		
		CheckupDTO checkupTest = new CheckupDTO();	// input
		Checkup tempCheckup = new Checkup();		// testing value
		Checkup retCheckup = new Checkup();
		MedicalWorkerDTO doctorTest = new MedicalWorkerDTO();		
		doctorTest.setId(DoctorConstants.DOCTOR_ID);
		checkupTest.setScheduled(CheckupConstants.CHECKUP_SCHEDULED);
		checkupTest.setDate(CheckupConstants.CHECKUP_DATE);
		checkupTest.setTime(CheckupConstants.CHECKUP_TIME_FALSE);
		checkupTest.setId(CheckupConstants.CHECKUP_ID);
		checkupTest.setMedicalWorker(doctorTest);
		
		RoomDTO testRoom = new RoomDTO();
		Room tempRoom = new Room();
		testRoom.setName(RoomConstants.ROOM_NAME);;
		testRoom.setNumber(RoomConstants.ROOM_NUMBER);
		testRoom.setTypeRoom(RoomConstants.ROOM_TYPE);
		testRoom.setId(RoomConstants.ROOM_ID);
		checkupTest.setRoom(testRoom);
		        
		try {
			retCheckup = checkupService.update(checkupTest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertNull(retCheckup);
	}
	
	@Test	
	public void checkupToAdminIntegracioniTest() {		
		CheckupDTO inputDto = new CheckupDTO();
		inputDto.setDate(CheckupConstants.CHECKUP_DATER);
		inputDto.setTime(CheckupConstants.CHECKUP_TIME);
		inputDto.setType("PREGLED");
		User u = new User();
		u.setEmail("pacijent@gmail.com");
		u.setId(PatientConstants.PATIENT_ID);
		Patient p = new Patient();
		p.setUser(u);
		
		MedicalWorkerDTO inputMwDto = new MedicalWorkerDTO();
		inputMwDto.setId(DoctorConstants.DOCTOR_ID);
		inputDto.setMedicalWorker(inputMwDto);
		ClinicDTO clinicDto = new ClinicDTO();
		clinicDto.setName(ClinicConstants.NAZIV_1);
		inputDto.setClinic(clinicDto);
				
		CheckUpTypeDTO inputTypeTest = new CheckUpTypeDTO();
		inputTypeTest.setName(CheckupTypeConstants.CHECK_UP_TYPE_NAME);
		inputDto.setCheckUpType(inputTypeTest);
		CheckUpType retCheckUpType = new CheckUpType();
		retCheckUpType.setName(CheckupTypeConstants.CHECK_UP_TYPE_NAME);
	
		boolean ret = false;
		try {
			ret = checkupService.checkupToAdmin(inputDto, "pacijent@gmail.com");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(ret);
	
	}
	
	
}
