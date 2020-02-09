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

import tim31.pswisa.constants.RoomConstants;
import tim31.pswisa.model.Room;
import tim31.pswisa.repository.RoomRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class RoomServiceTest {
	@MockBean
	private RoomRepository roomRepositoryMocked;
	
	@Autowired
	private RoomService roomService;
	
	@Test
	public void findOneByIdTest() {
		Room roomTest = new Room();
		roomTest.setId(RoomConstants.ROOM_ID);				
		Mockito.when(roomRepositoryMocked.findOneById(roomTest.getId())).thenReturn(roomTest);
		Room room = roomService.findOneById(RoomConstants.ROOM_ID);
		assertEquals(RoomConstants.ROOM_ID, room.getId());		
	}
}
