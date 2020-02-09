package tim31.pswisa.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tim31.pswisa.dto.AbsenceDTO;
import tim31.pswisa.dto.CheckupDTO;
import tim31.pswisa.dto.MedicalWorkerDTO;
import tim31.pswisa.dto.RecipeDTO;
import tim31.pswisa.dto.UserDTO;
import tim31.pswisa.model.Absence;
import tim31.pswisa.model.Clinic;
import tim31.pswisa.model.ClinicAdministrator;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.Recipe;
import tim31.pswisa.model.User;
import tim31.pswisa.security.TokenUtils;
import tim31.pswisa.service.AbsenceService;
import tim31.pswisa.service.ClinicAdministratorService;
import tim31.pswisa.service.MedicalWorkerService;
import tim31.pswisa.service.RecipeService;
import tim31.pswisa.service.UserService;

@RestController
public class MedicalWorkerController {

	@Autowired
	private MedicalWorkerService medicalWorkerService;

	@Autowired
	private UserService userService;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private ClinicAdministratorService clinicAdministratorService;

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private AbsenceService absenceService;

	/**
	 * This method servers for getting medical worker for update
	 * 
	 * @param request - information of logged user
	 * @return - (MedicalWorkerDTO) This method returns updated medical worker
	 */
	@GetMapping(value = "/getMedicalWorker", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MedicalWorkerDTO> getMedicalWorker(HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			MedicalWorker medicalWorker = medicalWorkerService.findByUser(user.getId());
			if (medicalWorker != null) {
				return new ResponseEntity<>(new MedicalWorkerDTO(medicalWorker), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method servers for getting answer can doctor access to medical record of
	 * patient
	 * 
	 * @param request - information of logged user
	 * @param pom     - email of patient
	 * @return - (String) This method returns string 'da' or 'ne' depending of
	 *         conditions
	 */
	@GetMapping(value = "/canAccessToMedicalRecord/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> canAccessController(HttpServletRequest request, @PathVariable Long id) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			String isOk = medicalWorkerService.canAccess(user, id);
			return new ResponseEntity<>(isOk, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * This method servers for implement booking for patient by doctor patient
	 * 
	 * @param c       - check-up of patient
	 * @param request - information of logged user
	 * @return - (String) This method returns string ok when booking is finished
	 */
	@PostMapping(value = "/bookForPatient", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> bookForPatientController(@RequestBody CheckupDTO c, HttpServletRequest request)
			throws MailException, InterruptedException {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		boolean ok = medicalWorkerService.bookForPatient(user, c);
		if(ok) {
			return new ResponseEntity<>("ok", HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>("greska", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method servers for deleting doctor in clinic by administrator
	 * 
	 * @param user    - doctor that have to be deleted
	 * @param request - information of logged user
	 * @return - (String) This method returns string after deleting, Obrisano or
	 *         Greska
	 */
	@PostMapping(value = "/deleteDoctor")
	public ResponseEntity<String> deleteDoctorController(@RequestBody UserDTO userDTO, HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
			if (clinicAdministrator != null) {
				String returnVal = medicalWorkerService.deleteDoctor(userDTO.getEmail(), clinicAdministrator);
				if (returnVal.equals("Obrisano")) {
					return new ResponseEntity<>("Obrisano", HttpStatus.OK);
				} else {
					return new ResponseEntity<>("Greska", HttpStatus.ALREADY_REPORTED);
				}
			}
		}
		return new ResponseEntity<>("Greska", HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method servers for finding doctor by criteria
	 * 
	 * @param params  - criteria for finding doctors in clinic
	 * @param request - information of logged user
	 * @return - (List<MedicalWorkerDTO>) This method returns list of found doctors
	 *         in clinic
	 */
	@PostMapping(value = "/findDoctors", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MedicalWorkerDTO>> findDoctorsController(@RequestBody String[] params,
			HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
			if (clinicAdministrator != null) {
				Clinic clinic = clinicAdministrator.getClinic();
				if (clinic != null) {
					if (medicalWorkerService.findDoctors(clinic, params[0], params[1]) == null) {
						return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
					} else {
						return new ResponseEntity<>(medicalWorkerService.findDoctors(clinic, params[0], params[1]),
								HttpStatus.OK);
					}
				}
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
	}

	/**
	 * This method servers for updating medical worker
	 * 
	 * @param mw      - new information about medical worker
	 * @param request - information of logged user
	 * @return - (MedicalWorkerDTO) This method returns updated medical worker
	 */
	@PostMapping(value = "/updateMedicalWorker", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MedicalWorkerDTO> updateMedicalWorkerController(@RequestBody MedicalWorkerDTO mw) {
		User user = userService.findOneByEmail(mw.getUser().getEmail());
		if (user != null) {
			MedicalWorker medWorker = medicalWorkerService.findByUser(user.getId());
			if (medWorker != null) {
				medWorker = medicalWorkerService.updateMedicalWorker(medWorker, mw);
				return new ResponseEntity<>(new MedicalWorkerDTO(medWorker), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	/**
	 * Method for adding new medical worker to the clinic
	 * 
	 * @param mw - informations of new medical worker
	 * @return - (MedicalWorkerDTO) This method returns created medical worker
	 */
	@PostMapping(value = "/addMedicalWorker", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MedicalWorkerDTO> addMedicalWorker(@RequestBody MedicalWorkerDTO mw) {
		MedicalWorker medicalWorker = medicalWorkerService.save(mw);
		if (medicalWorker != null) {
			return new ResponseEntity<>(new MedicalWorkerDTO(medicalWorker), HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	/**
	 * Method for getting recipes from one clinic that are not verified
	 * 
	 * @param request - informations of the logged user
	 * @return - (List<RecipeDTO>) This method returns list of the not verified
	 *         recipes
	 */
	@GetMapping(value = "/getRecipes", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RecipeDTO>> getRecipes(HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		MedicalWorker nurse = medicalWorkerService.findByUser(user.getId());
		List<Recipe> recipes = recipeService.findAllByVerified(false, nurse);
		List<RecipeDTO> ret = new ArrayList<RecipeDTO>();
		for (Recipe recipe : recipes) {
			ret.add(new RecipeDTO(recipe));
		}
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	/*
	 * method for searching doctors by given parameters input - String array, sent
	 * parameters from client request output - List<MedicalWorkerDTO> , list of
	 * doctors that meet searching params
	 */
	@PostMapping(value = "/searchDoctors", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MedicalWorkerDTO>> searchDoctors(@RequestBody String[] params) {
		List<MedicalWorkerDTO> result = medicalWorkerService.searchDoctors(params);
		if (result == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	/**
	 * Method for verifying recipe
	 * 
	 * @param request - informations of the logged user
	 * @param id      - id of the non verified recipe
	 * @return - (RecipeDTO) This method returns recipe after verifying
	 */
	@PostMapping(value = "/verifyRecipe/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RecipeDTO> verifyRecipe(@PathVariable Long id, HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		MedicalWorker nurse = medicalWorkerService.findByUser(user.getId());
		Recipe recipe = recipeService.findOneById(id);
		try {
			recipe = recipeService.verify(recipe, nurse);
			if (recipe != null) {
				return new ResponseEntity<>(new RecipeDTO(recipe), HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
		}
	}

	/**
	 * This method servers for getting all doctors by criteria
	 * 
	 * @param request -
	 * @return - (List<MedicalWorkerDTO>) This method returns list of all doctors in
	 *         clinic
	 */
	@GetMapping(value = "/getAllDoctors", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MedicalWorkerDTO>> getAllDoctors(HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		System.out.println(user.getName());
		System.out.println(email);
		if (user != null) {
			ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
			if (clinicAdministrator != null) {
				Clinic clinic = clinicAdministrator.getClinic();
				if (clinic != null) {
					if (medicalWorkerService.getDoctors(clinic) != null) {
						return new ResponseEntity<>(medicalWorkerService.getDoctors(clinic), HttpStatus.OK);
					}
				}
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
	}

	/**
	 * Method for getting all available doctors for the specified date and time
	 * 
	 * @param id   - id of the clinic in database
	 * @param date - date when doctor needs to be available
	 * @param time - time in the date when doctor needs to be available
	 * @return - (List<MedicalWorkerDTO>) This method returns list of the medical
	 *         workers that are available for the specified date and time
	 */
	@GetMapping(value = "/getAllAvailable/{id}/{date}/{time}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MedicalWorkerDTO>> getAllAvailableDoctors(@PathVariable Long id,
			@PathVariable String date, @PathVariable String time) {
		List<MedicalWorker> doctors = medicalWorkerService.findAllAvailable(id, date, time);
		List<MedicalWorkerDTO> ret = new ArrayList<>();
		for (MedicalWorker mw : doctors) {
			ret.add(new MedicalWorkerDTO(mw));
		}
		return new ResponseEntity<List<MedicalWorkerDTO>>(ret, HttpStatus.OK);
	}

	/**
	 * Method for creating request for absence/holiday
	 * @param absence - information of the absence that will be created
	 * @param request - information of the logged in user that wants to send request for absence
	 * @return - (AbsenceDTO) This method returns created absence if successful, or null if fails
	 */
	@PostMapping(value = "/vacationRequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AbsenceDTO> vacationRequest(@RequestBody AbsenceDTO absence, HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		MedicalWorker mw = medicalWorkerService.findByUser(user.getId());
		Absence a = absenceService.create(absence, mw);
		if (a != null) {
			return new ResponseEntity<>(new AbsenceDTO(a), HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}
	
	/**
	 * method for rating doctor who examined logged patient
	 * @param request
	 * @param param - param[0] is checkup id , param[1] is given rating
	 * @return
	 */
	@PostMapping(value = "/rateMedicalWorker", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> rateMedicalWorker(HttpServletRequest request, @RequestBody String[] param) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		boolean ok = medicalWorkerService.rateDoctor(email, param);
		if (ok) {
			return new ResponseEntity<>("uspesno ocenjen", HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

}
