package tim31.pswisa.dto;

import java.time.LocalDate;

import tim31.pswisa.model.Absence;

public class AbsenceDTO {

	private Long id;
	private LocalDate startVacation;
	private LocalDate endVacation;
	private ClinicDTO clinicOfAbsence;
	private MedicalWorkerDTO medicalWorker;
	private String typeOfAbsence;
	private String accepted;

	public AbsenceDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AbsenceDTO(Absence a) {
		this(a.getId(), a.getStartVacation(), a.getEndVacation(), new ClinicDTO(a.getClinicOfAbsence()),
				new MedicalWorkerDTO(a.getMw()), a.getTypeOfAbsence(), a.getAccepted());
	}

	public AbsenceDTO(Long id, LocalDate start, LocalDate end, ClinicDTO clinicDTO, MedicalWorkerDTO medicalWorker2,
			String t, String a) {
		super();
		this.id = id;
		this.startVacation = start;
		this.endVacation = end;
		this.clinicOfAbsence = clinicDTO;
		this.medicalWorker = medicalWorker2;
		this.typeOfAbsence = t;
		this.accepted = a;
	}

	public String getAccepted() {
		return accepted;
	}

	public void setAccepted(String accepted) {
		this.accepted = accepted;
	}

	public String getTypeOfAbsence() {
		return typeOfAbsence;
	}

	public void setTypeOfAbsence(String typeOfAbsence) {
		this.typeOfAbsence = typeOfAbsence;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getStartVacation() {
		return startVacation;
	}

	public void setStartVacation(LocalDate start) {
		this.startVacation = start;
	}

	public LocalDate getEndVacation() {
		return endVacation;
	}

	public void setEndVacation(LocalDate end) {
		this.endVacation = end;
	}

	public MedicalWorkerDTO getMedicalWorker() {
		return medicalWorker;
	}

	public void setMedicalWorker(MedicalWorkerDTO medicalWorker) {
		this.medicalWorker = medicalWorker;
	}

	public ClinicDTO getClinicOfAbsence() {
		return clinicOfAbsence;
	}

	public void setClinicOfAbsence(ClinicDTO clinicOfAbsence) {
		this.clinicOfAbsence = clinicOfAbsence;
	}

}
