package tim31.pswisa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim31.pswisa.dto.AbsenceDTO;
import tim31.pswisa.dto.CheckupDTO;
import tim31.pswisa.dto.RecipeDTO;
import tim31.pswisa.dto.ReportDTO;
import tim31.pswisa.model.Absence;
import tim31.pswisa.model.Checkup;
import tim31.pswisa.model.ClinicAdministrator;
import tim31.pswisa.model.Codebook;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.Recipe;
import tim31.pswisa.model.Report;
import tim31.pswisa.model.User;
import tim31.pswisa.security.TokenUtils;
import tim31.pswisa.service.AbsenceService;
import tim31.pswisa.service.CheckUpService;
import tim31.pswisa.service.CheckUpTypeService;
import tim31.pswisa.service.ClinicAdministratorService;
import tim31.pswisa.service.CodebookService;
import tim31.pswisa.service.EmailService;
import tim31.pswisa.service.MedicalWorkerService;
import tim31.pswisa.service.RecipeService;
import tim31.pswisa.service.ReportService;
import tim31.pswisa.service.UserService;

@RestController
@RequestMapping(value = "/checkup")
public class CheckupController {

	@Autowired
	private ClinicAdministratorService clinicAdministratorService;

	@Autowired
	private UserService userService;

	@Autowired
	TokenUtils tokenUtils;

	@Autowired
	CheckUpTypeService checkUpTypeService;

	@Autowired
	private CheckUpService checkupService;

	@Autowired
	private MedicalWorkerService medicalWorkerService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private CodebookService codebookService;

	@Autowired
	private AbsenceService absenceService;
	
	@Autowired
	private RecipeService recipeService;
	
	@Autowired
	private EmailService emailService;

	/**
	 * Method for adding report after examination of patient
	 * 
	 * @param r - report that has to be added
	 * @return - This method returns created report
	 */
	@PostMapping(value = "/addReport", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReportDTO> saveReport(@RequestBody ReportDTO r) {
		Report report = reportService.add(r);
		if (report == null) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<>(new ReportDTO(report), HttpStatus.CREATED);
	}

	/**
	 * This method servers for adding new appointment for booking with one click
	 * 
	 * @param c       - check-up that has to be added
	 * @param request - information of logged user
	 * @return - (CheckupDTO) This method returns added appointment if doctor are
	 *         not busy
	 */
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	@PostMapping(value = "/addAppointment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CheckupDTO> addAppointmentController(@RequestBody CheckupDTO c, HttpServletRequest request) {
		User doctorOne = userService.findOneByEmail(c.getMedicalWorker().getUser().getEmail());
		MedicalWorker doctorOne1 = medicalWorkerService.findByUser(doctorOne.getId());
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
			if (clinicAdministrator != null) {
				Checkup check = checkupService.addAppointment(c, doctorOne1, clinicAdministrator);
				if (check == null) {
					return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
				} else {
					// return new ResponseEntity<>(new CheckupDTO(check), HttpStatus.OK);
					return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
				}
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Method for getting one check-up
	 * 
	 * @param id - if of the check-up in database
	 * @return - This method returns found check-in
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<CheckupDTO> getCheckup(@PathVariable Long id) {
		Checkup checkup = checkupService.findOneById(id);
		if (checkup != null) {
			return new ResponseEntity<>(new CheckupDTO(checkup), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Method for creating recipes after examination of the patient
	 * 
	 * @param recipes - codes of the selected codes from the codebook
	 * @return - This method returns message if all recipes are created successfully
	 */
	@PostMapping(value = "/addRecipes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addRecipes(@RequestBody String[] recipes, @PathVariable Long id) {
		Report report = reportService.findOneById(id);
		for (String code : recipes) {
			Codebook codebook = codebookService.findOneByCode(code);
			Recipe r = reportService.addRecipe(report, codebook);
			if (r == null)
				return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
		}
		return new ResponseEntity<>("Uspjesno dodato", HttpStatus.OK);
	}

	@PostMapping(value = "/checkupRequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReportDTO> checkupRequest(@RequestBody CheckupDTO ch, HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		try {
			if (checkupService.checkupToAdmin(ch, email)) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

	}

	/**
	 * Method for changing check-up after finding a room, date and time for the
	 * appointment/operation by clinic administrator
	 * 
	 * @param c - check-up with the id of the check-up that has to be updated and
	 *          new informations about appointment
	 * @return - This method returns updated check-up
	 */
	//@PreAuthorize("hasRole('ADMINISTRATOR')")
	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CheckupDTO> updateCheckup(@RequestBody CheckupDTO c) {
		Checkup checkup;
		try {
			checkup = checkupService.update(c);
			if (checkup != null) {
				emailService.notifyDoctor(checkup.getId());
				return new ResponseEntity<CheckupDTO>(new CheckupDTO(checkup), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}

	/**
	 * Method for adding doctors which clinic administrator has selected to must
	 * attend the operation
	 * 
	 * @param doctors - id's of the doctors which will be assigned to operation
	 * @param id      - id of the check-up in the database
	 * @return - This method returns message about success of adding doctors to
	 *         check-up
	 */
	@PostMapping(value = "/addDoctors/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addDoctors(@RequestBody Long[] doctors, @PathVariable Long id) {
		Checkup checkup;
		try {
			checkup = checkupService.addDoctors(id, doctors);
			if (checkup != null) {
				return new ResponseEntity<>("Uspjesno dodato", HttpStatus.OK);
			}
			return new ResponseEntity<String>("Doslo je do greske", HttpStatus.EXPECTATION_FAILED);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<String>("Doslo je do greske", HttpStatus.EXPECTATION_FAILED);
		}
	}

	/**
	 * Method for getting all check-ups of the logged in user
	 * 
	 * @param request - request for getting logged in user
	 * @param id      - id of the room if logged in user is administrator of clinic
	 * @return - This method returns all check-ups of the logged in user
	 */
	@GetMapping(value = "/getCheckups/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CheckupDTO>> getCheckups(HttpServletRequest request, @PathVariable Long id) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		Set<Checkup> checkups = checkupService.getAllCheckups(user, id);
		List<CheckupDTO> ret = new ArrayList<>();
		for (Checkup c : checkups) {
			ret.add(new CheckupDTO(c));
		}
		return new ResponseEntity<List<CheckupDTO>>(ret, HttpStatus.OK);
	}

	/**
	 * Method for getting all absences of one medical worker
	 * 
	 * @param id - id of the medical worker in the database
	 * @return - This method returns all absences and holidays of the one medical
	 *         worker
	 */
	@PostMapping(value = "/getVacations/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AbsenceDTO>> getVacations(@PathVariable Long id) {
		List<Absence> absences = absenceService.findAllByMedicalWorkerId(id);
		List<AbsenceDTO> ret = new ArrayList<>();
		for (Absence absence : absences) {
			ret.add(new AbsenceDTO(absence));
		}
		return new ResponseEntity<List<AbsenceDTO>>(ret, HttpStatus.OK);
	}

	@PostMapping(value = "/getAllQuickApp/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CheckupDTO>> getAllQuickApp(@PathVariable Long id) {
		List<CheckupDTO> checkups = checkupService.getAllQuicks(id);
		if (checkups != null) {
			return new ResponseEntity<>(checkups, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	}

	/**
	 * method for booking predefined checkup
	 * 
	 * @param id - key for finding available checkup
	 * @return string - message for successful / unsuccessful booking
	 */
	// @PreAuthorize("hasRole('PACIJENT')")
	@PostMapping(value = "/bookQuickApp/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> bookQuickApp(@PathVariable Long id, HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		Checkup success;
		try {
			success = checkupService.bookQuickApp(id, email);
			try {
				emailService.quickAppConfirmationEmail(email, success);
			} catch (MailException e) {				
				e.printStackTrace();
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
			if (success != null) {
				return new ResponseEntity<>("Uspesno zakazivanje pregleda", HttpStatus.OK);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	}

	/**
	 * find 2 lists for patient checkups, one for history and one for incoming
	 * checkups
	 * 
	 * @param request - HttpServerRequest request - used for finding logged user
	 * @return
	 */

	@PreAuthorize("hasRole('ROLE_PACIJENT') or hasRole('ROLE_DOKTOR')")
	@PostMapping(value = "/patientHistory/{type}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<Integer, List<CheckupDTO>>> getPatientCheckups(@PathVariable String type,@PathVariable Long id, HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		HashMap<Integer, List<CheckupDTO>> patientCheckups = checkupService.getPatientCheckups(id, email, type);
		if (patientCheckups != null) {
			return new ResponseEntity<>(patientCheckups, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	}

	@PostMapping(value = "/scheduleCheckup/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> scheduleCheckup(@PathVariable Long id) {
		boolean success = checkupService.scheduleCheckup(id);
		if (success) {
			return new ResponseEntity<>("confirmed", HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	}

	@PostMapping(value = "/cancelCheckup/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> cancelCheckup(@PathVariable Long id) {
		boolean success = checkupService.cancelCheckup(id);
		if (success) {
			return new ResponseEntity<>("confirmed", HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	}

	/**
	 * method for getting scheduled check-up for today for logged doctor and
	 * selected patient
	 * 
	 * @param id - key of the user of patient
	 * @return CheckupDTO - found check-up if exist, else null
	 */
	@GetMapping(value = "/getCheckup/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CheckupDTO> getCheckup(HttpServletRequest request, @PathVariable Long id) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		CheckupDTO checkup = checkupService.findCheckup(email, id);
		if (checkup == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<CheckupDTO>(checkup, HttpStatus.OK);
	}
	
	/**
	 * returns checkup report needed for presenting additional info of checkup
	 * 
	 * @param request
	 * @param id - checkup id
	 * @return
	 */
	@PreAuthorize("hasRole('PACIJENT')")
	@PostMapping(value = "/infoReport/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RecipeDTO> checkupDetails(HttpServletRequest request, @PathVariable Long id) {		
		RecipeDTO report = recipeService.additionalCheckupInfo(id);
		if (report == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RecipeDTO>(report, HttpStatus.OK);
	}

	@PostMapping(value = "/updateReport", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReportDTO> updateReport(HttpServletRequest request, @RequestBody ReportDTO r) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		ReportDTO report = reportService.update(email, r);
		if (report != null) {
			return new ResponseEntity<ReportDTO>(report, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

}
