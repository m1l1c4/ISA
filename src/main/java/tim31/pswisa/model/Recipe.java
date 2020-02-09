package tim31.pswisa.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Recipe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonBackReference(value = "doctor_recipe_mov")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private MedicalWorker doctor;

	@JsonBackReference(value = "recipe_code_mov")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Codebook code;

	@JsonBackReference(value = "report_recipe_mov")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Report report;

	@JsonBackReference(value = "nurse_recipe_mov")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private MedicalWorker nurse;

	@Column(name = "verified", unique = false, nullable = false)
	private Boolean verified;

	@Version
	private Long version;

	public Recipe() {

	}

	public Codebook getCode() {
		return code;
	}

	public void setCode(Codebook code) {
		this.code = code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public MedicalWorker getNurse() {
		return nurse;
	}

	public void setNurse(MedicalWorker nurse) {
		this.nurse = nurse;
	}

	public MedicalWorker getDoctor() {
		return doctor;
	}

	public void setDoctor(MedicalWorker doctor) {
		this.doctor = doctor;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
