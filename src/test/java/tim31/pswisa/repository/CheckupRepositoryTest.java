package tim31.pswisa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tim31.pswisa.constants.CheckupConstants;
import tim31.pswisa.constants.ClinicConstants;
import tim31.pswisa.constants.RoomConstants;
import tim31.pswisa.model.Checkup;
import tim31.pswisa.model.Clinic;
import tim31.pswisa.model.Room;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class CheckupRepositoryTest {

	@Autowired
	CheckUpRepository checkupRepository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void testFindOneById() {
		Checkup checkupTest = new Checkup();
		checkupTest.setScheduled(CheckupConstants.CHECKUP_SCHEDULED);		
		checkupTest = entityManager.persist(checkupTest);
		Checkup checkup = checkupRepository.findOneById(checkupTest.getId());
		assertNotNull(checkup);
		assertEquals(checkup.getId(), checkupTest.getId());
	}

	@Test
	public void testSave() {
		Checkup checkupTest = new Checkup();
		checkupTest = entityManager.persistAndFlush(checkupTest);
		Checkup checkup = checkupRepository.save(checkupTest);
		assertEquals(checkupTest.getId(), checkup.getId());
	}
	
	@Test
	public void testfindAllByRoomIdAndTimeAndDate() {
		Room testRoom = new Room();		
		Checkup checkupTest = new Checkup();
		Clinic clinicTest = new Clinic();
		clinicTest.setName(ClinicConstants.CLINIC_NAME);
		clinicTest.setCity(ClinicConstants.CLINIC_CITY);
		clinicTest.setAddress(ClinicConstants.CLINIC_ADRESS);
		clinicTest.setDescription(ClinicConstants.CLINIC_DESCRIPTION);
		clinicTest = entityManager.persistAndFlush(clinicTest);
		testRoom.setName(RoomConstants.ROOM_NAME);
		testRoom.setNumber(RoomConstants.ROOM_NUMBER);
		testRoom.setClinic(clinicTest);
		checkupTest.setDate(CheckupConstants.CHECKUP_DATE);
		checkupTest.setTime(CheckupConstants.CHECKUP_TIME);
		checkupTest.setRoom(testRoom);
		testRoom = entityManager.persistAndFlush(testRoom);			
		checkupTest = entityManager.persistAndFlush(checkupTest);
		
		List<Checkup> checkups = checkupRepository.findAllByRoomIdAndTimeAndDate(checkupTest.getRoom().getId(),
				checkupTest.getTime(), checkupTest.getDate());
		assertThat(checkups).hasSize(1);
	}

}
