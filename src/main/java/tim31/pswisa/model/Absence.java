package tim31.pswisa.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Absence {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "startVacation")
	private LocalDate startVacation;

	@Column(name = "endVacation")
	private LocalDate endVacation;

	@JsonBackReference(value = "clinicAbsence_mov")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Clinic clinicOfAbsence;

	@JsonBackReference(value = "vacation")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private MedicalWorker medicalWorker;

	@Column(name = "typeOfAbsence")
	private String typeOfAbsence;

	// SENT, ACCEPTED, PASSED
	@Column(name = "accepted")
	private String accepted;

	public Absence() {

	}

	public Absence(LocalDate start, LocalDate end, MedicalWorker mw, String t, Clinic c, String a) {
		super();
		this.startVacation = start;
		this.endVacation = end;
		this.medicalWorker = mw;
		this.clinicOfAbsence = c;
		this.typeOfAbsence = t;
		this.accepted = a;
	}

	public String getAccepted() {
		return accepted;
	}

	public void setAccepted(String accepted) {
		this.accepted = accepted;
	}

	public Clinic getClinicOfAbsence() {
		return clinicOfAbsence;
	}

	public void setClinicOfAbsence(Clinic clinicOfAbsence) {
		this.clinicOfAbsence = clinicOfAbsence;
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

	public MedicalWorker getMw() {
		return medicalWorker;
	}

	public void setMw(MedicalWorker mw) {
		this.medicalWorker = mw;
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

}
