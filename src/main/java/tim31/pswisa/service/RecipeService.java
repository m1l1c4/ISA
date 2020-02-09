package tim31.pswisa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tim31.pswisa.dto.RecipeDTO;
import tim31.pswisa.model.Checkup;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.Patient;
import tim31.pswisa.model.Recipe;
import tim31.pswisa.model.Report;
import tim31.pswisa.model.User;
import tim31.pswisa.repository.RecipeRepository;

@Service
@Transactional(readOnly = true)
public class RecipeService {

	@Autowired
	private RecipeRepository recipeRepository;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PatientService patientService;
	
	@Autowired
	private CheckUpService checkupService;

	@Transactional(readOnly = false)
	public Recipe save(Recipe r) {
		return recipeRepository.save(r);
	}

	/**
	 * Method for getting one recipe by code
	 * @param code - code of the recipe in the codebook
	 * @return - (Recipe) This method returns found recipe
	 */
	public Recipe findOneByCode(String code) {
		return recipeRepository.findOneByCode(code);
	}

	/**
	 * Method for getting recipes from one clinic
	 * @param verified - informations if the recipe is verified or not
	 * @param nurse - nurse from the clinic which needs to verify recipes
	 * @return - (List<Recipe>) This method returns list of the not verified recipes
	 */
	public List<Recipe> findAllByVerified(Boolean verified, MedicalWorker nurse) {
		List<Recipe> ret = new ArrayList<>();
		List<Recipe> recipes = recipeRepository.findAllByVerified(verified);
		for(Recipe r: recipes) {
			if(r.getDoctor().getClinic().getId() == nurse.getClinic().getId()) {
				ret.add(r);
			}
		}
		return ret;
	}

	/**
	 * Method for getting one recipe by id
	 * @param id - id of the recipe in the database
	 * @return - (Recipe) This method returns found recipe
	 */
	public Recipe findOneById(Long id) {
		return recipeRepository.findOneById(id);
	}

	/**
	 * Method for verifying recipe
	 * @param recipe - recipe that will be verified
	 * @param nurse - nurse that wants to verify recipe
	 * @return - (Recipe) This method returns recipe after verifying
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Recipe verify(Recipe recipe, MedicalWorker nurse) throws Exception{
		recipe.setVerified(true);
		recipe.setNurse(nurse);
		return recipeRepository.save(recipe);
	}
	
	public List<Recipe> findAllByVerifiedAndNurseId(boolean verified, Long nurseId){
		return recipeRepository.findAllByVerifiedAndNurseId(verified, nurseId);
	}

	@Transactional(readOnly = false)
	public RecipeDTO additionalCheckupInfo(Long id) {		
		Checkup ch = checkupService.findOneById(id);
		if (ch.getReport() == null) {
			return null;
		}
		Recipe found = findOneByReportId(ch.getReport().getId());
		return new RecipeDTO(found);		
	}
	
	public Recipe findOneByReportId(Long id) {
		return recipeRepository.findOneByReportId(id);
	}

}
