package tim31.pswisa.repository;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tim31.pswisa.model.ClinicAdministrator;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class ClAdminRepositoryTest {
	@Autowired
	ClinicAdministratorRepository cladminRepository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void testFindAll() {
		List<ClinicAdministrator> clinicalAdministrators = cladminRepository.findAll();
		assertEquals(1, clinicalAdministrators.size());	
	}
}
