package tim31.pswisa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tim31.pswisa.model.Codebook;

public interface CodebookRepository extends JpaRepository<Codebook, Long> {

	/**
	 * Method for getting the whole codebook 
	 * @return - (List<Codebook>) This method returns all codes from the codebook
	 */
	List<Codebook> findAll();

	/**
	 * Method for getting one code from the codebook
	 * @param code - code of the code that will be returned
	 * @return - (Codebook) This method returns code that is found
	 */
	Codebook findOneByCode(String code);
}
