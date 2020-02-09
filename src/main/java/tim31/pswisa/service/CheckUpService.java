package tim31.pswisa.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tim31.pswisa.dto.CheckupDTO;
import tim31.pswisa.dto.MedicalWorkerDTO;
import tim31.pswisa.model.Absence;
import tim31.pswisa.model.CheckUpType;
import tim31.pswisa.model.Checkup;
import tim31.pswisa.model.Clinic;
import tim31.pswisa.model.ClinicAdministrator;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.Patient;
import tim31.pswisa.model.Room;
import tim31.pswisa.model.User;
import tim31.pswisa.repository.CheckUpRepository;

@Service
@Transactional(readOnly = true)
public class CheckUpService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MedicalWorkerService medicalWorkerService;

	@Autowired
	CheckUpTypeService checkUpTypeService;

	@Autowired
	private ClinicAdministratorService clinicAdministratorService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private ClinicService clinicService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private UserService userService;

	@Autowired
	private ClinicAdministratorService cladminService;

	@Autowired
	private CheckUpRepository checkupRepository;

	//@Autowired
	//private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("example-unit");

	/**
	 * This method servers for getting all check-up from database
	 * 
	 * @return - (Checkup) This method returns all absence from database
	 */
	public List<Checkup> findAll() {
		return checkupRepository.findAll();
	}

	/**
	 * This method servers for getting all check-up from database in one clinic
	 * 
	 * @param id - id of check-up
	 * @return - (List<Checkup>) This method returns all check-ups from database in
	 *         one clinic
	 */
	public List<Checkup> findAllByClinicId(Long id) {
		return checkupRepository.findAllByClinicId(id);
	}

	/**
	 * This method servers for saving check-up in database
	 * 
	 * @param c - check-up that have to be saved
	 * @return - (Checkup) This method returns saved check-up
	 */
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public Checkup save(Checkup c) {
		return checkupRepository.save(c);
	}

	/**
	 * This method servers for getting one check-up by id from database
	 * 
	 * @param id - id of check-up
	 * @return - (Checkup) This method returns all absence from database
	 */
	@Transactional(readOnly = false)
	public Checkup findOneById(Long id) {
		return checkupRepository.findOneById(id);
	}

	/**
	 * This method servers for finding all check-ups that are not scheduled
	 * 
	 * @param scheduled - scheduled or not scheduled
	 * @param user      - logged user / administrator
	 * @return - (List<CheckupDTO) list of check-ups scheduled appointments
	 */
	public List<CheckupDTO> findAllByScheduled(boolean scheduled, User user) {
		ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
		Clinic clinic = clinicAdministrator.getClinic();
		List<Checkup> temp = checkupRepository.findAllByScheduledAndClinicId(scheduled, clinic.getId());
		List<CheckupDTO> retVal = new ArrayList<CheckupDTO>();
		for (Checkup c : temp) {
			if (c.getPatient() != null) {
				retVal.add(new CheckupDTO(c));
			}
		}
		return retVal;
	}

	/**
	 * This method servers for adding new appointment for booking with one click
	 * 
	 * @param c                   - check-up to add for booking with one click
	 * @param mw                  - doctor on examination
	 * @param clinicAdministrator - clinic administrator who adding new appointment
	 * @return - (Checkup) added check-up or null if doctor is busy at that moment
	 */
	@Transactional(readOnly = false)
	public Checkup addAppointment(CheckupDTO c, MedicalWorker mw, ClinicAdministrator clinicAdministrator) {
		Checkup checkup = new Checkup();
		int ok = 0;
		int ok1 = 0;
		for (Checkup cek : mw.getCheckUps()) {
			if (cek.getDate().equals(c.getDate()) && cek.getTime().equals(c.getTime())) {
				ok = 1;
			}
		}
		for (Absence a : mw.getHollydays()) {
			LocalDate d = c.getDate();
			if (((a.getStartVacation().isBefore(d) || a.getStartVacation().isEqual(d))
					&& a.getAccepted().equals("ACCEPTED"))
					&& (((a.getEndVacation().isAfter(d) || a.getEndVacation().isEqual(d)))
							&& a.getAccepted().equals("ACCEPTED"))) {
				ok = 1;
			}
		}
		if (ok == 0) {
			checkup.getDoctors().add(mw);
			CheckUpType typeC = checkUpTypeService.findOneByName(c.getCheckUpType().getName());
			checkup.setCheckUpType(typeC);
			double temp = typeC.getTypePrice();
			if (c.getPrice() == 0) {
				checkup.setPrice(typeC.getTypePrice());
			} else {
				temp = temp - c.getPrice() / 100 * temp;
				checkup.setPrice(temp);
			}
			checkup.setDate(c.getDate());
			checkup.setTime(c.getTime());
			checkup.setTip(c.getType());
			checkup.setDuration(1);
			checkup.setDiscount(0);
			checkup.setScheduled(true);
			checkup.setRatedClinic(false);
			checkup.setRatedDoctor(false);
			Room room = new Room();
			Set<Room> rooms = new HashSet<Room>();
			Clinic clinic = new Clinic();
			clinic = clinicAdministrator.getClinic();
			if (clinic != null) {
				checkup.setClinic(clinic);
				rooms = clinic.getRooms();
				Set<Checkup> checkups = new HashSet<Checkup>();
				if (room.getBookedCheckups() != null) {
					checkups = room.getBookedCheckups();
					for (Checkup pom : checkups) { // same room and same time of appointment
						if (c.getDate().equals(pom.getDate()) && c.getTime().equals(pom.getTime())) {
							ok1 = 1;
						}
					}
				}
				for (Room r : rooms) {
					if (r.getNumber() == c.getRoom().getNumber()) {
						checkup.setRoom(roomService.findOneById(r.getId()));
						checkup.getRoom().getBookedCheckups().add(checkup);
						Room pom = roomService.findOneById(r.getId());
						System.out.println(pom.getName());
						checkup.getRoom().setName(pom.getName());
					}
				}
				room.getBookedCheckups().add(checkup);
				checkup.setTip(c.getType());
				checkup = save(checkup);
				mw.getCheckUps().add(checkup);

				mw = medicalWorkerService.update(mw);
			}

		}
		if (ok1 == 0) {
			return checkup;
		} else {
			return null;
		}
	}

	/**
	 * adds new checkup request to all clinical administrators, after that method
	 * calls aspect for sending email to all clinical administrators
	 * 
	 * @input CheckupDTO ch - checkup that needs to be booked
	 * @output boolean flag - defining whether request is successfully added or not
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public boolean checkupToAdmin(CheckupDTO ch, String email) throws Exception{
		logger.info("slanje requesta adminu pocelo " + email);
		Checkup newCh = new Checkup(0, false, ch.getDate(), ch.getTime(), ch.getType(), 1, 0, null, false);
		User u = userService.findOneByEmail(email);
		Patient p = patientService.findOneByUserId(u.getId());
		boolean ok = checkIfAvailable(ch.getMedicalWorker(), ch.getDate(), ch.getTime());
		Clinic c = clinicService.findOneByName(ch.getClinic().getName());
		CheckUpType chType = checkUpTypeService.findOneByName(ch.getCheckUpType().getName());
		ArrayList<ClinicAdministrator> clAdmins = (ArrayList<ClinicAdministrator>) cladminService.findAll();
		MedicalWorker doctor = medicalWorkerService.myFindOne(ch.getMedicalWorker().getId());
		try { Thread.sleep(7000); } catch (InterruptedException e) { }

		if (u == null || p == null || !ok || c == null || clAdmins == null || chType == null) {
			logger.info("slanje requesta adminu zavrseno sa false" + email);
			return false;
		} else {
			newCh.setPatient(p);
			newCh.getDoctors().add(doctor);
			newCh.setClinic(c);
			newCh.setCheckUpType(chType);
			newCh.setPending(true);
			newCh.setScheduled(false);
			newCh.setRatedClinic(false);
			newCh.setRatedDoctor(false);
			save(newCh);
			logger.info("slanje requesta adminu zavrseno sa true" + email);
			return true;
		}

	}

	/**
	 * checks if given doctor is available at given time, after the request is
	 * started
	 * 
	 * @param mw
	 * @return
	 */
	private boolean checkIfAvailable(MedicalWorkerDTO mw, LocalDate date, String time) {
		String d = date.toString();
		MedicalWorkerDTO doc = clinicService.getSelectedDoctor((Long) mw.getId(), d);
		HashMap<String, List<String>> timesInOneDay = doc.getAvailableCheckups();
		for (String t : timesInOneDay.get(d)) {
			if (t.equals(time)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method for finding all check-ups by room id, scheduled and date
	 * 
	 * @param id        - key of the room
	 * @param scheduled - is scheduled
	 * @param date      - date of the check-ups
	 * @return - (List<Checkup>) This method returns list of found check-ups
	 */
	public List<Checkup> findAllByRoomIdAndScheduledAndDate(Long id, boolean scheduled, LocalDate date) {
		return checkupRepository.findAllByRoomIdAndScheduledAndDate(id, scheduled, date);
	}

	/**
	 * Method for finding all check-ups by room id, scheduled and date
	 * 
	 * @param time - time of the defined check-up
	 * @param date - date of the check-ups
	 * @return - (List<Checkup>) This method returns list of found check-ups
	 */
	public List<Checkup> findAllByTimeAndDate(String time, LocalDate date) {
		return checkupRepository.findAllByTimeAndDate(time, date);
	}

	/**
	 * Method for changing check-up after finding a room, date and time for the
	 * appointment/operation by clinic administrator
	 * 
	 * @param c - check-up with the id of the check-up that has to be updated and
	 *          new informations about appointment
	 * @return - This method returns updated check-up
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Checkup update(CheckupDTO c) throws Exception {
		List<Checkup> temp = checkupRepository.findAllByRoomIdAndTimeAndDate(c.getRoom().getId(), c.getTime(), c.getDate());
		if(temp.size() > 0) {
			return null;
		}		
		Checkup checkup = checkupRepository.findOneById(c.getId());
		Room room = roomService.myFindOne(c.getRoom().getId());
		//try { Thread.sleep(7000); } catch (InterruptedException e) { }
		checkup.setDate(c.getDate());
		checkup.setTime(c.getTime());
		checkup.setRoom(room);
		checkup.setScheduled(true);
		checkup.setDoctors(new HashSet<MedicalWorker>());
		if(checkup.getTip().equals("PREGLED")){
			MedicalWorker doctor = medicalWorkerService.findOneById(c.getMedicalWorker().getId());
			checkup.getDoctors().add(doctor);
		}
		checkup = save(checkup);		
		return checkup;
	}

	/**
	 * Method for adding doctors which clinic administrator has selected to must
	 * attend the operation
	 * 
	 * @param id      - id of the check-up in the database
	 * @param workers - id's of the doctors which will be assigned to operation
	 * @return - This method returns message about success of adding doctors to
	 *         check-up
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW) // transakcija Milica
	public Checkup addDoctors(Long id, Long[] workers) throws Exception {
		Checkup checkup = checkupRepository.findOneById(id);
		checkup.setDoctors(new HashSet<MedicalWorker>());
		for (Long mwId : workers) {
			MedicalWorker mw = medicalWorkerService.findOneById(mwId);
			try { Thread.sleep(7000); } catch (InterruptedException e) { }
			checkup.getDoctors().add(mw);
		}
		if (checkup.getDoctors().size() == 0) {
			return null;
		} else {
			return save(checkup);
		}
	}

	public List<CheckupDTO> getAllQuicks(Long id) {
		logger.info("POCELO DOBAVLJANJE SVIH BRZIH");
		List<MedicalWorker> doctors = medicalWorkerService.findAllDoctors("DOKTOR", id);
		List<CheckupDTO> ret = new ArrayList<CheckupDTO>();
		for (MedicalWorker mw : doctors) {
			for (Checkup ch : mw.getCheckUps()) {
				if (ch.getPatient() == null) {
					ret.add(new CheckupDTO(ch));
				}
			}
		}
		logger.info("ZAVRSENO DOBAVLJANJE SVIH BRZIH");
		return ret;
	}

	/**
	 * books choosen quick checkup for patient
	 * 
	 * @param id    - checkup id
	 * @param email - user email, used for finding patient
	 * @return (boolean) flag - defines whether a checkup is successfully booked or
	 *         not
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Checkup bookQuickApp(Long id, String email) throws Exception {
		logger.info("POCEO SA ZAKAZIVANJEM BRZOG" + email);
		Checkup foundCheckup = checkupRepository.findOneById(id);
		if (foundCheckup == null || foundCheckup.getPatient() != null) { // if checkup has patient than it's already
																			// booked
			logger.info("NEUSPESNO ZAKAZIVANJE" + email);
			return null;
		} else {
			User u = userService.findOneByEmail(email);
			Patient p = patientService.findOneByUserId(u.getId());
			foundCheckup.setPatient(p);
			foundCheckup = checkupRepository.save(foundCheckup); // because of adding patient to checkup

			logger.info("USPESNO ZAKAZIVANJE" + email);
		}

		return foundCheckup;
	}

	/**
	 * Method for getting all check-ups of the logged in user
	 * 
	 * @param user - logged in user
	 * @param id   - id of the room if logged in user is administrator of clinic
	 * @return - This method returns all check-ups of the logged in user
	 */
	public Set<Checkup> getAllCheckups(User user, Long id) {
		if (user.getType().equals("DOKTOR")) {
			MedicalWorker worker = medicalWorkerService.findOneByUserId(user.getId());
			return worker.getCheckUps();
		}
		if (user.getType().equals("ADMINISTRATOR")) {
			Room room = roomService.findOneById(id);
			return room.getBookedCheckups();
		}
		return new HashSet<>();
	}

	/**
	 * Method for finding all check-ups by room id, scheduled and date
	 * 
	 * @param scheduled - is scheduled
	 * @param date      - date of the check-ups
	 * @param id        - key of the patient
	 * @return - (List<Checkup>) This method returns list of found check-ups
	 */
	public List<Checkup> findCheckupsByScheduledAndDateAndPatientIdAndType(boolean scheduled, LocalDate date, Long id,
			String type) {
		return checkupRepository.findAllByScheduledAndDateAndPatientIdAndTip(scheduled, date, id, type);
	}

	/**
	 * method for finding scheduled check-up for today for logged doctor and
	 * selected patient
	 * 
	 * @param email - email of the patient
	 * @param id    - key of the logged medical worker
	 * @return CheckupDTO - found check-up if exist, else null
	 */
	public CheckupDTO findCheckup(String email, Long id) {
		User user = userService.findOneByEmail(email);
		MedicalWorker doctor = medicalWorkerService.findOneByUserId(user.getId());
		Patient patient = patientService.findOneByUserId(id);
		LocalDate date = LocalDate.now();
		List<Checkup> checkups = findCheckupsByScheduledAndDateAndPatientIdAndType(true, date, patient.getId(),
				"PREGLED");
		CheckupDTO checkup;
		for (Checkup c : checkups) {
			checkup = new CheckupDTO(c);
			if (checkup.getMedicalWorker().getId() == doctor.getId() && !checkup.isFinished()) {
				return checkup;
			}
		}
		return null;
	}

	/**
	 * returns patient's checkups depending on type (PREGLED / OPERACIJA)
	 * 
	 * @param email - patient's email
	 * @param type  - type of checkup
	 * @return
	 */
	public HashMap<Integer, List<CheckupDTO>> getPatientCheckups(Long id, String email, String type) {
		User loggedUser = userService.findOneByEmail(email);
		HashMap<Integer, List<CheckupDTO>> ret = new HashMap<Integer, List<CheckupDTO>>(2);
		List<CheckupDTO> incomingCheckups = new ArrayList<CheckupDTO>();
		List<CheckupDTO> historyCheckups = new ArrayList<CheckupDTO>();
		if (loggedUser == null) {
			return null;
		}
		Patient loggedPatient = patientService.findOneByUserId(id);
		if (loggedPatient == null) {
			return null;
		}
		incomingCheckups = getIncomingChps(loggedPatient, type);
		historyCheckups = getChpsFromPast(loggedPatient, type);
		ret.put(1, incomingCheckups);
		ret.put(2, historyCheckups);

		return ret;
	}

	/**
	 * returns all future checkups depending on type
	 * 
	 * @param p
	 * @param type
	 * @return
	 */
	private List<CheckupDTO> getIncomingChps(Patient p, String type) {
		List<CheckupDTO> ret = new ArrayList<CheckupDTO>();
		for (Checkup checkup : p.getAppointments()) {
			if (checkup.getDate().isAfter(LocalDate.now()) || TypeCondition(type, checkup)
				&& checkup.isScheduled() && checkup.getTip().equals(type) ) {
				CheckupDTO temp = new CheckupDTO(checkup);
				MedicalWorker doctor = patientService.findDoctor(checkup);
				if (doctor != null) {
					temp.setMedicalWorker(new MedicalWorkerDTO(doctor));
				}
				ret.add(temp);
			}
		}
		return ret;
	}

	
	private boolean TypeCondition(String type, Checkup checkup) {
		if (type.equals("OPERACIJA"))
			return checkup.getDate().isEqual(LocalDate.now());
		else 
			return checkup.getDate().isEqual(LocalDate.now()) && !checkup.isFinished() ;
	}
	
	/**
	 * returns all previous checkups depending on type
	 * 
	 * @param p
	 * @param type
	 * @return
	 */
	private List<CheckupDTO> getChpsFromPast(Patient p, String type) {
		List<CheckupDTO> ret = new ArrayList<CheckupDTO>();
		for (Checkup checkup : p.getAppointments()) {
			if (checkup.getDate().isBefore(LocalDate.now()) && checkup.getTip().equals(type)) {
				CheckupDTO temp = new CheckupDTO(checkup);
				MedicalWorker doctor = patientService.findDoctor(checkup);
				if (doctor != null) {
					temp.setMedicalWorker(new MedicalWorkerDTO(doctor));
				}
				ret.add(temp);
			}
		}
		return ret;
	}

	public boolean scheduleCheckup(Long id) {
		boolean ok = true;
		Checkup checkupToSchedule = findOneById(id);
		if (checkupToSchedule == null) {
			ok = false;
		}
		checkupToSchedule.setScheduled(true);
		save(checkupToSchedule);
		return ok;
	}

	public boolean cancelCheckup(Long id) {
		boolean ok = true;
		Checkup checkupToCancel = findOneById(id);
		if (checkupToCancel == null) {
			ok = false;
		}
		logicalRemoval(checkupToCancel);
		return ok;
	}

	/**
	 * logical removal of checkup from clinic, patient and medical worker relation
	 * 
	 * @param checkupToCancel
	 */
	@Transactional(readOnly = false)
	private void logicalRemoval(Checkup checkupToCancel) {
		CheckUpType type = checkupToCancel.getCheckUpType();
		type.getCheckups().remove(checkupToCancel);
		checkUpTypeService.saveTwo(type);
		for (MedicalWorker mw : checkupToCancel.getDoctors()) {
			mw.getCheckUps().remove(checkupToCancel);
			medicalWorkerService.update(mw);
		}
		checkupToCancel.getDoctors().clear();
		Room r = checkupToCancel.getRoom();
		r.getBookedCheckups().remove(checkupToCancel);
		roomService.save(r);
		checkupToCancel.setRoom(null);
		checkupRepository.save(checkupToCancel);
		/*
		 * Report rep = checkupToCancel.getReport(); rep.setCheckUp(null);
		 * reportService.save(rep);
		 */
	}

	public List<Checkup> findAllByFinishedAndPatientIdAndTip(boolean finished, Long id, String type) {
		return checkupRepository.findAllByFinishedAndPatientIdAndTip(finished, id, type);
	}

	public List<Checkup> findAllByScheduledAndClinicId(boolean scheduled, Long clinicId) {
		return checkupRepository.findAllByScheduledAndClinicId(scheduled, clinicId);
	}

}
