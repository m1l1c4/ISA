package tim31.pswisa.dto;

import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.Recipe;

public class RecipeDTO {

	private Long id;
	private CodebookDTO code;
	private ReportDTO report;
	private Boolean verified;
	private MedicalWorkerDTO doctor;
	private MedicalWorkerDTO nurse;

	public RecipeDTO(Recipe r) {
		this.id = r.getId();
		this.code = new CodebookDTO(r.getCode());
		this.report = new ReportDTO(r.getReport());
		this.verified = r.getVerified();
		this.doctor = new MedicalWorkerDTO(r.getDoctor());
		MedicalWorker n = r.getNurse();
		if(n!=null) {
			this.nurse = new MedicalWorkerDTO(n);
		}
	}

	public RecipeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RecipeDTO(Long id, CodebookDTO code, ReportDTO report, Boolean verified, MedicalWorkerDTO doctor,
			MedicalWorkerDTO nurse) {
		super();
		this.id = id;
		this.code = code;
		this.report = report;
		this.verified = verified;
		this.doctor = doctor;
		this.nurse = nurse;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CodebookDTO getCode() {
		return code;
	}

	public void setCode(CodebookDTO code) {
		this.code = code;
	}

	public ReportDTO getReport() {
		return report;
	}

	public void setReport(ReportDTO report) {
		this.report = report;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public MedicalWorkerDTO getDoctor() {
		return doctor;
	}

	public void setDoctor(MedicalWorkerDTO doctor) {
		this.doctor = doctor;
	}

	public MedicalWorkerDTO getNurse() {
		return nurse;
	}

	public void setNurse(MedicalWorkerDTO nurse) {
		this.nurse = nurse;
	}

}
