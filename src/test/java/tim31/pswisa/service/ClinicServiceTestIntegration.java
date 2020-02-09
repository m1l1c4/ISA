package tim31.pswisa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

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
import tim31.pswisa.service.ClinicService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class ClinicServiceTestIntegration {

	@Autowired
	private ClinicService clinicService;

	@Test
	public void testFilterClinicsOk() {
		Clinic clinic1 = new Clinic(ClinicConstants.ID_C_1, ClinicConstants.NAZIV_1, ClinicConstants.GRAD_1,
				ClinicConstants.DRZAVA_1, ClinicConstants.ADRESA_1, ClinicConstants.RAITING_1, ClinicConstants.OPIS_1);

		Clinic clinic2 = new Clinic(ClinicConstants.ID_C_2, ClinicConstants.NAZIV_2, ClinicConstants.GRAD_2,
				ClinicConstants.DRZAVA_2, ClinicConstants.ADRESA_2, ClinicConstants.RAITING_2, ClinicConstants.OPIS_2);

		String raiting = ClinicConstants.RAITING_OK_1;
		String raiting2 = ClinicConstants.RAITING_OK_2;
		String raiting3 = ClinicConstants.RAITING_OK_3;
		String raiting4 = ClinicConstants.RAITING_OK_4;
		ArrayList<Clinic> clinics = new ArrayList<Clinic>();
		clinics.add(clinic1);
		clinics.add(clinic2);
		assertEquals(1, clinicService.filterClinics(raiting, clinics).size());
		assertEquals(2, clinicService.filterClinics(raiting2, clinics).size());
		assertEquals(1, clinicService.filterClinics(raiting3, clinics).size());
		assertEquals(0, clinicService.filterClinics(raiting4, clinics).size());

	}

	@Test
	public void testSearchClinicsFalseType() {
		String[] params = { CheckupTypeConstants.CHECK_UP_TYPE_NAME_FALSE, DoctorConstants.DATE_OK };
		List<ClinicDTO> rezultat = clinicService.searchClinics(params);
		assertNull(rezultat);
	}

	@Test
	public void testSearchClinicsFalseNull() {
		String[] params = { "NE POSTOJI", DoctorConstants.DATE_OK };
		List<ClinicDTO> rezultat = clinicService.searchClinics(params);
		assertNull(rezultat);
	}

	@Test
	public void testSearchClinicsFalseDate() {
		String[] params = { CheckupTypeConstants.CHECK_UP_TYPE_NAME, CheckupTypeConstants.CHECK_UP_TYPE_NAME_FALSE };
		List<ClinicDTO> rezultat = clinicService.searchClinics(params);
		assertNull(rezultat);
	}

	@Test
	public void testSearchClinics() {

		String[] params1 = { CheckupTypeConstants.CHECK_UP_TYPE_NAME, CheckupConstants.LOCAL_DATE_1.toString() };
		List<ClinicDTO> rezultat = clinicService.searchClinics(params1);

		assertEquals(1, rezultat.size());

	}
}
