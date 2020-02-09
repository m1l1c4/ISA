package tim31.pswisa.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.nio.charset.Charset;
import java.util.HashSet;

import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
import tim31.pswisa.model.UserTokenState;
import tim31.pswisa.security.auth.JwtAuthenticationRequest;
import tim31.pswisa.service.CheckUpService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class CheckupControllerUnitTest {

	private static final String URL_PREFIX = "/checkup/";

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	private String accessToken;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private CheckUpService checkupServiceMocked;

	@Before
	public void login() {
		ResponseEntity<UserTokenState> responseEntity = restTemplate.postForEntity("/login",
				new JwtAuthenticationRequest("pacijent@gmail.com", "sifra1"), UserTokenState.class);
		accessToken = "Bearer " + responseEntity.getBody().getAccessToken();
	}

	@PostConstruct
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testbookQuickApp() throws Exception {
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
		Mockito.when(checkupServiceMocked.bookQuickApp(CheckupConstants.CHECKUP_ID, UserConstants.USER2_EMAIL))
				.thenReturn(checkupTest);
		mockMvc.perform(post(URL_PREFIX + "bookQuickApp/" + CheckupConstants.CHECKUP_ID)
				.header("Authorization", accessToken).contentType(contentType)).andExpect(status().isOk())
				.andExpect(jsonPath("$").value("Uspesno zakazivanje pregleda"));
		verify(checkupServiceMocked, times(1)).bookQuickApp(CheckupConstants.CHECKUP_ID, UserConstants.USER2_EMAIL);
	}

	@Test
	public void testbookQuickAppFalse() throws Exception {
		Mockito.when(checkupServiceMocked.bookQuickApp(CheckupConstants.CHECKUP_ID_FALSE, UserConstants.USER2_EMAIL))
				.thenReturn(null);
		mockMvc.perform(post(URL_PREFIX + "bookQuickApp/" + CheckupConstants.CHECKUP_ID_FALSE).header("Authorization",
				accessToken)).andExpect(status().isExpectationFailed());
		verify(checkupServiceMocked, times(1)).bookQuickApp(CheckupConstants.CHECKUP_ID_FALSE, UserConstants.USER2_EMAIL);
	}
}
