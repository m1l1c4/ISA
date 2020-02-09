package tim31.pswisa.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonManagedReference(value = "report_recipe_mov")
	@OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Recipe> recipes;

	@JsonBackReference(value = "record_mov")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private MedicalRecord medicalRecord;

	@JsonManagedReference(value = "checkup_report_mov")
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Checkup checkUp;

	@Column(name = "informations", unique = false, nullable = true)
	private String informations;

	@Column(name = "diagnose", unique = false, nullable = false)
	private String diagnose;

	public Report() {
		super();
	}

	
	
	public Report(Set<Recipe> recipes, MedicalRecord medicalRecord,
			Checkup checkUp, String informations, String diagnose) {
		super();
		this.recipes = recipes;
		this.medicalRecord = medicalRecord;
		this.checkUp = checkUp;
		this.informations = informations;
		this.diagnose = diagnose;
	}



	public MedicalRecord getMedicalRecord() {
		return medicalRecord;
	}

	public void setMedicalRecord(MedicalRecord medicalRecord) {
		this.medicalRecord = medicalRecord;
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

	public Set<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(Set<Recipe> recipes) {
		this.recipes = recipes;
	}

	public Checkup getCheckUp() {
		return checkUp;
	}

	public void setCheckUp(Checkup checkUp) {
		this.checkUp = checkUp;
	}

}
