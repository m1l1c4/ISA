package tim31.pswisa.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim31.pswisa.dto.CheckUpTypeDTO;
import tim31.pswisa.dto.ClinicDTO;
import tim31.pswisa.dto.MedicalWorkerDTO;
import tim31.pswisa.dto.RoomDTO;
import tim31.pswisa.model.CheckUpType;
import tim31.pswisa.model.Clinic;
import tim31.pswisa.model.ClinicAdministrator;
import tim31.pswisa.model.Room;
import tim31.pswisa.model.User;
import tim31.pswisa.security.TokenUtils;
import tim31.pswisa.service.CheckUpTypeService;
import tim31.pswisa.service.ClinicAdministratorService;
import tim31.pswisa.service.ClinicService;
import tim31.pswisa.service.RoomService;
import tim31.pswisa.service.UserService;

@RestController
@RequestMapping(value = "/clinic")
public class ClinicController {

	@Autowired
	private ClinicService clinicService;

	@Autowired
	private ClinicAdministratorService clinicAdministratorService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoomService roomService;

	@Autowired
	TokenUtils tokenUtils;

	@Autowired
	private CheckUpTypeService checkUpTypeService;

	/**
	 * This method servers for updating clinic by administrator
	 * 
	 * @param clinic  - new information about clinic that have to be updated
	 * @param request - information of logged user
	 * @return - (ClinicDTO) This method returns updated clinic
	 * 
	 */
	@PostMapping(value = "/updateClinic", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClinicDTO> upadateClinicController(@RequestBody ClinicDTO clinic,
			HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);

		if (user != null) {
			ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
			if (clinicAdministrator != null) {
				Clinic temp;
				try {
					temp = clinicService.updateClinic(clinicAdministrator, clinic);
					if (temp != null) {
						return new ResponseEntity<>(new ClinicDTO(temp), HttpStatus.OK);
					} else {
						return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

			} else
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	@GetMapping(value = "/getClinics", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ClinicDTO>> getAllClinics() {
		List<Clinic> clinics = clinicService.findAll();
		List<ClinicDTO> retDto = new ArrayList<ClinicDTO>();

		if (clinics == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			for (Clinic clinic : clinics) {
				ClinicDTO cldto = new ClinicDTO(clinic);

				retDto.add(cldto);
			}

			return new ResponseEntity<>(retDto, HttpStatus.OK);
		}
	}

	/**
	 * This method servers for updating type of check-up
	 * 
	 * @param params  - new information about type that have to be changed
	 * @param request - information of logged user
	 * @return - (CheckUpTypeDTO) This method returns updated check-up type
	 * 
	 */
	@PostMapping(value = "/changeNameOfType", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CheckUpTypeDTO> changeTypeNameController(@RequestBody String[] params,
			HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);

		if (user != null) {
			ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
			if (clinicAdministrator != null) {
				Clinic clinic = clinicAdministrator.getClinic();
				if (clinic != null) {
					CheckUpType temp = new CheckUpType();
					temp = clinicService.editType(clinic, params[0], params[1], params[2]);
					if (temp == null) {
						return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
					} else {
						return new ResponseEntity<>(new CheckUpTypeDTO(temp), HttpStatus.OK);
					}
				}
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
	}

	/**
	 * This method servers for updating type of check-up
	 * 
	 * @param name    - the name of type that have to be returned
	 * @param request - information of logged user
	 * @return - (ArrayList<CheckUpTypeDTO>) This method returns one type with
	 *         entered name
	 * 
	 */
	@PostMapping(value = "/searchOneType/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<CheckUpTypeDTO>> getOneTypeController(@PathVariable String name,
			HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		ArrayList<CheckUpTypeDTO> temp = new ArrayList<CheckUpTypeDTO>();
		if (user != null) {
			ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
			if (clinicAdministrator != null) {
				temp = clinicService.getOneTypeInClinic(clinicAdministrator, name);
				return new ResponseEntity<>(temp, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
	}

	/**
	 * This method servers for searching rooms in clinic
	 * 
	 * @param params  - the criteria for searching room: name, number or type
	 *                (appointment or operation)
	 * @param request - information of logged user
	 * @return - (List<RoomDTO>) This methods return all room with entered criteria
	 * 
	 */

	@PostMapping(value = "/searchRooms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RoomDTO>> searchRoomsController(@RequestBody String[] params,
			HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
			if (clinicAdministrator != null) {
				Clinic clinic = clinicAdministrator.getClinic();
				if (clinic != null) {
					List<RoomDTO> ret = clinicService.searchRooms(clinic, params);
					return new ResponseEntity<>(ret, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
				}
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
	}

	/**
	 * This method servers for filter room by criteria
	 * 
	 * @param number  - number of filtering room
	 * @param request - information of logged user
	 * @return - (List<RoomDTO>) This method returns room with entered criteria
	 *         (entered number)
	 * 
	 */
	@PostMapping(value = "/filterRooms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RoomDTO>> filterRoomsController(@RequestBody int[] number, HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
			if (clinicAdministrator != null) {
				Clinic clinic = clinicAdministrator.getClinic();
				if (clinic != null) {
					List<RoomDTO> ret = clinicService.filterRooms(clinic, number[0]);
					return new ResponseEntity<>(ret, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
				}
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
	}

	@PostMapping(value = "/searchClinic", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ClinicDTO>> searchClinics(@RequestBody String[] params) {
		List<ClinicDTO> ret = clinicService.searchClinics(params);
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@PostMapping(value = "/filterClinic/{p}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Clinic>> filterClinics(@PathVariable String p, @RequestBody List<Clinic> clinics) {
		List<Clinic> ret = new ArrayList<Clinic>();
		ret = clinicService.filterClinics(p, (ArrayList<Clinic>) clinics);

		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	/*
	 * find all doctors in one clinic by clinic id input - string, clinic id return
	 * value - List<MedicalWorker> , list of all doctors in clinic
	 */
	@PostMapping(value = "/clinicDoctors", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MedicalWorkerDTO>> getDoctorsByClinicId(@RequestBody String[] params) {
		List<MedicalWorkerDTO> ret = clinicService.doctorsInClinic(params[0], params[1], params[2]);
		if (ret == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(ret, HttpStatus.OK);
		}
	}

	/**
	 * Method for creating new clinic
	 * @param c - new clinic that has to be created
	 * @return - (ClinicDTO) This method returns created clinic
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClinicDTO> saveClinic(@RequestBody ClinicDTO c) {
		Clinic clinic = clinicService.save(c);
		if (clinic == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		return new ResponseEntity<>(new ClinicDTO(clinic), HttpStatus.CREATED);
	}

	/**
	 * This method servers for getting all rooms in clinic
	 * 
	 * @param request - information of logged user
	 * @return - (List<RoomDTO>) This method returns rooms in clinic
	 * 
	 */
	@GetMapping(value = "/getRooms", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RoomDTO>> getRooms(HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			List<RoomDTO> dtos = new ArrayList<RoomDTO>();
			dtos = clinicService.getAllRooms(user);
			if (dtos != null) {
				return new ResponseEntity<>(dtos, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// This method is not used at the moment
	@GetMapping(value = "/getFreeRooms", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Room>> getFreeRooms(HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
			if (clinicAdministrator != null) {
				Clinic clinic = clinicAdministrator.getClinic();
				if (clinic != null) {
					List<Room> rooms = roomService.findAllByClinicId(clinic.getId());
					Set<Room> temp = new HashSet<Room>();
					for (Room r : rooms) {
						temp.add(r);
					}
					return new ResponseEntity<>(temp, HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * This method servers for getting all doctors in clinic
	 * 
	 * @param request - information of logged user
	 * @return - (ArrayList<MedicalWorkerDTO>) This method returns all medical
	 *         workers in clinic
	 */
	@GetMapping(value = "/getDoctors", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<MedicalWorkerDTO>> getAllMedicalWorkers(HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		ArrayList<MedicalWorkerDTO> dtos = new ArrayList<MedicalWorkerDTO>();
		if (user != null) {
			dtos = clinicService.getAllDoctors(user);
			if (dtos != null) {
				return new ResponseEntity<>(dtos, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * This method servers for getting all types in clinic
	 * 
	 * @param request - information of logged user
	 * @return - (ArrayList<CheckUpTypesDTO>) This method returns all type of
	 *         check-ups in clinic
	 */
	@GetMapping(value = "/getAllTypes", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<CheckUpTypeDTO>> getAllTypes(HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		ArrayList<CheckUpTypeDTO> dtos = new ArrayList<CheckUpTypeDTO>();
		if (user != null) {
			dtos = clinicService.getAllTypes(user);
			if (dtos != null) {
				return new ResponseEntity<>(dtos, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * This method servers for getting clinic of logged administrator
	 * 
	 * @param request - information of logged user
	 * @return - (ClinicDTO) This method returns clinic of user who is administrators
	 *         of clinic and who is logged
	 */
	@GetMapping(value = "/getClinic", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClinicDTO> getClinic(HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
			if (clinicAdministrator != null) {
				Clinic clinic = clinicService.findOneById(clinicAdministrator.getClinic().getId());
				return new ResponseEntity<>(new ClinicDTO(clinic), HttpStatus.OK);
			} else
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * This method servers for deleting room by number in clinic
	 * 
	 * @param number  Number of room that have to be deleted
	 * @param request -
	 * @return - (String) This method returns 'Obrisano' if room is deleted and
	 *         'Greska' if room can not be deleted
	 */
	@PostMapping(value = "/deleteRoom/{number}")
	public ResponseEntity<String> deleteTypeController(@PathVariable int number, HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
			if (clinicAdministrator != null) {
				String returnVal = clinicService.deleteRoomN(number, clinicAdministrator);
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
	 * This method servers for adding room in clinic
	 * 
	 * @param room    - room that have to be added in clinic
	 * @param request - information of logged user
	 * @return - (RoomDTO) This method returns added room in clinic
	 */
	@PostMapping(value = "/addRoom", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RoomDTO> addRoomController(@RequestBody RoomDTO room, HttpServletRequest request) {

		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);

		if (user != null) {
			ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
			if (clinicAdministrator != null) {
				Room room1 = clinicService.addRoom(room, clinicAdministrator);
				if (room1 == null) {
					return new ResponseEntity<>(room, HttpStatus.ALREADY_REPORTED);
				} else {
					return new ResponseEntity<>(new RoomDTO(room1), HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<>(room, HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<>(room, HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * This method servers for updating room in clinic by administrator
	 * 
	 * @param room    - room that have to be updated
	 * @param request - information of logged user
	 * @return - (RoomDTO) This method returns updated room
	 */

	@PostMapping(value = "/changeRoom", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RoomDTO> changeRoomController(@RequestBody RoomDTO room, HttpServletRequest request) {

		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);

		if (user != null) {
			ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
			if (clinicAdministrator != null) {
				Room room1 = clinicService.changeRoom(room, clinicAdministrator);
				if (room1 == null) {
					return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
				} else {
					return new ResponseEntity<>(new RoomDTO(room1), HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * This method servers for getting clinic raiting
	 * 
	 * @param request - information of logged user
	 * @return - (Double) This method returns raiting of clinic
	 */
	@GetMapping(value = "/getClinicRaiting", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Double> getClinicRaitinController(HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			double retVal = clinicService.getClinicRaiting(user);
			return new ResponseEntity<>(retVal, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
	}

	/**
	 * This method servers for getting report for month in clinic
	 * 
	 * @param request - information of logged user
	 * @return - (Integer[]) This method returns the numbers of appointment or
	 *         operations in clinic at one month
	 */
	@GetMapping(value = "/getReportForMonth", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer[]> getReportForMonthController(HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			Integer[] returnValue = clinicService.getReportForMonth(user);
			return new ResponseEntity<>(returnValue, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * This method servers for getting revenue in clinic for entered period
	 * 
	 * @param request - information of logged user
	 * @param params  - start date an end date
	 * @return - (Double) This method returns how much clinic is earned in entered
	 *         period
	 */
	@PostMapping(value = "/getRevenue", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Double> getRevenueController(HttpServletRequest request, @RequestBody String[] params) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			Double returnValue = clinicService.getRevenue(user, params);
			if (returnValue == null) {
				return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
			} else {
				return new ResponseEntity<>(returnValue, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * This method servers for getting report for week in clinic
	 * 
	 * @param request - information of logged user
	 * @return - (Integer[]) This method returns the numbers of appointment or
	 *         operations in clinic at one week
	 */
	@GetMapping(value = "/getReportForWeek", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer[]> getRepostForWeekController(HttpServletRequest request) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		User user = userService.findOneByEmail(email);
		if (user != null) {
			Integer[] returnValue = clinicService.getReportForWeek(user);
			return new ResponseEntity<>(returnValue, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		}
	}
	
	/**
	 * Method for adding rooms in the clinic at the moment of creating
	 * @param rooms - rooms that will be added in clinic
	 * @param id - id/key of the clinic in the database
	 * @return - (List<RoomDTO>) This method returns added rooms in clinic
	 * 
	 */
	@PostMapping(value = "/addRooms/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RoomDTO>> addRooms(@RequestBody List<RoomDTO> rooms, @PathVariable Long id) {
		Clinic clinic = clinicService.findOneById(id);
		for (RoomDTO room : rooms) {
			Room r = clinicService.addRoom(clinic, room);
			if (r == null)
				return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
		}
		return new ResponseEntity<>(rooms, HttpStatus.OK);
	}

	@GetMapping(value = "/getClinicsByType/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ClinicDTO>> getClinicByCheckupType(@PathVariable String name) {
		Set<Clinic> clinics = checkUpTypeService.findClinics(name);
		List<ClinicDTO> ret = new ArrayList<>();
		for (Clinic clinic : clinics) {
			ret.add(new ClinicDTO(clinic));
		}
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@PostMapping(value = "/getSelectedDoctor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MedicalWorkerDTO> getSelectedDoctor(@RequestBody String[] docId, HttpServletRequest request) {
		MedicalWorkerDTO ret = clinicService.getSelectedDoctor(Long.parseLong(docId[0]), docId[1]);
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@PostMapping(value = "/allDocsOneClinic/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MedicalWorkerDTO>> getAllDoctorsInOneClinic(@PathVariable String id,
			HttpServletRequest request) {
		List<MedicalWorkerDTO> ret = clinicService.getAllDoctorsInOneClinic(Long.parseLong(id));
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	/**
	 * Method returns all available rooms for date of the check-up from one clinic with the chosen type of check-up (operation/appointment)
	 * @param id - id of the check-up in database
	 * @return - (List<RoomDTO>) This method returns rooms in clinic by type of the check-up
	 */
	@GetMapping(value = "/getRooms/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RoomDTO>> getRoomsByClinicIdAndType(@PathVariable Long id) {
		List<Room> rooms = roomService.findAllByClinicIdAndTypeRoom(id);
		List<RoomDTO> ret = new ArrayList<>();
		for (Room room : rooms) {
			ret.add(new RoomDTO(room));
		}
		return new ResponseEntity<List<RoomDTO>>(ret, HttpStatus.OK);
	}

	/**
	 * Method returns all available terms of the room for selected date and time
	 * @param id - id of the room in database
	 * @return - (ArrayList<Integer>) This method returns list of the available terms
	 */
	@GetMapping(value = "/roomAvailability/{id}/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Integer>> getRoomVailability(@PathVariable Long id, @PathVariable String date) {
		ArrayList<Integer> roomAvailability = roomService.findRoomAvailability(id, date);
		return new ResponseEntity<List<Integer>>(roomAvailability, HttpStatus.OK);
	}

	/**
	 * method for getting clinic object when given clinic id
	 * 
	 * @param id      - clinic id in database
	 * @param request - HttpServletRequest, to find logged in user
	 * @return ClinicDTO clinic - clinic object found in database
	 */
	@GetMapping(value = "/getDetails/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClinicDTO> getClinicDetails(@PathVariable Long id, HttpServletRequest request) {
		Clinic ret = clinicService.findOneById(id);
		if (ret == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		else {
			ClinicDTO newRet = new ClinicDTO(ret);
			return new ResponseEntity<>(newRet, HttpStatus.OK);
		}

	}
	
	/**
	 * method for rating clinic that logged patient visited
	 * @param request
	 * @param param - param[0] is checkup id , param[1] is given rating
	 * @return
	 */
	@PostMapping(value = "/rateClinic", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> rateVisitedClinic(HttpServletRequest request, @RequestBody String[] param) {
		String token = tokenUtils.getToken(request);
		String email = tokenUtils.getUsernameFromToken(token);
		boolean ok = clinicService.rateClinic(email, param);
		if (ok) {
			return new ResponseEntity<>("uspesno ocenjen", HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

}