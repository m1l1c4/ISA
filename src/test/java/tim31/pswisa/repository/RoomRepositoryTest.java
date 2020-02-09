package tim31.pswisa.repository;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import tim31.pswisa.constants.ClinicConstants;
import tim31.pswisa.constants.RoomConstants;
import tim31.pswisa.model.Clinic;
import tim31.pswisa.model.Room;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class RoomRepositoryTest {
	@Autowired
	RoomRepository roomRepository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	@Transactional
	public void testFindOneById() {
		Room testRoom = new Room();		
		Clinic clinicTest = new Clinic();
		clinicTest.setName(ClinicConstants.CLINIC_NAME);
		clinicTest.setCity(ClinicConstants.CLINIC_CITY);
		clinicTest.setAddress(ClinicConstants.CLINIC_ADRESS);
		clinicTest.setDescription(ClinicConstants.CLINIC_DESCRIPTION);		
		testRoom.setName(RoomConstants.ROOM_NAME);
		testRoom.setNumber(RoomConstants.ROOM_NUMBER);
		testRoom.setClinic(clinicTest);
		clinicTest = entityManager.persistAndFlush(clinicTest);
		testRoom = entityManager.persistAndFlush(testRoom);
		Room room = roomRepository.findOneById(testRoom.getId());
		assertEquals(testRoom.getId(), room.getId());
	}
	
	
}
