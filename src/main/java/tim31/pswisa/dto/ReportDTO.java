package tim31.pswisa.dto;

import tim31.pswisa.model.Report;

public class ReportDTO {

	private Long id;
	private String informations;
	private String diagnose;
	private CheckupDTO checkUp;

	public ReportDTO(Report r) {
		this(r.getId(), r.getInformations(), r.getDiagnose(), new CheckupDTO(r.getCheckUp()));
	}

	public ReportDTO() {
		super();
	}

	public ReportDTO(Long id, String informations, String diagnose, CheckupDTO checkup) {
		super();
		this.id = id;
		this.informations = informations;
		this.diagnose = diagnose;
		this.checkUp = checkup;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInformations() {
		return informations;
	}

	public void setInformations(String informations) {
		this.informations = informations;
	}

	public String getDiagnose() {
		return diagnose;
	}

	public void setDiagnose(String diagnose) {
		this.diagnose = diagnose;
	}

	public CheckupDTO getCheckUp() {
		return checkUp;
	}

	public void setCheckUp(CheckupDTO checkUp) {
		this.checkUp = checkUp;
	}

}
