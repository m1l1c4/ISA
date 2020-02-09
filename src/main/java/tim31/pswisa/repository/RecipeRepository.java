package tim31.pswisa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tim31.pswisa.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

	/**
	 * Method for getting one recipe by code
	 * @param code - code of the recipe in the codebook
	 * @return - (Recipe) This method returns found recipe
	 */
	Recipe findOneByCode(String code);

	/**
	 * Method for getting recipes from one clinic
	 * @param verified - informations if the recipe is verified or not
	 * @return - (List<Recipe>) This method returns list of the not verified recipes
	 */
	List<Recipe> findAllByVerified(Boolean verified);

	/**
	 * Method for getting one recipe by id
	 * @param id - id of the recipe in the database
	 * @return - (Recipe) This method returns found recipe
	 */
	Recipe findOneById(Long id);
	
	List<Recipe> findAllByVerifiedAndNurseId(Boolean verified, Long nurseId);
	
	Recipe findOneByReportId(Long id);

}
