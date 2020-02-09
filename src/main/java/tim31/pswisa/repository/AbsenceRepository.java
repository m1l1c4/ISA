package tim31.pswisa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tim31.pswisa.model.Absence;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {

	/**
	 * This method servers for getting all absences from database
	 * 
	 * @return - (List<Absence>) This method returns all absences in database
	 */
	List<Absence> findAll();

	/**
	 * This method servers for getting one absence by absence id
	 * 
	 * @param id - absence id
	 * @return - (Absence) This method returns one room
	 */
	Absence findOneById(Long id);

	/**
	 * This method servers for getting all absence in clinic
	 * 
	 * @param id - clinic id
	 * @return - (List<Absence>) This method returns list of absence in one clinic
	 */
	List<Absence> findAllByClinicOfAbsenceId(Long id);

	
	/**
	 * Method for finding all absences and holidays of one medical worker
	 * @param id - id, key of the medical worker
	 * @return - (List<Absence>) This method returns list of absences of one medical worker
	 */
	List<Absence> findAllByMedicalWorkerId(Long id);

}
