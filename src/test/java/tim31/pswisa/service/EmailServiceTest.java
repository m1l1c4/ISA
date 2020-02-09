package tim31.pswisa.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;

import javax.mail.MessagingException;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tim31.pswisa.constants.CheckupConstants;
import tim31.pswisa.constants.ClinicConstants;
import tim31.pswisa.constants.DoctorConstants;
import tim31.pswisa.constants.RoomConstants;
import tim31.pswisa.constants.UserConstants;
import tim31.pswisa.model.Checkup;
import tim31.pswisa.model.Clinic;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.Room;
import tim31.pswisa.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class EmailServiceTest {

	@Autowired
	private EmailService emailService;

	@Test
	public void testQuickAppConfirmationEmail() throws InterruptedException, MessagingException, NullPointerException {
		Checkup checkupTest = new Checkup();
		MedicalWorker doctorTest = new MedicalWorker();
		User user1 = new User();
		user1.setName(UserConstants.IME_1);
		user1.setSurname(UserConstants.PREZIME_1);
		user1.setType(UserConstants.TIP);
		doctorTest.setId(DoctorConstants.DOCTOR_ID);
		doctorTest.setUser(user1);
		checkupTest.setScheduled(CheckupConstants.CHECKUP_SCHEDULED);
		checkupTest.setDate(CheckupConstants.CHECKUP_DATE);
		checkupTest.setTime(CheckupConstants.CHECKUP_TIME);
		checkupTest.setId(CheckupConstants.CHECKUP_ID);
		checkupTest.setDoctors(new HashSet<MedicalWorker>());
		checkupTest.getDoctors().add(doctorTest);
		Room testRoom = new Room();
		testRoom.setName(RoomConstants.ROOM_NAME);
		testRoom.setNumber(RoomConstants.ROOM_NUMBER);
		testRoom.setTypeRoom(RoomConstants.ROOM_TYPE);
		testRoom.setId(RoomConstants.ROOM_ID);
		Clinic clinic1 = new Clinic(ClinicConstants.ID_C_1, ClinicConstants.NAZIV_1, ClinicConstants.GRAD_1,
				ClinicConstants.DRZAVA_1, ClinicConstants.ADRESA_1, ClinicConstants.RAITING_1, ClinicConstants.OPIS_1);
		testRoom.setClinic(clinic1);
		checkupTest.setRoom(testRoom);
		checkupTest.setClinic(clinic1);
		assertDoesNotThrow(() -> emailService.quickAppConfirmationEmail(UserConstants.EMAIL_1, checkupTest));
	}

	@Test
	public void testQuickAppConfirmationEmailClinicNull()
			throws InterruptedException, MessagingException, NullPointerException {
		Checkup checkupTest = new Checkup();
		MedicalWorker doctorTest = new MedicalWorker();
		User user1 = new User();
		user1.setName(UserConstants.IME_1);
		user1.setSurname(UserConstants.PREZIME_1);
		user1.setType(UserConstants.TIP);
		doctorTest.setId(DoctorConstants.DOCTOR_ID);
		doctorTest.setUser(user1);
		checkupTest.setScheduled(CheckupConstants.CHECKUP_SCHEDULED);
		checkupTest.setDate(CheckupConstants.CHECKUP_DATE);
		checkupTest.setTime(CheckupConstants.CHECKUP_TIME);
		checkupTest.setId(CheckupConstants.CHECKUP_ID);
		checkupTest.setDoctors(new HashSet<MedicalWorker>());
		checkupTest.getDoctors().add(doctorTest);
		Room testRoom = new Room();
		testRoom.setName(RoomConstants.ROOM_NAME);
		testRoom.setNumber(RoomConstants.ROOM_NUMBER);
		testRoom.setTypeRoom(RoomConstants.ROOM_TYPE);
		testRoom.setId(RoomConstants.ROOM_ID);
		checkupTest.setRoom(testRoom);
		assertThrows(NullPointerException.class, () -> {
			emailService.quickAppConfirmationEmail(UserConstants.EMAIL_1, checkupTest);
		});
	}

	@Test
	public void testQuickAppConfirmationEmailUserNull()
			throws InterruptedException, MessagingException, NullPointerException {
		Checkup checkupTest = new Checkup();
		checkupTest.setScheduled(CheckupConstants.CHECKUP_SCHEDULED);
		checkupTest.setDate(CheckupConstants.CHECKUP_DATE);
		checkupTest.setTime(CheckupConstants.CHECKUP_TIME);
		checkupTest.setId(CheckupConstants.CHECKUP_ID);
		Room testRoom = new Room();
		testRoom.setName(RoomConstants.ROOM_NAME);
		testRoom.setNumber(RoomConstants.ROOM_NUMBER);
		testRoom.setTypeRoom(RoomConstants.ROOM_TYPE);
		testRoom.setId(RoomConstants.ROOM_ID);
		Clinic clinic1 = new Clinic(ClinicConstants.ID_C_1, ClinicConstants.NAZIV_1, ClinicConstants.GRAD_1,
				ClinicConstants.DRZAVA_1, ClinicConstants.ADRESA_1, ClinicConstants.RAITING_1, ClinicConstants.OPIS_1);
		testRoom.setClinic(clinic1);
		checkupTest.setRoom(testRoom);
		checkupTest.setClinic(clinic1);
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
			emailService.quickAppConfirmationEmail(UserConstants.EMAIL_1, checkupTest);
		});
	}

	@Test
	public void testQuickAppConfirmationCheckupNull()
			throws InterruptedException, MessagingException, NullPointerException {
		Checkup checkupTest = new Checkup();
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
			emailService.quickAppConfirmationEmail(UserConstants.EMAIL_1, checkupTest);
		});
	}
	
	@Test
	public void testQuickAppConfirmationEmailRoomNull() throws InterruptedException, MessagingException, NullPointerException {
		Checkup checkupTest = new Checkup();
		MedicalWorker doctorTest = new MedicalWorker();
		User user1 = new User();
		user1.setName(UserConstants.IME_1);
		user1.setSurname(UserConstants.PREZIME_1);
		user1.setType(UserConstants.TIP);
		doctorTest.setId(DoctorConstants.DOCTOR_ID);
		doctorTest.setUser(user1);
		checkupTest.setScheduled(CheckupConstants.CHECKUP_SCHEDULED);
		checkupTest.setDate(CheckupConstants.CHECKUP_DATE);
		checkupTest.setTime(CheckupConstants.CHECKUP_TIME);
		checkupTest.setId(CheckupConstants.CHECKUP_ID);
		checkupTest.setDoctors(new HashSet<MedicalWorker>());
		checkupTest.getDoctors().add(doctorTest);
		Clinic clinic1 = new Clinic(ClinicConstants.ID_C_1, ClinicConstants.NAZIV_1, ClinicConstants.GRAD_1,
				ClinicConstants.DRZAVA_1, ClinicConstants.ADRESA_1, ClinicConstants.RAITING_1, ClinicConstants.OPIS_1);
		checkupTest.setClinic(clinic1);
		assertThrows(NullPointerException.class, () -> {
			emailService.quickAppConfirmationEmail(UserConstants.EMAIL_1, checkupTest);
		});
	}
	
	@Test
	public void testNotifyDoctorEmail() throws InterruptedException, MessagingException, NullPointerException {
		assertDoesNotThrow(() -> emailService.notifyDoctor(CheckupConstants.CHECKUP_ID));
	}
	
	@Test
	public void testNotifyDoctorNullException() throws InterruptedException, MessagingException, NullPointerException {
		assertThrows(NullPointerException.class, () -> {
			emailService.notifyDoctor(CheckupConstants.CHECKUP_ID_FALSE);
		});

	}

}
