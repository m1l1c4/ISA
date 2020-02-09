package tim31.pswisa.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim31.pswisa.dto.DiagnoseDTO;
import tim31.pswisa.dto.MedicalRecordDTO;
import tim31.pswisa.dto.PatientDTO;
import tim31.pswisa.model.Checkup;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.Patient;
import tim31.pswisa.model.Recipe;
import tim31.pswisa.model.Report;
import tim31.pswisa.repository.PatientRepository;

@Service
public class PatientService {

	@Autowired
	private PatientRepository patientRepository;
	
	@Autowired
	private CheckUpService checkupService;

	public List<PatientDTO> getAllPatients() {
		List<Patient> temp = patientRepository.findAll();
		List<PatientDTO> retVal = new ArrayList<PatientDTO>();
		for (Patient p : temp) {
			retVal.add(new PatientDTO(p));
		}
		return retVal;
	}

	public List<PatientDTO> filterPatients(String jbo) {
		List<Patient> temp = patientRepository.findAll();
		List<PatientDTO> retVal = new ArrayList<PatientDTO>();
		for (Patient p : temp) {
			if (p.getJbo().contains(jbo)) {
				retVal.add(new PatientDTO(p));
			}
		}
		if (retVal.size() == 0)
			return null;
		else
			return retVal;
	}

	public List<PatientDTO> findPatients(String name, String surname, String jbo) {
		List<Patient> temp = patientRepository.findAll();
		List<PatientDTO> retVal = new ArrayList<PatientDTO>();
		for (Patient p : temp) {
			if (p.getJbo().equals(jbo)) {
				retVal.add(new PatientDTO(p));
				return retVal;
			}
		}

		if (name.equals("") && !surname.equals("")) {
			for (Patient p : temp) {
				if (p.getUser().getSurname().equals(surname)) {
					retVal.add(new PatientDTO(p));
				}
			}
		}
		if (!name.equals("") && surname.equals("")) {
			for (Patient p : temp) {
				if (p.getUser().getName().equals(name)) {
					retVal.add(new PatientDTO(p));
				}
			}
		}

		if (retVal.size() == 0) {
			return null;
		} else {
			return retVal;
		}
	}

	/**
	 * Method for getting all requests for registration
	 * 
	 * @param processed - parameter that shows whether the request is processed or
	 *                  not
	 * @return - (List<Patient>) This method returns all patients that have been
	 *         sent request for the registration
	 */
	public List<Patient> findAllByProcessed(boolean processed) {
		List<Patient> patients = patientRepository.findAllByProcessed(processed);
		return patients;
	}

	public Patient findOneById(Long id) {
		return patientRepository.findOneById(id);
	}

	public Patient findOneByUserId(Long id) {
		return patientRepository.findByUserId(id);
	}

	public Patient save(Patient p) {
		return patientRepository.save(p);
	}

	public Patient editP(Patient patient, Patient editp) {
		patient.getUser().setName(editp.getUser().getName());
		patient.getUser().setSurname(editp.getUser().getSurname());
		patient.setAddress(editp.getAddress());
		patient.setCity(editp.getCity());
		patient.setPhoneNumber(editp.getPhoneNumber());
		patient.setState(editp.getState());
		return patient;
	}

	public MedicalRecordDTO getMedicalRecord(Long id) {
		Patient patient = findOneByUserId(id);
		MedicalRecordDTO ret = new MedicalRecordDTO(patient.getMedicalRecord());
		ret.setDiagnoses(patientDiagnoses(patient));

		return ret;
	}

	/**
	 * returns all patient diagnoses from checkup history
	 * 
	 * @param loggedPatient - logged-in patient
	 * @return List<DiagnoseDTO> - list of all diagnoses
	 */
	private List<DiagnoseDTO> patientDiagnoses(Patient loggedPatient) {
		List<DiagnoseDTO> patientDiagnoses = new ArrayList<DiagnoseDTO>();
		LocalDate currentDate = LocalDate.now();
		List<Checkup> checkupsHistory = checkupService.findAllByFinishedAndPatientIdAndTip(true,
										loggedPatient.getId(), "PREGLED");		
		for (Checkup ch : checkupsHistory) {			
				MedicalWorker doctor = findDoctor(ch); // getDcotors should work bc I assume there is one doctor per
				if (ch.getReport().getDiagnose().isEmpty()) {
					ch.getReport().setDiagnose("nema dijagnoze");
				} 
				patientDiagnoses.add(new DiagnoseDTO(ch.getReport().getDiagnose(), ch.getDate(), doctor.getUser().getName(),
						doctor.getUser().getSurname(), doctor.getClinic().getName()));
			
		}
		return patientDiagnoses;
	}
	
	/**
	 * used for finding first doc in checkup, assuming there is only one doctor per checkup
	 * @param c
	 * @return
	 */
	public MedicalWorker findDoctor(Checkup c) {
		MedicalWorker ret = null;
		for (MedicalWorker mw : c.getDoctors()) {
			if (mw.getUser().getType().equals("DOKTOR")) {
				ret = new MedicalWorker(mw);
				break;
			}
		}
		return ret;
	}
	
	

}
