package tim31.pswisa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tim31.pswisa.dto.CheckupDTO;
import tim31.pswisa.dto.MedicalWorkerDTO;
import tim31.pswisa.model.Absence;
import tim31.pswisa.model.Authority;
import tim31.pswisa.model.CheckUpType;
import tim31.pswisa.model.Checkup;
import tim31.pswisa.model.Clinic;
import tim31.pswisa.model.ClinicAdministrator;
import tim31.pswisa.model.MedicalRecord;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.Patient;
import tim31.pswisa.model.Recipe;
import tim31.pswisa.model.Room;
import tim31.pswisa.model.User;
import tim31.pswisa.repository.CheckUpRepository;
import tim31.pswisa.repository.ClinicRepository;
import tim31.pswisa.repository.MedicalWorkerRepository;
import tim31.pswisa.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class MedicalWorkerService {

	@Autowired
	private EmailService emailService;

	@Autowired
	private MedicalWorkerRepository medicalWorkerRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private ClinicService clinicService;

	@Autowired
	private UserService userService;

	@Autowired
	private CheckUpTypeService checkUpTypeService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private ClinicRepository clinicRepository;
	
	@Autowired
	private CheckUpRepository checkupRepository;
	
	@Autowired
	private RecipeService recipeService;
	
	@Autowired
	private MedicalRecordService medicalRecordService;

	public MedicalWorker findOneByUserId(Long id) {
		return medicalWorkerRepository.findOneByUserId(id);
	}
	
	@Autowired
	private CheckUpService checkupService;

	public List<MedicalWorker> findAllByClinicId(Long id) {
		return medicalWorkerRepository.findAllByClinicId(id);
	}

	public Set<MedicalWorker> findAllByTipAndClinicId(String type, Long id) {
		return medicalWorkerRepository.findAllByTipAndClinicId(type, id);
	}

	/**
	 * This method servers for updating medical worker
	 * 
	 * @param medWorker - medical worker who has to be changed
	 * @param mw        - new information about medical worker
	 * @return - (MedicalWorker) This method returns updated medicalWorker
	 */
	public MedicalWorker updateMedicalWorker(MedicalWorker medWorker, MedicalWorkerDTO mw) {
		medWorker.getUser().setName(mw.getUser().getName());
		medWorker.getUser().setSurname(mw.getUser().getSurname());
		medWorker.setPhone(mw.getPhone());
		medWorker = update(medWorker);
		return medWorker;
	}

	public MedicalWorker findByUser(Long id) {
		return medicalWorkerRepository.findOneByUserId(id);
	}

	/**
	 * This method servers for getting medical workers in clinic
	 * 
	 * @param clinic - clinic where medical workers work
	 * @return - (List<MedicalWorker>) This method returns list of medical workers
	 *         in clinic
	 */
	public List<MedicalWorkerDTO> getDoctors(Clinic clinic) {
		List<MedicalWorker> temp = findAllByClinicId(clinic.getId());
		List<MedicalWorkerDTO> returnVal = new ArrayList<MedicalWorkerDTO>();

		for (MedicalWorker med : temp) {
			returnVal.add(new MedicalWorkerDTO(med));
		}
		return returnVal;
	}

	/**
	 * This method servers for deleting medical worker
	 * 
	 * @param email               - medical worker who has to be changed
	 * @param clinicAdministrator - logged clinic administrator
	 * @return - (String) This method returns string ok or ""
	 */
	public String deleteDoctor(String email, ClinicAdministrator clinicAdministrator) {
		Clinic clinic = clinicService.findOneById(clinicAdministrator.getClinic().getId());
		User user = userService.findOneByEmail(email);
		MedicalWorker med = findByUser(user.getId());
		Set<CheckupDTO>allCek = checkupRepository.findAllByScheduledAndMedicalWorkerIdAndFinished(true, med.getId(), false);
		if(allCek.size()!=0) {
			return "";
		}
		else {
			clinic.getMedicalStuff().remove(med);
			clinicRepository.save(clinic);
			med.setClinic(null);
			medicalWorkerRepository.save(med);
			return "Obrisano";
		}
	}

	/**
	 * This method servers for booking appointment by doctor
	 * 
	 * @param user - logged doctor
	 * @param c    - check-up of patient
	 * @return - (void) This method has no return value
	 */
	@Transactional(readOnly = false)
	public boolean bookForPatient(User user, CheckupDTO c) throws MailException, InterruptedException {
		int ok = 0;
		if (user != null) {
			MedicalWorker medWorker = findByUser(user.getId());
			Clinic clinic = medWorker.getClinic();
			Checkup checkup = new Checkup();
			User patient = userService.findOneByEmail(c.getPatient().getUser().getEmail());
			Patient p = patientService.findOneByUserId(patient.getId());
			for (Checkup cek : medWorker.getCheckUps()) {
				if (cek.getDate().equals(c.getDate()) && cek.getTime().equals(c.getTime())) {
					ok = 1;
				}
			}
			for (Absence a : medWorker.getHollydays()) {
				LocalDate d = c.getDate();
				if (((a.getStartVacation().isBefore(d) || a.getStartVacation().isEqual(d)) && a.getAccepted().equals("ACCEPTED"))
						&&( (a.getEndVacation().isAfter(d) || a.getEndVacation().isEqual(d))) && a.getAccepted().equals("ACCEPTED")) {
					ok = 1;
				}
			}
			
			if (ok == 0) {
				checkup.setPatient(p);
				checkup.setScheduled(false);
				checkup.setRatedDoctor(false);
				checkup.setRatedClinic(false);
				checkup.setTip(c.getType());
				checkup.getDoctors().add(medWorker);
				checkup.setTime(c.getTime());
				checkup.setDuration(1);
				checkup.setClinic(clinic);
				CheckUpType temp = checkUpTypeService.findOneByName(medWorker.getType());
				checkup.setCheckUpType(temp);
				List<Room> rooms = roomService.findAllByClinicId(clinic.getId());
				checkup.setPrice(temp.getTypePrice());
				checkup.setDate(c.getDate());
				checkup.setRoom(rooms.get(1));
				checkupService.save(checkup);
				emailService.sendNotificationToAmin(clinic, medWorker, p);
				return true;
			}
			
		}
		return false;
	}
	

	/**
	 * This method servers for checking if doctor can access to medical record of
	 * patient
	 * 
	 * @param user - logged doctor
	 * @param pom  - email of patient
	 * @return - (String) This method returns string 'DA' or 'NE'
	 */
	public String canAccess(User user, Long id) {
		String retVal = "NE";
		Patient p = patientService.findOneByUserId(id);
		MedicalWorker medWorker = findByUser(user.getId());
		
		if(user.getType().equals("DOKTOR")) {
			 for (Checkup c : p.getAppointments()) { 
				 MedicalWorker mw = (MedicalWorker) c.getDoctors().toArray()[0];
				 if(mw.getUser().getEmail().equals((medWorker.getUser().getEmail())) && c.isFinished()) {
					 retVal = "DA"; 
				 }
			 }
		}
		else if(user.getType().equals("MEDICINAR")) {
			MedicalRecord medicalRecord = medicalRecordService.findOneByPatientId(p.getId());
			List<Recipe> recipes = recipeService.findAllByVerifiedAndNurseId(true, medWorker.getId());
			for(Recipe r:recipes) {
				if(r.getReport().getMedicalRecord().getId() == medicalRecord.getId()) {
					return "DA";
				}
			}
		}
		return retVal;

	}

	public List<MedicalWorkerDTO> findDoctors(Clinic clinic, String name, String typeD) {
		Set<MedicalWorker> temp = findAllByTipAndClinicId(typeD, clinic.getId());
		List<MedicalWorkerDTO> returnVal = new ArrayList<MedicalWorkerDTO>();

		if (name.equals("")) {
			for (MedicalWorker med : temp) {
				returnVal.add(new MedicalWorkerDTO(med));
			}
		}

		else {
			for (MedicalWorker med : temp) {
				if (med.getUser().getName().equals(name)) {
					returnVal.add(new MedicalWorkerDTO(med));
				}
			}
		}

		return returnVal;

	}

	public MedicalWorker findOne(Long id) {
		return medicalWorkerRepository.findById(id).orElseGet(null);
	}

	public MedicalWorker update(MedicalWorker mw) {
		return medicalWorkerRepository.save(mw);
	}

	/**
	 * Method for adding new medical worker to the clinic
	 * @param mw - informations of new medical worker
	 * @return - (MedicalWorkerDTO) This method returns created medical worker
	 */
	public MedicalWorker save(MedicalWorkerDTO mw) {
		User user = userRepository.findOneByEmail(mw.getUser().getEmail());
		if (user != null) {
			return null;
		}
		MedicalWorker medicalWorker = new MedicalWorker();
		user = new User();
		user.setName(mw.getUser().getName());
		user.setSurname(mw.getUser().getSurname());
		user.setEmail(mw.getUser().getEmail());
		user.setType(mw.getUser().getType());
		medicalWorker.setUser(user);
		Clinic clinic = clinicService.findOneById(mw.getClinic().getId());
		medicalWorker.setClinic(clinic);
		medicalWorker.setPhone(mw.getPhone());
		medicalWorker.setEndHr(mw.getEndHr());
		medicalWorker.setStartHr(mw.getStartHr());
		medicalWorker.getUser().setPassword(passwordEncoder.encode("sifra123"));
		medicalWorker.getUser().setFirstLogin(false);
		medicalWorker.getUser().setEnabled(true);
		medicalWorker.getUser().setActivated(true);
		medicalWorker.setType(mw.getType());
		if (user.getType().equals("MEDICINAR")) {
			medicalWorker.setType("");
		} else {
			medicalWorker.setType(mw.getType());
		}
		List<Authority> auth = authorityService.findByname(medicalWorker.getUser().getType());
		medicalWorker.getUser().setAuthorities(auth);

		return medicalWorkerRepository.save(medicalWorker);
	}

	public MedicalWorker findOneById(Long id) {
		return medicalWorkerRepository.findOneById(id);
	}

	@Transactional(readOnly = false)
	public List<MedicalWorkerDTO> searchDoctors(String[] params) {		
		List<MedicalWorkerDTO> forSearch = clinicService.doctorsInClinic(params[0], params[1], params[2]);
		String name = params[3].equals("") ? "" : params[3];
		String surname = params[4].equals("") ? "" : params[4];
		int rating = 0;
		List<MedicalWorkerDTO> ret = new ArrayList<MedicalWorkerDTO>();

		if (!params[5].equals("")) {
			rating = Integer.parseInt(params[5]);
		}

		for (MedicalWorkerDTO mw : forSearch) {
			if (checkParams(mw, name, surname, rating)) {
				ret.add(mw);
			}
		}

		return ret;

	}

	public boolean checkParams(MedicalWorkerDTO mw, String name, String surname, int rating) {

		if (!name.equals("") && !mw.getUser().getName().toLowerCase().contains(name.toLowerCase()))
			return false;
		if (!surname.equals("") && !mw.getUser().getSurname().toLowerCase().contains(surname.toLowerCase()))
			return false;
		if (rating != 0 && mw.getRating() != rating)
			return false;

		return true;
	}

	/**
	 * Method for getting all available doctors for the specified date and time
	 * @param id   - id of the clinic in database
	 * @param date - date when doctor needs to be available
	 * @param t - time in the date when doctor needs to be available
	 * @return - (List<MedicalWorker>) This method returns list of the medical
	 *         workers that are available for the specified date and time
	 */
	public List<MedicalWorker> findAllAvailable(Long id, String date, String t) {
		List<MedicalWorker> doctors = medicalWorkerRepository.findAllByClinicId(id);
		int time = Integer.parseInt(t);
		List<MedicalWorker> ret = new ArrayList<>();
		List<Checkup> checkups = checkupService.findAllByTimeAndDate(t, LocalDate.parse(date));
		for (Checkup checkup : checkups) {
			if (checkup.isScheduled()) {
				for (MedicalWorker doctor : checkup.getDoctors()) {
					doctors.remove(doctor);
				}
			}
		}
		
		for (MedicalWorker doctor : doctors) {
			boolean ok = true;
			for (Absence a : doctor.getHollydays()) {
				LocalDate d = LocalDate.parse(date);
				if ((a.getStartVacation().isBefore(d) || a.getStartVacation().isEqual(d))
						&& (a.getEndVacation().isAfter(d) || a.getEndVacation().isEqual(d)) && a.getAccepted().equals("ACCEPTED")) {
					ok = false;
				}
			}
			if (time < doctor.getEndHr() && time >= doctor.getStartHr()
					&& doctor.getUser().getType().equals("DOKTOR") && ok) {
				ret.add(doctor);
			}
		}
		return ret;
	}

	@Transactional(readOnly = false)
	public List<MedicalWorker> findAllDoctors(String type, Long id) {
		return medicalWorkerRepository.findAllDoctors(type, id);
	}
	
	@Transactional(readOnly = false)
	public boolean rateDoctor(String email, String[] param) {	
		Long checkupId ;
		double rating;
		boolean ok = false;
		try {
			checkupId = Long.parseLong(param[0]);		
			rating = Double.parseDouble(param[1]);
			Checkup checkupForRating = checkupService.findOneById(checkupId);
			MedicalWorker doctor = patientService.findDoctor(checkupForRating);
			if (!checkupForRating.isRatedDoctor() && doctor != null) {
				doctor.setRating(doTheMath(doctor.getRating() , rating));
				checkupForRating.setRatedDoctor(true);
				update(doctor);		// this should save doctor with new rating				
				checkupService.save(checkupForRating);
				ok = true;
			}			
		}catch(NumberFormatException e) {
			e.printStackTrace();
		}
		
		return ok;		
	}
	
	public double doTheMath(double prevRating, double rating) {		
		return (prevRating + rating) / 2;		
	}
	
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public MedicalWorker myFindOne(Long id) {
		return medicalWorkerRepository.myFindOne(id);
	}
	
}