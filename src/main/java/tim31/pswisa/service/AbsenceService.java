package tim31.pswisa.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim31.pswisa.dto.AbsenceDTO;
import tim31.pswisa.model.Absence;
import tim31.pswisa.model.Checkup;
import tim31.pswisa.model.Clinic;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.repository.AbsenceRepository;
import tim31.pswisa.repository.ClinicRepository;

@Service
public class AbsenceService {

	@Autowired
	private AbsenceRepository abesenceRepository;

	@Autowired
	private ClinicRepository clinicRepository;

	/**
	 * This method servers for getting all absences in database
	 * 
	 * @return - (List<Absence>) This method returns all absences in database
	 */
	public List<Absence> findAll() {
		return abesenceRepository.findAll();
	}

	/**
	 * This method servers for saving new absence in database
	 * 
	 * @param a - absence that have to be saved
	 * @return - (List<Absence>) This method returns saved absence
	 */
	public Absence save(Absence a) {
		return abesenceRepository.save(a);
	}

	/**
	 * This method servers getting one absence from database
	 * 
	 * @param id - id of absence that have to be found
	 * @return - (Absence) This method returns gotten absence
	 */
	public Absence findOneById(Long id) {
		return abesenceRepository.findOneById(id);
	}

	/**
	 * This method servers getting all absence from database in one clinic
	 * 
	 * @param id - id of clinic
	 * @return - (List<Absence>) This method returns list of absences in clinic
	 */
	public List<Absence> findAllByClinicOfAbsenceId(Long id) {
		return abesenceRepository.findAllByClinicOfAbsenceId(id);
	}

	/**
	 * Method for finding all absences and holidays of one medical worker
	 * @param id - id, key of the medical worker
	 * @return - (List<Absence>) This method returns list of absences of one medical worker
	 */
	public List<Absence> findAllByMedicalWorkerId(Long id) {
		return abesenceRepository.findAllByMedicalWorkerId(id);
	}

	/**
	 * Method for creating request for absence/holiday
	 * @param a - information of the absence that will be created
	 * @param mw - medical worker which want to send request for absence
	 * @return - (Absence) This method returns created absence if successful, or null if fails
	 */
	public Absence create(AbsenceDTO a, MedicalWorker mw) {
		Absence absence = new Absence();
		absence.setAccepted("SENT");
		Clinic clinic = clinicRepository.getOne(mw.getClinic().getId());
		Set<Checkup> checkups = mw.getCheckUps();
		for (Checkup c : checkups) {
			if ((a.getStartVacation().isBefore(c.getDate()) || a.getStartVacation().isEqual(c.getDate()))
					&& (a.getEndVacation().isAfter(c.getDate()) || a.getEndVacation().isEqual(c.getDate()))) {
				return null;
			}
		}
		absence.setClinicOfAbsence(clinic);
		absence.setEndVacation(a.getEndVacation());
		absence.setStartVacation(a.getStartVacation());
		absence.setMw(mw);
		absence.setTypeOfAbsence(a.getTypeOfAbsence());
		absence = abesenceRepository.save(absence);
		return absence;
	}

}
