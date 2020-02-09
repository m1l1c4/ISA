package tim31.pswisa.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tim31.pswisa.dto.AbsenceDTO;
import tim31.pswisa.dto.CheckupDTO;
import tim31.pswisa.dto.MedicalWorkerDTO;
import tim31.pswisa.dto.PatientDTO;
import tim31.pswisa.model.Absence;
import tim31.pswisa.model.Checkup;
import tim31.pswisa.model.Clinic;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.Patient;
import tim31.pswisa.model.User;
import tim31.pswisa.repository.AbsenceRepository;
import tim31.pswisa.repository.MedicalWorkerRepository;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private Environment env;

	@Autowired
	private UserService userService;

	@Autowired
	private MedicalWorkerService medicalWorkerService;

	@Autowired
	private AbsenceService absenceService;

	@Autowired
	private MedicalWorkerRepository medicalWorkerRepository;

	@Autowired
	private AbsenceRepository absenceRepository;

	@Autowired
	private CheckUpService checkupService;

	@Autowired
	private PatientService patientService;

	/**
	 * Method for sending confirmation email to the patient which sent request for
	 * registration
	 * 
	 * @param email - the email of the patient
	 * @param text - the reason of the rejection (if the request has been rejected)
	 * @return - (String) Confirmation that email has been sent successfully
	 */
	@Async
	public void sendAccountConfirmationEmail(String email, String text) throws MailException, InterruptedException {
		System.out.println("Sending email...");
		User u = userService.findOneByEmail(email);
		Patient p = patientService.findOneByUserId(u.getId());
		String path = "http://localhost:3000/activateAccount/" + u.getId();

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo("pswisa.tim31.2019@gmail.com");
		msg.setFrom(env.getProperty("spring.mail.username"));
		msg.setSubject("Account confirmation");
		if (text.equals("approved")) {
			msg.setText("Please confirm your Clinical center account by clicking on link below. \n\n" + path
					+ "\n\nAdministration team");
		} else {
			msg.setText(
					"Your request for registration to Clinical center can't be approved. The reason for rejection is:\n\n"
							+ text + "\n\nAdministration team");
		}
		javaMailSender.send(msg);
		p.setProcessed(true);
		patientService.save(p);
		System.out.println("Email sent.");
	}

	/**
	 * This method servers for sending email notification to clinic administrators
	 * when doctor booked appointment or operation
	 * 
	 * @param clinic    - clinic where doctor works
	 * @param medWorker - doctor who wants to book appointment or operation
	 * @param patient   - patient who has to be examinated
	 * @return - (void) This method has no return value
	 */
	@Async
	public void sendNotificationToAmin(Clinic clinic, MedicalWorker medWorker, Patient patient)
			throws MailException, InterruptedException {
		// Set<ClinicAdministrator> clinicAdministrators = clinic.getClAdmins();
		SimpleMailMessage msg = new SimpleMailMessage();
		// for(ClinicAdministrator ca : clinicAdministrators) {
		System.out.println("Sending email...");
		msg.setTo("pswisa.tim31.2019@gmail.com");
		msg.setFrom(env.getProperty("spring.mail.username"));
		msg.setSubject("New request for operation or appointment");
		msg.setText("There is new request for operation or appointment by doctor " + medWorker.getUser().getName() + " "
				+ medWorker.getUser().getSurname() + " for patient " + patient.getUser().getName() + " "
				+ patient.getUser().getSurname());
		javaMailSender.send(msg);
		System.out.println("Email sent.");
		// }

	}

	/**
	 * This method servers for sending email to medical worker after accepting or
	 * refusing request for vacation when doctor booked appointment or operation
	 * 
	 * @param user   - logged clinic administrator
	 * @param a      - absence of doctor
	 * @param reason - reason for refusing or "" if it is accepted
	 * @return - (void) This method has no return value
	 */
	@Async
	public void sendReasonToMw(User user, AbsenceDTO a, String reason) throws MailException, InterruptedException {
		// send email to medicalWorker
		String email = a.getMedicalWorker().getUser().getEmail();
		User medWorker = userService.findOneByEmail(email);
		MedicalWorker medicalWorker = medicalWorkerService.findByUser(medWorker.getId());
		Absence absence = absenceService.findOneById(a.getId());
		System.out.println(reason);
		if (reason.equals("ok")) {
			// not refuse request for vacation
			medicalWorker.getHollydays().add(absence);
			absence.setAccepted("ACCEPTED");
			absence = absenceRepository.save(absence);
			medicalWorkerRepository.save(medicalWorker);
			SimpleMailMessage msg = new SimpleMailMessage();
			System.out.println("Sending email...");
			msg.setTo("pswisa.tim31.2019@gmail.com");
			msg.setFrom(env.getProperty("spring.mail.username"));
			msg.setSubject("Accepped request for " + a.getTypeOfAbsence());
			msg.setText("Your request is accepted.");
			javaMailSender.send(msg);
			System.out.println("Email sent.");
		} else {
			reason.replace('%', ' ');
			absence.setAccepted("PASSED");
			absence = absenceRepository.save(absence);
			SimpleMailMessage msg = new SimpleMailMessage();
			System.out.println("Sending email...");
			msg.setTo("pswisa.tim31.2019@gmail.com");
			msg.setFrom(env.getProperty("spring.mail.username"));
			msg.setSubject("Refused request for " + a.getTypeOfAbsence());
			msg.setText(reason);
			javaMailSender.send(msg);
			System.out.println("Email sent.");
		}

	}

	/**
	 * Method for sending email to the patient after changing the date of the operation
	 * @param id - id of the check-up in the database with the new time and date informations
	 * @return - (String) Confirmation that email has been sent successfully
	 */
	@Async
	public void sendChangeDate(Long id) {
		Checkup c = checkupService.findOneById(id);
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo("pswisa.tim31.2019@gmail.com");
		msg.setFrom(env.getProperty("spring.mail.username"));
		msg.setSubject("Account confirmation");
		msg.setText("Your operation has been resheduled for " + c.getDate().toString() + " " + c.getTime() + " in the room ."
				+ c.getRoom().getName() + " number: " + c.getRoom().getNumber());
		javaMailSender.send(msg);
		System.out.println("Email sent.");
	}

	/**
	 * Method for sending email to the doctors after reserving a room for the appointment/operation
	 * @param id - id of the check-up in the database with the all necessary informations
	 * @return - (String) Confirmation that email has been sent successfully
	 */
	@Async
	public void notifyDoctor(Long id) {
		Checkup c = checkupService.findOneById(id);
		Set<MedicalWorker> doctors = c.getDoctors();
		SimpleMailMessage msg = new SimpleMailMessage();

		if (c.getTip().equals("PREGLED")) {
			sendEmailToDoctorAndPatient(new CheckupDTO(c));
		} else {
			for (MedicalWorker doctor : doctors) {
				msg.setTo("pswisa.tim31.2019@gmail.com");
				msg.setFrom(env.getProperty("spring.mail.username"));
				msg.setSubject("Operation");
				msg.setText("To " + doctor.getUser().getName() + " " + doctor.getUser().getSurname()+ "\nYou must attend an operation on " + c.getDate().toString() + " " + c.getTime()
						+ " in the room ." + c.getRoom().getName() + " number: " + c.getRoom().getNumber());
				javaMailSender.send(msg);
			}
		}
		System.out.println("Email sent.");
	}

	/**
	 * This method servers for sending email to medical workers and patients at the
	 * end of day to notify when they have appointment or operation
	 * 
	 * @param c   - check-up that has all required information
	 * @return - (void) This method has no return value
	 */
	@Async
	public void sendEmailToDoctorAndPatient(CheckupDTO c) {
		MedicalWorkerDTO mw = c.getMedicalWorker();
		PatientDTO p = c.getPatient();
		// patientEmail and doctorEmail, for real system
		String patientEmail = p.getUser().getEmail();
		String doctorEmail = mw.getUser().getEmail();
		SimpleMailMessage msg = new SimpleMailMessage();
		String path = "http://localhost:3000/ConfirmCheckup/" + c.getId();
		System.out.println("Sending email...");
		msg.setTo("pswisa.tim31.2019@gmail.com");
		msg.setFrom(env.getProperty("spring.mail.username"));
		msg.setSubject("Notification for doctor");
		msg.setText("\nYou have scheduled " + c.getType() + " " + "for: " + "\nDate: " + c.getDate() + "  " + "\nTime: "
				+ c.getTime() + "h " + "\nRoom: " + c.getRoom().getName() + " " + c.getRoom().getNumber()
				+ "\nPatient: " + c.getPatient().getUser().getName() + " " + c.getPatient().getUser().getSurname()
				+ " ");
		javaMailSender.send(msg);
		System.out.println("Email sent.");

		System.out.println("Sending email...");
		msg.setSubject("Notification for patient");
		msg.setText("\nYou have scheduled " + c.getType() + " " + "for: " + "\nDate: " + c.getDate() + "  " + "\nTime: "
				+ c.getTime() + "h  " + "\nDoctor: " + c.getMedicalWorker().getUser().getName() + " "
				+ c.getMedicalWorker().getUser().getSurname() + " " + "\nSpecialization: "
				+ c.getMedicalWorker().getType() + " " + "\nConfirmation: " + path);
		javaMailSender.send(msg);
		System.out.println("Email sent.");

	}

  /**
   * Method for sending email to the patient after reserving a room for the operation
	 * @param id - id of the check-up in the database with the all necessary informations
	 * @return - (String) Confirmation that email has been sent successfully
	 */
	@Async
	public void notifyPatient(Long id) {
		Checkup c = checkupService.findOneById(id);
		SimpleMailMessage msg = new SimpleMailMessage();

		msg.setTo("pswisa.tim31.2019@gmail.com");
		msg.setFrom(env.getProperty("spring.mail.username"));
		msg.setSubject("Appointment");
		msg.setText("Your operation has been scheduled for " + c.getDate().toString() + " " + c.getTime()
				+ "h in the room ." + c.getRoom().getName() + " number: " + c.getRoom().getNumber());

		javaMailSender.send(msg);
		System.out.println("Email sent.");
	}

	/**
	 * method for sending email to patient after successfully booked predefined
	 * appointment
	 * 
	 * @param email
	 * @param text
	 * @throws MailException
	 * @throws InterruptedException
	 */
	@Async
	public void quickAppConfirmationEmail(String email, Checkup checkup) throws MailException, InterruptedException {		String text = "";
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo("pswisa.tim31.2019@gmail.com");
		msg.setFrom(env.getProperty("spring.mail.username"));
		msg.setSubject("Booking checkup confirmation");
		MedicalWorker doc = (MedicalWorker) checkup.getDoctors().toArray()[0];
		text = "We announce you that you successfully booked medical appointment for " + checkup.getDate();
		text += "\nCheckup details: \n" + "Date and time: " + checkup.getDate() + " " + checkup.getTime() + "h\n"
				+ "Clinic: " + checkup.getClinic().getName() + ", " + checkup.getClinic().getAddress() + "\n"
				+ "Doctor: " + doc.getUser().getName() + "\n" + "Room: " + checkup.getRoom().getName() + "\n"
				+ "Price: " + checkup.getPrice();
		msg.setText(text);
		javaMailSender.send(msg);
		System.out.println("Email sent.");
	}

}
