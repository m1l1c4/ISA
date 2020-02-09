package tim31.pswisa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import tim31.pswisa.model.Patient;

@Service
public interface PatientRepository extends JpaRepository<Patient, Long> {

	Patient findByUserId(Long id);

	Patient findOneById(Long id);

	List<Patient> findAll();

	/**
	 * Method for getting all requests for registration
	 * @param processed - parameter that shows whether the request is processed or not
	 * @return - (List<Patient>) This method returns all patients that have been sent request for the registration
	 */
	List<Patient> findAllByProcessed(boolean processed);
}
