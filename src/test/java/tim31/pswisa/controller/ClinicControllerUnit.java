package tim31.pswisa.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
import tim31.pswisa.dto.ClinicDTO;
import tim31.pswisa.model.CheckUpType;
import tim31.pswisa.model.Clinic;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.User;
import tim31.pswisa.model.UserTokenState;
import tim31.pswisa.security.auth.JwtAuthenticationRequest;
import tim31.pswisa.service.ClinicService;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class ClinicControllerUnit {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	public static final String pre_url = "/clinic/searchClinic";

	@MockBean
	private ClinicService clinicServiceMock;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private TestRestTemplate restTemplate;

	@PostConstruct
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testSearchClinicsControllerNonClinicsUnit() throws Exception {
		String[] params = { CheckupTypeConstants.CHECK_UP_TYPE_NAME_FALSE, CheckupConstants.LOCAL_DATE_1.toString() };
		String jsonString = TestUtil.json(params);

		Mockito.when(clinicServiceMock.searchClinics(params)).thenReturn(null);

		mockMvc.perform(post(pre_url).contentType(contentType).content(jsonString)).andExpect(status().isOk())
				.andExpect(jsonPath("$.object").doesNotExist());

		verify(clinicServiceMock, times(1)).searchClinics(params);

	}

	@Test
	public void testSearchClinicsControllerOkUnit() throws Exception {

		String[] params = { CheckupTypeConstants.CHECK_UP_TYPE_NAME, CheckupConstants.LOCAL_DATE_1.toString() };
		Clinic clinic1 = new Clinic(ClinicConstants.ID_C_1, ClinicConstants.NAZIV_1, ClinicConstants.GRAD_1,
				ClinicConstants.DRZAVA_1, ClinicConstants.ADRESA_1, ClinicConstants.RAITING_1, ClinicConstants.OPIS_1);

		List<ClinicDTO> clinics = new ArrayList<ClinicDTO>();

		clinics.add(new ClinicDTO(clinic1));

		String jsonString = TestUtil.json(params);

		Mockito.when(clinicServiceMock.searchClinics(params)).thenReturn(clinics);

		mockMvc.perform(post(pre_url).contentType(contentType).content(jsonString)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$.[*].id").value(hasItem(ClinicConstants.ID_C_1.intValue())));

		verify(clinicServiceMock, times(1)).searchClinics(params);

	}

	@Test
	public void testFilterControllerClinicsNoneUnit() throws Exception {
		Clinic clinic1 = new Clinic(ClinicConstants.ID_C_1, ClinicConstants.NAZIV_1, ClinicConstants.GRAD_1,
				ClinicConstants.DRZAVA_1, ClinicConstants.ADRESA_1, ClinicConstants.RAITING_1, ClinicConstants.OPIS_1);

		Clinic clinic2 = new Clinic(ClinicConstants.ID_C_2, ClinicConstants.NAZIV_2, ClinicConstants.GRAD_2,
				ClinicConstants.DRZAVA_2, ClinicConstants.ADRESA_2, ClinicConstants.RAITING_2, ClinicConstants.OPIS_2);

		List<Clinic> clinicsFilter = new ArrayList<Clinic>();
		clinicsFilter.add(clinic1);
		clinicsFilter.add(clinic2);

		ArrayList<Clinic> pomList = new ArrayList<Clinic>();
		pomList.add(clinic1);
		pomList.add(clinic2);

		List<Clinic> clinics = new ArrayList<Clinic>();
		clinics.add(clinic1);
		clinics.add(clinic2);
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

		List<Clinic> retValue = new ArrayList<Clinic>();
		retValue.add(clinic1);

		Mockito.when(clinicServiceMock.filterClinics("15", pomList)).thenReturn(retValue);

		String jsonString = TestUtil.json(pomList);

		mockMvc.perform(post("/clinic/filterClinic/" + "15").contentType(contentType).content(jsonString))
				.andExpect(status().isOk()).andExpect(jsonPath("$.object").doesNotExist());

	}

	@Test
	public void testFilterClinicsControllerUnit() throws Exception {

		Clinic clinic1 = new Clinic(ClinicConstants.ID_C_1, ClinicConstants.NAZIV_1, ClinicConstants.GRAD_1,
				ClinicConstants.DRZAVA_1, ClinicConstants.ADRESA_1, ClinicConstants.RAITING_1, ClinicConstants.OPIS_1);

		Clinic clinic2 = new Clinic(ClinicConstants.ID_C_2, ClinicConstants.NAZIV_2, ClinicConstants.GRAD_2,
				ClinicConstants.DRZAVA_2, ClinicConstants.ADRESA_2, ClinicConstants.RAITING_2, ClinicConstants.OPIS_2);

		List<Clinic> clinics = new ArrayList<Clinic>();
		clinics.add(clinic1);
		clinics.add(clinic2);

		List<Clinic> retValue = new ArrayList<Clinic>();

		Mockito.when(clinicServiceMock.filterClinics("greska", (ArrayList<Clinic>) clinics)).thenReturn(retValue);

		String jsonString = TestUtil.json(clinics);
	     		
		mockMvc.perform(post("/clinic/filterClinic/" + "greska").contentType(contentType).content(jsonString))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.object").doesNotExist());

	}

}
