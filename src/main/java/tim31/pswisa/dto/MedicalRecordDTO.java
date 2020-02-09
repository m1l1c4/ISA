package tim31.pswisa.dto;

import java.util.List;

import tim31.pswisa.model.MedicalRecord;

public class MedicalRecordDTO {

	private Long id;
	private PatientDTO patient;
	private String bloodType;
	private Double diopter;
	private Double height;
	private Double weight;
	private List<DiagnoseDTO> diagnoses;

	public MedicalRecordDTO(MedicalRecord mr) {
		this(mr.getId(), new PatientDTO(mr.getPatient()), mr.getBloodType(), mr.getDiopter(), mr.getHeight(),
				mr.getWeight());
	}

	public MedicalRecordDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MedicalRecordDTO(Long id, PatientDTO patient, String bloodType, Double diopter, Double height,
			Double weight) {
		super();
		this.id = id;
		this.patient = patient;
		this.bloodType = bloodType;
		this.diopter = diopter;
		this.height = height;
		this.weight = weight;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PatientDTO getPatient() {
		return patient;
	}

	public void setPatient(PatientDTO patient) {
		this.patient = patient;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public Double getDiopter() {
		return diopter;
	}

	public void setDiopter(Double diopter) {
		this.diopter = diopter;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public List<DiagnoseDTO> getDiagnoses() {
		return diagnoses;
	}

	public void setDiagnoses(List<DiagnoseDTO> diagnoses) {
		this.diagnoses = diagnoses;
	}	

}
