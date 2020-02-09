package tim31.pswisa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tim31.pswisa.model.Clinic;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {

	/**
	 * This method returns all clinics from database
	 * 
	 * @return - (List<Clinic>) This method returns all clinics from database
	 * 
	 */
	List<Clinic> findAll();

	/**
	 * This method returns clinic name of clinic
	 * 
	 * @param clinic - name of clinic
	 * @return - (Clinic) This method returns clinic name of clinic
	 * 
	 */
	Clinic findOneByName(String clinic);

	/**
	 * This method returns clinic id
	 * 
	 * @param id - id of clinic
	 * @return - (Clinic) This method returns clinic clinic id
	 * 
	 */
	Clinic findOneById(Long id);

}
