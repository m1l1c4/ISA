package tim31.pswisa.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import tim31.pswisa.constants.DoctorConstants;
import tim31.pswisa.model.ClinicAdministrator;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.repository.ClinicAdministratorRepository;

public class ClinicAdminServiceTest {
	@Autowired
	private ClinicAdministratorService clinicAdministratorService;
	
	@MockBean
	private ClinicAdministratorRepository cladminRepositoryMocked;
	
	@Test
	public void findAllTest() {
		ArrayList<ClinicAdministrator> admins = new ArrayList<ClinicAdministrator>(1);						
		Mockito.when(cladminRepositoryMocked.findAll()).thenReturn(admins);
		List<ClinicAdministrator> administrators = clinicAdministratorService.findAll();
		assertEquals(1, administrators.size());		
	}
}
