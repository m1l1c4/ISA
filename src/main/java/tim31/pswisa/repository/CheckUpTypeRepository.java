package tim31.pswisa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tim31.pswisa.model.CheckUpType;

public interface CheckUpTypeRepository extends JpaRepository<CheckUpType, Long> {

	/**
	 * This method servers for getting all type check-ups from database
	 * 
	 * @return - (List<CheckUpType>) This method returns list of type check-ups from
	 *         database
	 */
	List<CheckUpType> findAll();

	/**
	 * This method servers for getting one type of check-up by name
	 * 
	 * @param name - name of type of check-up that has to be returned
	 * @return - (CheckUpType) This method returns searched type of check-up
	 */
	CheckUpType findOneByName(String name);

	/**
	 * This method servers for getting one type of check-up by id
	 * 
	 * @param id - id of type of check-up that has to be returned
	 * @return - (CheckUpType) This method returns searched type of check-up
	 */
	CheckUpType findOneById(Long id);
	
	/**
	 * method for getting all checkup types by clinic id
	 * 
	 * @param id - clinic id
	 * @return List<CheckUpType> - list of all checkup types
	 */
	//List<CheckUpType> findAllByClinicId(Long id);

}
