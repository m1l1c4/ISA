package tim31.pswisa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tim31.pswisa.model.Patient;
import tim31.pswisa.service.EmailService;
import tim31.pswisa.service.MedicalRecordService;
import tim31.pswisa.service.PatientService;

@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private MedicalRecordService medicalRecordService;

	/**
	 * Method for sending confirmation email to the patient which sent request for
	 * registration
	 * 
	 * @param data - data contains two values, the first one is the reason of the
	 *             rejection (if the request has been rejected) and the second one
	 *             is the email of the patient
	 * @return - (String) Confirmation that email has been sent successfully
	 */
	@PostMapping(value = "/sendConfirm", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> sendConfirmationEmail(@RequestBody String[] data) {
		String email = data[1];
		String text = data[0];

		try {
			emailService.sendAccountConfirmationEmail(email, text);
		} catch (MailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>("Email sent", HttpStatus.OK);
	}

	@PostMapping(value = "/activateEmail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Patient> activateAccount(@PathVariable String id) {
		long p = Integer.parseInt(id);
		Patient px = patientService.findOneByUserId(p);
		px.getUser().setActivated(true);
		px = patientService.save(px);
		medicalRecordService.add(px);
		return new ResponseEntity<>(px, HttpStatus.OK);
	}

	/**
	 * Method for sending email to the patient after changing the date of the operation
	 * @param id - id of the check-up in the database with the new time and date informations
	 * @return - (String) Confirmation that email has been sent successfully
	 */
	@PostMapping(value = "/changeDate/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> activateAccount(@PathVariable Long id) {
		emailService.sendChangeDate(id);
		return new ResponseEntity<>("Email sent", HttpStatus.OK);
	}

	/**
	 * Method for sending email to the doctors after reserving a room for the appointment/operation
	 * @param id - id of the check-up in the database with the all necessary informations
	 * @return - (String) Confirmation that email has been sent successfully
	 */
	@PostMapping(value = "/notifyDoctor/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> notifyDoctor(@PathVariable Long id) {
		emailService.notifyDoctor(id);
		return new ResponseEntity<>("Email sent", HttpStatus.OK);
	}

	/**
	 * Method for sending email to the patient after reserving a room for the operation
	 * @param id - id of the check-up in the database with the all necessary informations
	 * @return - (String) Confirmation that email has been sent successfully
	 */
	@PostMapping(value = "/notifyPatient/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> notifyPatient(@PathVariable Long id) {
		emailService.notifyPatient(id);
		return new ResponseEntity<>("Email sent", HttpStatus.OK);
	}

}
