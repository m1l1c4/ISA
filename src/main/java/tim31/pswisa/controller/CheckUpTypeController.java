package tim31.pswisa.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim31.pswisa.dto.CheckUpTypeDTO;
import tim31.pswisa.model.CheckUpType;
import tim31.pswisa.model.ClinicAdministrator;
import tim31.pswisa.model.User;
import tim31.pswisa.security.TokenUtils;
import tim31.pswisa.service.CheckUpTypeService;
import tim31.pswisa.service.ClinicAdministratorService;
import tim31.pswisa.service.UserService;

@RestController
@RequestMapping(value = "/checkUpType")
public class CheckUpTypeController {

	@Autowired
	private ClinicAdministratorService clinicAdministratorService;

	@Autowired
	private UserService userService;

	@Autowired
	private CheckUpTypeService checkUpTypeService;

	@Autowired
	TokenUtils tokenUtils;

	/**
	 * This method servers for deleting check-up type in clinic by clinic
	 * administrator
	 * 
	 * @param name    - the name of check-up type that have to be deleted
	 * @param request - information of logged user
	 * @return - (String) This method returns string "Obrisano" if type is deleted
	 *         or "Greska" can't delete that type
	 */
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	@PostMapping(value = "/deleteType/{name}")
	public ResponseEntity<String> deleteTypeController(@PathVariable String name, HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			String returnVal = checkUpTypeService.deleteType(user, name);
			if (returnVal.equals("Obrisano")) {
				return new ResponseEntity<>("Obrisano", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Greska", HttpStatus.ALREADY_REPORTED);
			}
		}
		return new ResponseEntity<>("Greska", HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method servers getting all types of check-ups in clinic
	 * 
	 * @param request - information of logged user
	 * @return - (ArrayList<CheckUpTypeDTO>) This method returns list of all
	 *         check-ups type in clinic if user is not null
	 */
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR', 'ROLE_PACIJENT', 'ROLE_CCADMIN', 'ROLE_STAFF')")
	@GetMapping(value = "/getTypes", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<CheckUpTypeDTO>> getTypes(HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			ArrayList<CheckUpTypeDTO> retValue = new ArrayList<CheckUpTypeDTO>();
			retValue = checkUpTypeService.getAllTypes(user);
			return new ResponseEntity<>(retValue, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
	}

	/**
	 * This method servers for adding new appointment for booking with one click
	 * 
	 * @param type    - check-up type that have to be added
	 * @param request - information of logged user
	 * @return - (CheckUpTypeDTO) This method returns added check-up type in clinic
	 * 
	 */
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	@PostMapping(value = "/addType", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CheckUpTypeDTO> addTypeController(@RequestBody CheckUpTypeDTO type,
			HttpServletRequest request) {

		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);

		// save types in clinic
		if (user != null) {
			ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
			if (clinicAdministrator != null) {
				CheckUpType temp = new CheckUpType();
				temp = checkUpTypeService.addType(type, clinicAdministrator);
				if (temp == null) {
					return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
				} else {
					return new ResponseEntity<>(new CheckUpTypeDTO(temp), HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping(value = "/allTypes", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CheckUpTypeDTO>> allTypes() {
		List<CheckUpTypeDTO> chTypes = checkUpTypeService.findAllOptimised();
		if (chTypes != null) {
			return new ResponseEntity<>(chTypes, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Method used for getting all checkup types available in one clinic
	 * 
	 * @param Long id - clinic id
	 * @return List<CheckUpTypeDTO> - list of all available checkup types
	 */
	@GetMapping(value = "/allTypesOneClinic/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CheckUpTypeDTO>> allTypesInClinic(@PathVariable Long id) {
		List<CheckUpTypeDTO> chTypes = checkUpTypeService.findAllByClinicId(id);
		if (chTypes.size() > 0) {
			return new ResponseEntity<>(chTypes, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
