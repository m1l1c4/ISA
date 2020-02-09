package tim31.pswisa.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim31.pswisa.dto.ReportDTO;
import tim31.pswisa.model.Checkup;
import tim31.pswisa.model.Codebook;
import tim31.pswisa.model.MedicalRecord;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.Patient;
import tim31.pswisa.model.Recipe;
import tim31.pswisa.model.Report;
import tim31.pswisa.model.User;
import tim31.pswisa.repository.ReportRepository;

@Service
public class ReportService {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private CheckUpService checkupService;

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private PatientService patientService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MedicalWorkerService medicalWorkerService;

	public Report add(ReportDTO r) {
		Report report = new Report();
		report.setDiagnose(r.getDiagnose());
		report.setInformations(r.getInformations());
		Checkup checkup = checkupService.findOneById(r.getCheckUp().getId());
		checkup.setFinished(true);
		Patient patient = patientService.findOneById(r.getCheckUp().getPatient().getId());
		MedicalRecord medicalRecord = patient.getMedicalRecord();
		report.setMedicalRecord(medicalRecord);
		report.setCheckUp(checkup);
		checkupService.save(checkup);
		return reportRepository.save(report);
	}

	/**
	 * Method for getting one recipe by id
	 * @param id - id of the recipe in the database
	 * @return - (Recipe) This method returns found recipe
	 */
	public Report findOneById(Long id) {
		return reportRepository.findOneById(id);
	}

	/**
	 * Method for creating recipe after examination of the patient
	 * @param report - report in which recipe should be added
	 * @param code - code of the medication from the codebook
	 * @return - This method returns created recipe
	 */
	public Recipe addRecipe(Report report, Codebook r) {
		Recipe recipe = new Recipe();
		recipe.setCode(r);
		recipe.setReport(report);
		recipe.setVerified(false);
		Set<MedicalWorker> doctor = report.getCheckUp().getDoctors();
		for (MedicalWorker mw : doctor) {
			recipe.setDoctor(mw);
		}
		return recipeService.save(recipe);
	}
	
	public Report save(Report r) {
		return reportRepository.save(r);
	}
	
	public ReportDTO update(String email, ReportDTO r) {
		User user = userService.findOneByEmail(email);
		if(user != null) {
			MedicalWorker medicalWorker = medicalWorkerService.findByUser(user.getId());
			Report report = reportRepository.findOneById(r.getId());
			if(r.getCheckUp().getMedicalWorker().getId()!= medicalWorker.getId()) {
				return null;
			}
			report.setInformations(r.getInformations());
			report = save(report);
			return new ReportDTO(report);
		}
		return null;
	}

}
