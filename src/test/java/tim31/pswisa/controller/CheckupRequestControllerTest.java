package tim31.pswisa.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import tim31.pswisa.constants.CheckupConstants;
import tim31.pswisa.constants.ClinicConstants;
import tim31.pswisa.constants.DoctorConstants;
import tim31.pswisa.dto.CheckUpTypeDTO;
import tim31.pswisa.dto.CheckupDTO;
import tim31.pswisa.dto.ClinicDTO;
import tim31.pswisa.dto.MedicalWorkerDTO;
import tim31.pswisa.model.UserTokenState;
import tim31.pswisa.security.auth.JwtAuthenticationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class CheckupRequestControllerTest {
	private static final String URL_PREFIX = "/checkup/";

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	private String accessToken;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void login() {
		ResponseEntity<UserTokenState> responseEntity = restTemplate.postForEntity("/login",
				new JwtAuthenticationRequest("admin@gmail.com", "sifra1"), UserTokenState.class);
		accessToken = "Bearer " + responseEntity.getBody().getAccessToken();		
		
	}

	@PostConstruct
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test // integracioni
	public void checkupToAdminTest() {
		CheckupDTO inputDto = new CheckupDTO();
		inputDto.setDate(CheckupConstants.CHECKUP_DATER);
		inputDto.setTime(CheckupConstants.CHECKUP_TIME);
		inputDto.setType("PREGLED");
		MedicalWorkerDTO inputMwDto = new MedicalWorkerDTO();
		inputMwDto.setId(DoctorConstants.DOCTOR_ID);
		inputDto.setMedicalWorker(inputMwDto);
		ClinicDTO clinicDto = new ClinicDTO();
		clinicDto.setName(ClinicConstants.NAZIV_1);
		inputDto.setClinic(clinicDto);
		CheckUpTypeDTO chTypeTest = new CheckUpTypeDTO();
		chTypeTest.setName(CheckupConstants.CHECKUP_CHTYPE);
		inputDto.setCheckUpType(chTypeTest);		
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		String body = mapper.valueToTree(inputDto).toString();		
		
		try {
			mockMvc.perform(post(URL_PREFIX + "checkupRequest" ).header("Authorization", accessToken)
					.contentType(contentType)
					.content(body))
					.andExpect(status().isOk())	;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Test // integracioni false
	public void checkupToAdminTestFalse() {
		CheckupDTO inputDto = new CheckupDTO();
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		String body = mapper.valueToTree(inputDto).toString();		
		
		try {
			mockMvc.perform(post(URL_PREFIX + "checkupRequest" ).header("Authorization", accessToken)
					.contentType(contentType)
					.content(body))
					.andExpect(status().isExpectationFailed())	;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	
	
	
	
}
