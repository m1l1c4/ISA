package tim31.pswisa.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class MedicalRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonBackReference(value = "patient_record_movement")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Patient patient;

	@Column(name = "bloodType", unique = false, nullable = true)
	private String bloodType;

	@Column(name = "diopter", unique = false, nullable = true)
	private Double diopter;

	@Column(name = "height", unique = false, nullable = true)
	private Double height;

	@Column(name = "weight", unique = false, nullable = true)
	private Double weight;

	// private ArrayList<String> allergies;

	public MedicalRecord() {
		super();
	}

	public MedicalRecord(Patient patient, String bloodType, Double diopter, Double height, Double weight) {
		super();
		this.patient = patient;
		this.bloodType = bloodType;
		this.diopter = diopter;
		this.height = height;
		this.weight = weight;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*
	 * public ArrayList<String> getAllergies() { return allergies; }
	 * 
	 * public void setAllergies(ArrayList<String> allergies) { this.allergies =
	 * allergies; }
	 */

}
