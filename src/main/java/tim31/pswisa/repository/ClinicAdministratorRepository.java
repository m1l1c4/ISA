package tim31.pswisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tim31.pswisa.model.ClinicAdministrator;

public interface ClinicAdministratorRepository extends JpaRepository<ClinicAdministrator, Long> {

	/**
	 * This method returns clinic administrator by user id
	 * 
	 * @param id - id of user
	 * @return - (ClinicAdministrator) This method returns clinic administrator by sent id of user
	 * 
	 */
	ClinicAdministrator findOneByUserId(Long id);

	/**
	 * This method returns clinic administrator by id
	 * 
	 * @param id - id of clinic administrator
	 * @return - (ClinicAdministrator) This method returns clinic administrator by sent id
	 * 
	 */
	ClinicAdministrator findOneById(Long id);
}
