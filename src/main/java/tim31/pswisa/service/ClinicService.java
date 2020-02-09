package tim31.pswisa.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tim31.pswisa.dto.CheckUpTypeDTO;
import tim31.pswisa.dto.ClinicDTO;
import tim31.pswisa.dto.MedicalWorkerDTO;
import tim31.pswisa.dto.RoomDTO;
import tim31.pswisa.model.Absence;
import tim31.pswisa.model.CheckUpType;
import tim31.pswisa.model.Checkup;
import tim31.pswisa.model.Clinic;
import tim31.pswisa.model.ClinicAdministrator;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.Room;
import tim31.pswisa.model.User;
import tim31.pswisa.repository.CheckUpTypeRepository;
import tim31.pswisa.repository.ClinicRepository;
import tim31.pswisa.repository.RoomRepository;

@Service
@Transactional(readOnly = true)

public class ClinicService {

	@Autowired
	private ClinicRepository clinicRepository;

	@Autowired
	private RoomService roomService;

	@Autowired
	private CheckUpService checkupService;

	@Autowired
	private CheckUpTypeService checkUpTypeService;

	@Autowired
	private CheckUpTypeRepository checkupTypeRepository;

	@Autowired
	private ClinicAdministratorService clinicAdministratorService;

	@Autowired
	private MedicalWorkerService medicalWorkerService;

	@Autowired
	private RoomRepository roomRepository;

	/**
	 * This method servers for finding all rooms from database
	 * 
	 * @return - (List<Room>) This method returns all room from database
	 */
	public List<Clinic> findAll() {
		return clinicRepository.findAll();
	}

	/**
	 * This method servers for finding clinic by clinic id
	 * 
	 * @param id - id of clinic that has to be found
	 * @return - (Clinic) This method returns found clinic
	 */
	public Clinic findOneById(Long id) {
		return clinicRepository.findOneById(id);
	}

	/**
	 * This method servers for finding clinic by clinic name
	 * 
	 * @param id - id of room that has to be found
	 * @return - (Clinic) This method returns found clinic
	 */
	public Clinic findOneByName(String clinic) {
		return clinicRepository.findOneByName(clinic);
	}

	/**
	 * This method servers for getting raiting of clinic
	 * 
	 * @param user - logged clinic administrator
	 * @return - (double) This method returns raiting of clinic
	 */
	public double getClinicRaiting(User user) {
		ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
		Clinic clinic = clinicAdministrator.getClinic();
		return clinic.getRating();
	}

	/**
	 * This method servers for getting revenue in clinic for entered period
	 * 
	 * @param user   - scheduled or not scheduled
	 * @param params - two date / period for getting revenue
	 * @return - (Double) This method returns revenue for entered period
	 */
	public Double getRevenue(User user, String[] params) {
		String date1 = params[0];
		String date2 = params[1];
		ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
		Clinic clinic = clinicAdministrator.getClinic();
		LocalDate date1Local = LocalDate.parse(date1);
		LocalDate date2Local = LocalDate.parse(date2);
		Double retValue = 0.0;
		System.out.println(date1Local.toString());
		System.out.println(date2Local.toString());
		if (date1Local.compareTo(date2Local) >= 0) {
			return null;
		} else {
			List<Checkup> checkups = checkupService.findAll();
			for (Checkup c : checkups) {
				if (c.getClinic().getId() == clinic.getId() && c.isScheduled()) {
					LocalDate temp = c.getDate();
					System.out.println(temp.toString());
					System.out.println(c.getDate());
					if (temp.compareTo(date1Local) >= 0 && temp.compareTo(date2Local) <= 0) {
						retValue += c.getPrice();
					}
				}
			}
		}
		return retValue;
	}

	/**
	 * This method servers for getting report for week
	 * 
	 * @param user - logged clinic administrator
	 * @return - (Integer[]) This method returns list of numbers (numbers of
	 *         operations or appointments of the seven days before current date
	 */
	public Integer[] getReportForWeek(User user) {
		ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
		Clinic clinic = clinicAdministrator.getClinic();
		Integer[] retValue = new Integer[7];
		for (int i = 0; i < retValue.length; i++) {
			retValue[i] = 0;
		}
		LocalDate date = LocalDate.now();
		date = LocalDate.parse(date.toString());
		List<Checkup> checkups = checkupService.findAll();
		for (Checkup c : checkups) {
			if (c.getClinic().getId() == clinic.getId() && c.isScheduled()) {
				if (c.getDate().equals(date.minusDays(1))) {
					retValue[6] += 1;
				} else if (c.getDate().equals(date.minusDays(2))) {
					retValue[5] += 1;
				} else if (c.getDate().equals(date.minusDays(3))) {
					retValue[4] += 1;
				} else if (c.getDate().equals(date.minusDays(4))) {
					retValue[3] += 1;
				} else if (c.getDate().equals(date.minusDays(5))) {
					retValue[2] += 1;
				} else if (c.getDate().equals(date.minusDays(6))) {
					retValue[1] += 1;
				} else if (c.getDate().equals(date.minusDays(7))) {
					retValue[0] += 1;
				}
			}
		}
		return retValue;
	}

	/**
	 * This method servers for getting report for month
	 * 
	 * @param user - logged clinic administrator
	 * @return - (Integer[]) This method returns list of numbers (numbers of
	 *         operations or appointments of themonth
	 */
	public Integer[] getReportForMonth(User user) {
		ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
		Clinic clinic = clinicAdministrator.getClinic();
		Integer[] retValue = new Integer[12];
		for (int i = 0; i < retValue.length; i++) {
			retValue[i] = 0;
		}
		List<Checkup> checkups = checkupService.findAll();
		for (Checkup c : checkups) {
			if (c.getClinic().getId() == clinic.getId() && c.isScheduled()) {
				String temp = c.getDate().toString();
				String[] temp1 = temp.split("-");
				String temp2 = temp1[1];
				if (temp2.equals("01")) {
					retValue[0] += 1;
				} else if (temp2.equals("02")) {
					retValue[1] += 1;
				} else if (temp2.equals("03")) {
					retValue[2] += 1;
				} else if (temp2.equals("04")) {
					retValue[3] += 1;
				} else if (temp2.equals("05")) {
					retValue[4] += 1;
				} else if (temp2.equals("06")) {
					retValue[5] += 1;
				} else if (temp2.equals("07")) {
					retValue[6] += 1;
				} else if (temp2.equals("08")) {
					retValue[7] += 1;
				} else if (temp2.equals("09")) {
					retValue[8] += 1;
				} else if (temp2.equals("10")) {
					retValue[9] += 1;
				} else if (temp2.equals("11")) {
					retValue[10] += 1;
				} else if (temp2.equals("12")) {
					retValue[11] += 1;
				}
			}
		}
		return retValue;
	}

	/**
	 * Method for creating new clinic
	 * @param c - new clinic that has to be created
	 * @return - (ClinicDTO) This method returns created clinic
	 */
	@Transactional(readOnly = false)
	public Clinic save(ClinicDTO c) {
		Clinic clinic = new Clinic();
		clinic.setName(c.getName());
		clinic.setCity(c.getCity());
		clinic.setAddress(c.getAddress());
		clinic.setCountry(c.getCountry());
		clinic.setDescription(c.getDescription());
		return clinicRepository.save(clinic);
	}

	/**
	 * This method servers for updating clinic
	 * 
	 * @param clinicAdministrator - logged clinic administrator
	 * @param clinic              - new data of clinic
	 * @return - (Clinic) This method returns updated clinic
	 */
	@Transactional(readOnly = false)
	public Clinic updateClinic(ClinicAdministrator clinicAdministrator, ClinicDTO clinic) throws Exception {
		Clinic nameOfClinic = clinicAdministrator.getClinic();
		List<Clinic> temp = findAll();
		String name1 = clinic.getName();
		for (Clinic c : temp) {
			if (c.getName().equals(name1) && c.getId() != nameOfClinic.getId()) {
				return null;
			}
		}
		nameOfClinic.setName(clinic.getName());
		nameOfClinic.setAddress(clinic.getAddress());
		nameOfClinic.setCity(clinic.getCity());
		nameOfClinic.setDescription(clinic.getDescription());
		for (Room r : nameOfClinic.getRooms())
			r.setClinic(nameOfClinic);
		return clinicRepository.save(nameOfClinic);

	}

	/**
	 * This method servers for editing type of check-up
	 * 
	 * @param clinic - clinic of logged clinic administrator
	 * @param before - older name of type
	 * @param after  - new name of type
	 * @param price  - new price of type
	 * @return - (CheckUpType) This method returns updated check-up type or null if
	 *         there is check-up type with same name
	 */
	@Transactional(readOnly = false)
	public CheckUpType editType(Clinic clinic, String before, String after, String price) {
		CheckUpType retVal = new CheckUpType();
		List<Clinic> clinics = findAll();
		retVal = checkUpTypeService.findOneByName(before);
		for (Clinic klinika : clinics) {
			if (klinika.getAvailableAppointments() != null) {
				for (Checkup c : klinika.getAvailableAppointments()) {
					if (c.getTip().equals(before)) {
						return null; // returns null if can't change name of type
					}
				}
			}
		}
		if (checkUpTypeService.update(retVal, after, price) == null) {
			return null;
		} else {
			retVal = checkUpTypeService.update(retVal, after, price);
			return retVal;
		}
	}

	/**
	 * This method servers for getting one type of check-up
	 * 
	 * @param clinicAdministrator - logged clinic administrator
	 * @param name                - name of type that have to be gotten
	 * @return - (ArrayList<CheckUpTypeDTO>) This method returns one type in list
	 */
	public ArrayList<CheckUpTypeDTO> getOneTypeInClinic(ClinicAdministrator clinicAdministrator, String name) {
		Clinic clinic = clinicAdministrator.getClinic();
		ArrayList<CheckUpTypeDTO> temp = new ArrayList<CheckUpTypeDTO>();
		if (clinic != null) {
			Set<CheckUpType> temps = clinic.getCheckUpTypes();
			for (CheckUpType c : temps) {
				if (c.getName().equals(name)) {
					temp.add(new CheckUpTypeDTO(c));
				}
			}
			return temp;
		}
		return null;
	}

	/**
	 * This method servers for filter rooms in clinic
	 * 
	 * @param clinic - clinic of logged clinic administrator
	 * @param number - number of room
	 * @return - (List<RoomDTO>) This method returns list of rooms with entered
	 *         number (one room)
	 */
	public List<RoomDTO> filterRooms(Clinic clinic, int number) {
		Set<Room> temp = clinic.getRooms();
		List<RoomDTO> ret = new ArrayList<RoomDTO>();
		for (Room r : temp) {
			if (r.getNumber() == number) {
				ret.add(new RoomDTO(r));
				return ret;
			}
		}
		return null;
	}

	/**
	 * This method servers for getting all doctors in clinic
	 * 
	 * @param user - logged clinic administrator
	 * @return - (ArrayList<MedicalWorkerDTO>>) This method returns list of doctors
	 *         in clinic
	 */
	public ArrayList<MedicalWorkerDTO> getAllDoctors(User user) {
		ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
		ArrayList<MedicalWorkerDTO> dtos = new ArrayList<MedicalWorkerDTO>();
		if (clinicAdministrator != null) {
			Clinic clinic = clinicAdministrator.getClinic();
			if (clinic != null) {
				List<MedicalWorker> workers = medicalWorkerService.findAllByClinicId(clinic.getId());
				for (MedicalWorker d : workers) {
					dtos.add(new MedicalWorkerDTO(d));
				}
				return dtos;
			}
		}
		return null;
	}

	/**
	 * This method servers for getting all types of check-ups
	 * 
	 * @param user - logged clinic administrator
	 * @return - (ArrayList<CheckUpTypeDTO>>) This method returns list of check-up
	 *         types
	 */
	public ArrayList<CheckUpTypeDTO> getAllTypes(User user) {
		ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
		ArrayList<CheckUpTypeDTO> dtos = new ArrayList<CheckUpTypeDTO>();
		if (clinicAdministrator != null) {
			Clinic clinic = clinicAdministrator.getClinic();
			if (clinic != null) {
				Set<CheckUpType> tmp = clinic.getCheckUpTypes();
				for (CheckUpType c : tmp) {
					dtos.add(new CheckUpTypeDTO(c));
				}
			}
			return dtos;
		}
		return null;
	}

	/**
	 * This method servers for getting all rooms in clinic
	 * 
	 * @param user - logged clinic administrator
	 * @return - (List<RoomDTO>>) This method returns list rooms in clinic
	 */
	public List<RoomDTO> getAllRooms(User user) {
		ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
		if (clinicAdministrator != null) {
			Clinic clinic = clinicAdministrator.getClinic();
			if (clinic != null) {
				List<Room> rooms = roomService.findAllByClinicId(clinic.getId());
				List<RoomDTO> dtos = new ArrayList<RoomDTO>();
				for (Room room : rooms) {
					LocalDate date = LocalDate.now();
					boolean found = false;
					while (!found) {
						List<Checkup> checkups = checkupService.findAllByRoomIdAndScheduledAndDate(room.getId(), true,
								date);
						if (checkups == null || checkups.size() < 13) {
							found = true;
							room.setFirstFreeDate(date);
							dtos.add(new RoomDTO(room));
						}
						date = date.plusDays(1);
					}
				}
				return dtos;
			}
		}
		return null;
	}

	/**
	 * This method servers for searching rooms in clinic by criteria
	 * 
	 * @param clinic - clinic of logged clinic administrator
	 * @param params - criteria of searching, params[0] is name, params[1] is type
	 *               of room and params[2] is date when it is free
	 * @return - (List<RoomDTO>) This method returns list of rooms with enterd
	 *         criteria
	 */
	public List<RoomDTO> searchRooms(Clinic clinic, String[] params) {
		String name = params[0];
		String type = params[1];
		LocalDate dateTemp = LocalDate.parse(params[2]);
		List<RoomDTO> ret = new ArrayList<RoomDTO>();
		List<Room> rooms = roomRepository.findAllByClinicIdAndTipRoom(clinic.getId(), type);
		for (Room room : rooms) {
			boolean found = false;
			if (!name.equals("") && !name.equals(room.getName())) {
				found = true;
			}
			LocalDate date = dateTemp;
			while (!found) {
				List<Checkup> checkups = checkupService.findAllByRoomIdAndScheduledAndDate(room.getId(), true, date);
				if (checkups == null || checkups.size() < 13) {
					found = true;
					room.setFirstFreeDate(date);
					ret.add(new RoomDTO(room));
				}
				date = date.plusDays(1);
			}
		}

		if (ret.size() == 0) {
			return null;
		} else {

			return ret;
		}
	}

	/**
	 * This method servers for deleting room in clinic by name
	 * 
	 * @param name                - name of room that has to be deleted
	 * @param clinicAdministrator - logged clinic administrator
	 * @return - (String) This method string 'Obrisano' or ''
	 */
	@Transactional(readOnly = false)
	public String deleteRoom(String name, ClinicAdministrator clinicAdministrator) {
		Clinic clinic = findOneById(clinicAdministrator.getClinic().getId());
		Set<Room> sobe = clinic.getRooms();
		for (Room r : sobe) {
			if (r.getName().equals(name)) {
				clinic.getRooms().remove(r);
				try {
					clinic = updateClinic(clinicAdministrator, new ClinicDTO(clinic));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				roomRepository.delete(r);
				return "Obrisano";
			}
		}
		return "";
	}

	/**
	 * This method servers for deleting room in clinic by number
	 * 
	 * @param number              - clinic of logged clinic administrator
	 * @param clinicAdministrator - logged clinic administrator
	 * @return - (String) This method returns string 'Obrisano' or ''
	 */
	@Transactional(readOnly = false)
	public String deleteRoomN(int number, ClinicAdministrator clinicAdministrator) {
		Clinic clinic = findOneById(clinicAdministrator.getClinic().getId());
		Set<Room> sobe = clinic.getRooms();
		for (Room r : sobe) {
			if (r.getNumber() == number) {
				Set<Checkup> ceks = r.getBookedCheckups();
				for (Checkup c : ceks) {
					if (c.isFinished() == false) {
						return "";
					}
				}
			}
			clinic.getRooms().remove(r);
			r.setClinic(null);
			roomRepository.save(r);
			return "Obrisano";
		}

		return "";
	}

	/**
	 * This method servers for adding room in clinic
	 * 
	 * @param room                - room that has to be added
	 * @param clinicAdministrator - logged clinic administrator
	 * @return - (Room) This method returns added room
	 */
	@Transactional(readOnly = false)
	public Room addRoom(RoomDTO room, ClinicAdministrator clinicAdministrator) {
		Room room1 = new Room();
		room1.setName(room.getName());
		room1.setNumber(room.getNumber());
		room1.setTypeRoom(room.getTypeRoom());
		Clinic klinika = new Clinic();
		klinika = findOneById(clinicAdministrator.getClinic().getId());
		List<Room> allRooms = roomService.findAllByClinicId(klinika.getId());
		for (Room r : allRooms) {
			if ((r.getNumber() == room.getNumber()) && r.getClinic() != null) {
				return null;
			}
		}
		room1.setClinic(klinika);
		room1.setFree(true);
		klinika.getRooms().add(room1);
		room1 = roomService.save(room1);
		return room1;
	}

	/**
	 * This method servers for changing room in clinic transaction (D)
	 * 
	 * @param room                - room that has to be changed
	 * @param clinicAdministrator - logged clinic administrator
	 * @return - (Room) This method returns changed room
	 */
	@Transactional(readOnly = false)
	public Room changeRoom(RoomDTO room, ClinicAdministrator clinicAdministrator) {
		Clinic klinika = findOneById(clinicAdministrator.getClinic().getId());
		Room room1 = roomRepository.findOneByClinicIdAndNumber(klinika.getId(), room.getNumber());
		Set<Checkup> ceks = room1.getBookedCheckups();
		for (Checkup c : ceks) {
			if (c.isFinished() == false) {
				return null;
			}
		}
		room1.setName(room.getName());
		room1.setTypeRoom(room.getTypeRoom());
		roomRepository.save(room1);
		return room1;
	}

	public List<ClinicDTO> searchClinics(String[] params) {
		List<Clinic> retClinics = new ArrayList<Clinic>();
		List<ClinicDTO> result = new ArrayList<ClinicDTO>();
		int counter = 0; // assuming there are 7 checkups in one day
		CheckUpType srchType = checkupTypeRepository.findOneByName(params[0]);

		if (params[0].equals("") || params[1].equals("") || srchType == null)
			return null; // nothing to search

		else {
			for (Clinic cl : srchType.getClinics()) {
				retClinics.add(cl); // all clinics of specified type of check-up
			}

			// check if clinic has available doctor, if not remove that clinic from list
			for (Clinic cl : retClinics) {
				for (MedicalWorker mw : cl.getMedicalStuff()) {
					if (mw.getUser().getType().equals("DOKTOR") && mw.getType().equals(params[0])) {
						for (Checkup c : mw.getCheckUps()) {
							if (c.getDate().toString().equals(params[1])) {
								counter++;
							}

						}
						if (counter < 7) {
							ClinicDTO pom = new ClinicDTO(cl);
							pom.setAppPrice(srchType.getTypePrice());
							result.add(pom);
							break;
						}
						counter = 0;
					}
				}
			}

			return result;

		}

	}

	public List<Clinic> filterClinics(String parametar, ArrayList<Clinic> clinics) {
		int ranging = -1;
		List<Clinic> filtered = new ArrayList<Clinic>();
		ranging = Integer.parseInt(parametar);

		for (Clinic clinic : clinics) {
			if (clinic.getRating() >= ranging) {
				filtered.add(clinic);
			}
		}

		return filtered;
	}

	@Transactional(readOnly = false)
	public List<MedicalWorkerDTO> doctorsInClinic(String name, String type, String date) {
		Clinic cl = clinicRepository.findOneByName(name);
		List<MedicalWorkerDTO> doctors = new ArrayList<MedicalWorkerDTO>();
		int counter = 0;
		if (cl != null) {
			for (MedicalWorker medicalWorker : cl.getMedicalStuff()) {
				if (medicalWorker.getType().equals(type) && !absentForTheDate(date, medicalWorker)) {
					for (Checkup c : medicalWorker.getCheckUps()) {
						if (c.getDate().toString().equals(date)) {
							counter++;
						}
					}
					if (counter < 7) {
						MedicalWorkerDTO mw = new MedicalWorkerDTO(medicalWorker);
						doctors.add(mw);						
					}
				}
				counter = 0;
			}
			boolean taken = false;
			ArrayList<String> pom = new ArrayList<String>(); // list of times of appointments for specific date
			for (MedicalWorkerDTO mw : doctors) {
				MedicalWorker medicalWorker = medicalWorkerService.findOne(mw.getId());
				for (int i = medicalWorker.getStartHr(); i < medicalWorker.getEndHr(); i++) {
					for (Checkup ch : medicalWorker.getCheckUps()) {
						if (Integer.parseInt(ch.getTime()) == i || ch.getPending()) {
							taken = true;
							break;
						}
					}
					if (!taken) {
						pom.add(Integer.toString(i));
					} else {
						taken = false;
					}
				}

				mw.getAvailableCheckups().put(date, pom);

			}
			return doctors;
		}
		return null;

	}
	
	@Transactional(readOnly = false)
	public MedicalWorkerDTO getSelectedDoctor(Long parametar, String date) {
		MedicalWorker mww = medicalWorkerService.findOne(parametar);
		LocalDate realDate = LocalDate.parse(date);
		if (mww != null) {
			MedicalWorkerDTO mw = new MedicalWorkerDTO(mww);
			boolean taken = false;
			ArrayList<String> pom = new ArrayList<String>();
			for (int i = mww.getStartHr(); i < mww.getEndHr(); i++) {
				for (Checkup ch : mww.getCheckUps()) {
					if (Integer.parseInt(ch.getTime()) == i && ch.getDate().toString().equals(date)) {
						taken = true;	
						break;
					}
				}
				if (!taken) {
					pom.add(Integer.toString(i));
				} else {
					taken = false;
				}
			}

			mw.getAvailableCheckups().put(date, pom);
			return mw;
		}

		return null;
	}

	/**
	 * Method for adding room in the clinic at the moment of creating
	 * @param clinic - clinic in which room will be added
	 * @param r - room that will be added
	 * @return - (Room) This method returns added room in clinic
	 * 
	 */
	@Transactional(readOnly = false)
	public Room addRoom(Clinic clinic, RoomDTO r) {
		Room room = new Room();
		room.setClinic(clinic);
		room.setFree(true);
		room.setName(r.getName());
		room.setTypeRoom(r.getTypeRoom());
		room.setNumber(r.getNumber());
		room.setFirstFreeDate(r.getFirstFreeDate());
		return roomService.save(room);
	}

	public List<MedicalWorkerDTO> getAllDoctorsInOneClinic(Long parametar) {
		List<MedicalWorker> doctors = (List<MedicalWorker>) medicalWorkerService.findAllByClinicId(parametar);
		List<MedicalWorkerDTO> ret = new ArrayList<MedicalWorkerDTO>();

		for (MedicalWorker mw : doctors) {
			if (mw.getUser().getType().equals("DOKTOR")) {
				ret.add(new MedicalWorkerDTO(mw));
			}
		}

		return ret;
	}
	
	@Transactional(readOnly = false)
	public boolean rateClinic(String email, String[] param) {	
		Long checkupId ;
		double rating;
		boolean ok = false;
		try {
			checkupId = Long.parseLong(param[0]);		
			rating = Double.parseDouble(param[1]);
			Checkup checkupForRating = checkupService.findOneById(checkupId);
			Clinic clinicForRating = checkupForRating.getClinic();
			if (clinicForRating != null && !checkupForRating.isRatedClinic()) {
				double newRating = medicalWorkerService.doTheMath(clinicForRating.getRating(), rating);
				clinicForRating.setRating(newRating);
				checkupForRating.setRatedClinic(true);
				update(clinicForRating);
				checkupService.save(checkupForRating);
				ok = true;
			}
		}catch(NumberFormatException e) {
			e.printStackTrace();
		}
		
		return ok;		
	}
	
	public Clinic update(Clinic clinic) {
		for (Room r : clinic.getRooms())
			r.setClinic(clinic);
		return clinicRepository.save(clinic);
	}
	
	/**
	 * checks if doctor is absent for specific date
	 * @param date
	 * @param mw
	 * @return
	 */
	private boolean absentForTheDate(String date, MedicalWorker mw) {
		LocalDate searchDate = LocalDate.parse(date);
		if (mw.getUser().getType().equals("DOKTOR")) {
			for (Absence absence : mw.getHollydays()) {
				if (absence.getStartVacation().isBefore(searchDate) && absence.getEndVacation().isAfter(searchDate))
						return true;
			}
		}
		return false;
	}
	
}
