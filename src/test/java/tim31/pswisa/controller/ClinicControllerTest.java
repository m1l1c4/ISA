package tim31.pswisa.controller;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import javax.annotation.PostConstruct;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import tim31.pswisa.TestUtil;
import tim31.pswisa.constants.CheckupConstants;
import tim31.pswisa.constants.CheckupTypeConstants;
import tim31.pswisa.constants.ClinicConstants;
import tim31.pswisa.constants.DoctorConstants;
import tim31.pswisa.constants.UserConstants;
import tim31.pswisa.model.CheckUpType;
import tim31.pswisa.model.Clinic;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.User;
import tim31.pswisa.model.UserTokenState;
import tim31.pswisa.security.auth.JwtAuthenticationRequest;
import tim31.pswisa.dto.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class ClinicControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	public static final String pre_url = "/clinic/searchClinic";

	@Autowired
	private WebApplicationContext webApplicationContext;

	private String accessToken;

	@Autowired
	TestRestTemplate restTemplate;

	@PostConstruct
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Before
	public void login() {
		ResponseEntity<UserTokenState> responseEntity = restTemplate.postForEntity("/login",
				new JwtAuthenticationRequest("pacijent@gmail.com", "sifra1"), UserTokenState.class);
		accessToken = "Bearer " + responseEntity.getBody().getAccessToken();
	}

	@Test
	public void testSearchClinicsControllerNonClinics() throws Exception {
		String[] params = { CheckupTypeConstants.CHECK_UP_TYPE_NAME_FALSE, CheckupConstants.LOCAL_DATE_1.toString() };

		String jsonString = TestUtil.json(params);

		mockMvc.perform(post(pre_url).contentType(contentType).content(jsonString)).andExpect(status().isOk())
				.andExpect(jsonPath("$.object").doesNotExist());

	}

	@Test
	public void testSearchClinicsControllerOk() throws Exception {

		String[] params = { CheckupTypeConstants.CHECK_UP_TYPE_NAME, CheckupConstants.LOCAL_DATE_1.toString() };

		String jsonString = TestUtil.json(params);

		mockMvc.perform(post(pre_url).contentType(contentType).content(jsonString)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$.[*].id").value(hasItem(ClinicConstants.ID_C_1.intValue())));
	}

	@Test
	public void testFilterClinicsControllerOneClinics() throws Exception {
		Clinic clinic1 = new Clinic(ClinicConstants.ID_C_1, ClinicConstants.NAZIV_1, ClinicConstants.GRAD_1,
				ClinicConstants.DRZAVA_1, ClinicConstants.ADRESA_1, ClinicConstants.RAITING_1, ClinicConstants.OPIS_1);

		Clinic clinic2 = new Clinic(ClinicConstants.ID_C_2, ClinicConstants.NAZIV_2, ClinicConstants.GRAD_2,
				ClinicConstants.DRZAVA_2, ClinicConstants.ADRESA_2, ClinicConstants.RAITING_2, ClinicConstants.OPIS_2);

		List<ClinicDTO> clinics = new ArrayList<ClinicDTO>();
		ClinicDTO clinic11 = new ClinicDTO(clinic1);
		ClinicDTO clinic12 = new ClinicDTO(clinic2);
		clinics.add(clinic11);
		clinics.add(clinic12);
		clinic1.setMedicalStuff(new HashSet<MedicalWorker>());
		clinic2.setMedicalStuff(new HashSet<MedicalWorker>());

		User user1 = new User();
		user1.setName(UserConstants.IME_1);
		user1.setSurname(UserConstants.PREZIME_1);
		user1.setType(UserConstants.TIP);

		User user2 = new User();
		user2.setName(UserConstants.IME_2);
		user2.setSurname(UserConstants.PREZIME_2);
		user2.setType(UserConstants.TIP);

		MedicalWorker mw1 = new MedicalWorker(DoctorConstants.DOCTOR_ID_1, user1, clinic1, DoctorConstants.TIP_D_1);
		clinic1.getMedicalStuff().add(mw1);

		MedicalWorker mw2 = new MedicalWorker(DoctorConstants.DOCTOR_ID_2, user2, clinic2, DoctorConstants.TIP_D_1);
		clinic2.getMedicalStuff().add(mw2);

		CheckUpType srchType = new CheckUpType();
		srchType.setClinics(new HashSet<Clinic>());
		srchType.getClinics().add(clinic1);
		srchType.getClinics().add(clinic2);
		srchType.setName(CheckupTypeConstants.CHECK_UP_TYPE_NAME);
		srchType.setId(CheckupTypeConstants.CHECK_UP_TYPE_ID);
		srchType.setTypePrice(100);

		String jsonString = TestUtil.json(clinics);

		mockMvc.perform(post("/clinic/filterClinic/" + "7").contentType(contentType).content(jsonString))
				.andExpect(status().isOk()).andExpect(jsonPath("$").value(hasSize(1)))
				.andExpect(jsonPath("$.[*].id").value(hasItem(ClinicConstants.ID_C_1.intValue())));

	}

	@Test
	public void testFilterClinicsController() throws Exception {

		Clinic clinic1 = new Clinic(ClinicConstants.ID_C_1, ClinicConstants.NAZIV_1, ClinicConstants.GRAD_1,
				ClinicConstants.DRZAVA_1, ClinicConstants.ADRESA_1, ClinicConstants.RAITING_1, ClinicConstants.OPIS_1);

		Clinic clinic2 = new Clinic(ClinicConstants.ID_C_2, ClinicConstants.NAZIV_2, ClinicConstants.GRAD_2,
				ClinicConstants.DRZAVA_2, ClinicConstants.ADRESA_2, ClinicConstants.RAITING_2, ClinicConstants.OPIS_2);

		List<ClinicDTO> clinics = new ArrayList<ClinicDTO>();
		ClinicDTO clinic11 = new ClinicDTO(clinic1);
		ClinicDTO clinic12 = new ClinicDTO(clinic2);
		clinics.add(clinic11);
		clinics.add(clinic12);
		clinic1.setMedicalStuff(new HashSet<MedicalWorker>());
		clinic2.setMedicalStuff(new HashSet<MedicalWorker>());

		User user1 = new User();
		user1.setName(UserConstants.IME_1);
		user1.setSurname(UserConstants.PREZIME_1);
		user1.setType(UserConstants.TIP);

		User user2 = new User();
		user2.setName(UserConstants.IME_2);
		user2.setSurname(UserConstants.PREZIME_2);
		user2.setType(UserConstants.TIP);

		MedicalWorker mw1 = new MedicalWorker(DoctorConstants.DOCTOR_ID_1, user1, clinic1, DoctorConstants.TIP_D_1);
		clinic1.getMedicalStuff().add(mw1);

		MedicalWorker mw2 = new MedicalWorker(DoctorConstants.DOCTOR_ID_2, user2, clinic2, DoctorConstants.TIP_D_1);
		clinic2.getMedicalStuff().add(mw2);

		CheckUpType srchType = new CheckUpType();
		srchType.setClinics(new HashSet<Clinic>());
		srchType.getClinics().add(clinic1);
		srchType.getClinics().add(clinic2);
		srchType.setName(CheckupTypeConstants.CHECK_UP_TYPE_NAME);
		srchType.setId(CheckupTypeConstants.CHECK_UP_TYPE_ID);
		srchType.setTypePrice(100);

		String jsonString = TestUtil.json(clinics);

		mockMvc.perform(post("/clinic/filterClinic/" + "10").contentType(contentType).content(jsonString))
				.andExpect(status().isOk()).andExpect(jsonPath("$").value(hasSize(1)))
				.andExpect(jsonPath("$.[*].id").value(hasItem(ClinicConstants.ID_C_1.intValue())));

	}

}