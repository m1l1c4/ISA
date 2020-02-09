package tim31.pswisa.service;

import static org.junit.Assert.*;
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
public class CheckupServiceUnitTest {

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
	public void testBookQuickAppFalse() throws Exception {
		
		Checkup checkupTest = new Checkup();
		checkupTest.setScheduled(CheckupConstants.CHECKUP_SCHEDULED);
		checkupTest.setDate(CheckupConstants.CHECKUP_DATE);
		checkupTest.setId(CheckupConstants.CHECKUP_ID_FALSE);
		Mockito.when(checkupRepositoryMocked.findOneById(checkupTest.getId())).thenReturn(null);
		Checkup ret = checkupService.bookQuickApp(checkupTest.getId(), UserConstants.USER1_EMAIL);
		assertNull(ret);
	}
	
	@Test
	public void testBookQuickApp() throws Exception {
		Checkup checkupTest = new Checkup();
		checkupTest.setScheduled(CheckupConstants.CHECKUP_SCHEDULED);
		checkupTest.setDate(CheckupConstants.CHECKUP_DATE);
		checkupTest.setId(CheckupConstants.CHECKUP_ID2);
		
		Mockito.when(checkupRepositoryMocked.findOneById(checkupTest.getId())).thenReturn(checkupTest);
		
		User testUser = new User();
		testUser.setEmail(UserConstants.USER1_EMAIL);
		testUser.setName(UserConstants.USER1_NAME);
		testUser.setSurname(UserConstants.USER1_SURNAME);
		testUser.setPassword(UserConstants.USER1_PASS);
		testUser.setId(3L);
		
        Mockito.when(userRepositoryMocked.findOneByEmail(testUser.getEmail())).thenReturn(testUser);
		
		Patient testPatient = new Patient();
		testPatient.setUser(testUser);
		testPatient.setJbo(PatientConstants.PATIENT1_JBO);
		testPatient.setPhoneNumber(PatientConstants.PATIENT1_PHONE);
		testPatient.setCity(PatientConstants.PATIENT1_CITY);
		testPatient.setState(PatientConstants.PATIENT1_STATE);
		testPatient.setAddress(PatientConstants.PATIENT1_ADDRESS);
		testPatient.setProcessed(PatientConstants.PATIENT1_PROCESSED);
		testPatient.setId(1L);
		
		Mockito.when(patientServiceMocked.findOneByUserId(testPatient.getUser().getId())).thenReturn(testPatient);
		
		Checkup checkupTest2 = new Checkup();
		checkupTest2.setScheduled(CheckupConstants.CHECKUP_SCHEDULED);
		checkupTest2.setDate(CheckupConstants.CHECKUP_DATE);
		checkupTest2.setId(CheckupConstants.CHECKUP_ID2);
		checkupTest2.setPatient(testPatient);

		Mockito.when(checkupRepositoryMocked.save(checkupTest)).thenReturn(checkupTest2);
		Mockito.doNothing().when(emailServiceMocked).quickAppConfirmationEmail(UserConstants.EMAIL_1, checkupTest2);
		Checkup ret = checkupService.bookQuickApp(checkupTest.getId(), UserConstants.USER1_EMAIL);
		assertEquals(checkupTest2.getId(), ret.getId());
	}
	
	@Test
	public void testUpdate() {
		CheckupDTO checkupTest = new CheckupDTO();	// input
		Checkup tempCheckup = new Checkup();		// testing value
		Checkup retCheckup = new Checkup();
		MedicalWorkerDTO doctorTest = new MedicalWorkerDTO();		
		doctorTest.setId(DoctorConstants.DOCTOR_ID);
		checkupTest.setScheduled(CheckupConstants.CHECKUP_SCHEDULED);
		checkupTest.setDate(CheckupConstants.CHECKUP_DATE);
		checkupTest.setTime(CheckupConstants.CHECKUP_TIME);
		checkupTest.setId(CheckupConstants.CHECKUP_ID);
		checkupTest.setMedicalWorker(doctorTest);
		Mockito.when(checkupRepositoryMocked.findOneById(checkupTest.getId())).thenReturn(tempCheckup);
		
		RoomDTO testRoom = new RoomDTO();
		Room tempRoom = new Room();
		testRoom.setName(RoomConstants.ROOM_NAME);;
		testRoom.setNumber(RoomConstants.ROOM_NUMBER);
		testRoom.setTypeRoom(RoomConstants.ROOM_TYPE);
		testRoom.setId(RoomConstants.ROOM_ID);
		checkupTest.setRoom(testRoom);
		
        Mockito.when(roomRepositoryMocked.findOneById(checkupTest.getRoom().getId())).thenReturn(tempRoom);
		
        List<Checkup> allInRoomTest = new ArrayList<Checkup>();		
		Mockito.when(checkupRepositoryMocked.findAllByRoomIdAndTimeAndDate(tempRoom.getId(),
				checkupTest.getTime(), checkupTest.getDate())).thenReturn(allInRoomTest);
		
		tempCheckup.setDate(checkupTest.getDate());
		tempCheckup.setTime(checkupTest.getTime());
		tempCheckup.setRoom(tempRoom);
		tempCheckup.setScheduled(true);
		tempCheckup.setDoctors(new HashSet<MedicalWorker>());
		
		MedicalWorker doctorToSet = new MedicalWorker();		
		Mockito.when(doctorRepositoryMocked.findOneById(checkupTest.getMedicalWorker().getId()))
			   .thenReturn(doctorToSet);
		
		tempCheckup.getDoctors().add(doctorToSet);
		
        Mockito.when(checkupRepositoryMocked.save(tempCheckup)).thenReturn(tempCheckup);

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
		Mockito.when(checkupRepositoryMocked.findOneById(checkupTest.getId())).thenReturn(tempCheckup);
		
		RoomDTO testRoom = new RoomDTO();
		Room tempRoom = new Room();
		testRoom.setName(RoomConstants.ROOM_NAME);;
		testRoom.setNumber(RoomConstants.ROOM_NUMBER);
		testRoom.setTypeRoom(RoomConstants.ROOM_TYPE);
		testRoom.setId(RoomConstants.ROOM_ID);
		checkupTest.setRoom(testRoom);
		
        Mockito.when(roomRepositoryMocked.findOneById(checkupTest.getRoom().getId())).thenReturn(tempRoom);
		
        List<Checkup> allInRoomTest = new ArrayList<Checkup>();		
		Mockito.when(checkupRepositoryMocked.findAllByRoomIdAndTimeAndDate(tempRoom.getId(),
				checkupTest.getTime(), checkupTest.getDate())).thenReturn(allInRoomTest);
		
		try {
			retCheckup = checkupService.update(checkupTest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertNull(retCheckup);
	}
	
	@Test		//unit
	public void checkupToAdminTest() {		
		CheckupDTO inputDto = new CheckupDTO();
		inputDto.setDate(CheckupConstants.CHECKUP_DATER);
		inputDto.setTime(CheckupConstants.CHECKUP_TIME);
		inputDto.setType("PREGLED");
		Checkup newCh = new Checkup(0, false, inputDto.getDate(), inputDto.getTime(), inputDto.getType(),
						1, 0, null, false);
		User u = new User();
		u.setEmail("pacijent@gmail.com");
		u.setId(PatientConstants.PATIENT_ID);
		Mockito.when(userRepositoryMocked.findOneByEmail("pacijent@gmail.com")).thenReturn(u);
		
		Patient p = new Patient();
		p.setUser(u);
		Mockito.when(patientServiceMocked.findOneByUserId(u.getId())).thenReturn(p);
		
		MedicalWorkerDTO inputMwDto = new MedicalWorkerDTO();
		inputMwDto.setId(DoctorConstants.DOCTOR_ID);
		inputDto.setMedicalWorker(inputMwDto);
		ClinicDTO clinicDto = new ClinicDTO();
		clinicDto.setName(ClinicConstants.NAZIV_1);
		inputDto.setClinic(clinicDto);
		Clinic clinic = new Clinic();
		Mockito.when(clinicServiceMocked.findOneByName(clinicDto.getName())).thenReturn(clinic);
		
		CheckUpTypeDTO inputTypeTest = new CheckUpTypeDTO();
		inputTypeTest.setName(CheckupTypeConstants.CHECK_UP_TYPE_NAME);
		inputDto.setCheckUpType(inputTypeTest);
		CheckUpType retCheckUpType = new CheckUpType();
		retCheckUpType.setName(CheckupTypeConstants.CHECK_UP_TYPE_NAME);
		Mockito.when(checkuptypeServiceMocked.findOneByName(inputDto.getCheckUpType().getName())).thenReturn(retCheckUpType);
	
		ArrayList<ClinicAdministrator> admins = new ArrayList<ClinicAdministrator>(1);
		Mockito.when(clAdminsServiceMocked.findAll()).thenReturn(admins);
		MedicalWorker doctor = new MedicalWorker();
		Mockito.when(medicalWorkerServiceMocked.myFindOne(inputDto.getMedicalWorker().getId()))
			   .thenReturn(doctor);
		
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
