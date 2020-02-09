package tim31.pswisa.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class MedicalWorker {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonManagedReference(value = "medworker_movement")
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private User user;

	@JsonBackReference(value = "clinic_movement")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Clinic clinic;

	@Column(name = "phone", unique = false, nullable = true)
	private String phone;

	@JsonManagedReference(value = "vacation")
	@OneToMany(mappedBy = "medicalWorker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Absence> hollydays = new HashSet<Absence>();

	@JsonManagedReference(value = "nurse_recipe_mov")
	@OneToMany(mappedBy = "nurse", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Recipe> recipes;

	@JsonManagedReference(value = "doctor_recipe_mov")
	@OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Recipe> recipesDoctor;
	// just for doctors

	@Column(name = "rating", unique = false, nullable = true)
	private double rating;

	@Column(name = "startHr", unique = false, nullable = true)
	private int startHr;

	@Column(name = "endHr", unique = false, nullable = true)
	private int endHr;

	@Column(name = "tip", unique = false, nullable = true)
	private String tip;

	@JsonBackReference(value = "doctor_checkup_mov")
	@ManyToMany(mappedBy = "doctors")
	private Set<Checkup> checkUps = new HashSet<Checkup>();

	@Version
	private Long version;

	// just for nurse
	// @ManyToMany(mappedBy = "medicalWorkers")
	// private Set<Recipe> receipts = new HashSet<Recipe>();

	public MedicalWorker() {
		super();
	}

	public MedicalWorker(Long id, User user, Clinic clinic, String tip) {
		super();
		this.id = id;
		this.user = user;
		this.clinic = clinic;
		this.tip = tip;
	}

	public MedicalWorker(MedicalWorker m) {
		this.id = m.id;
		this.checkUps = m.getCheckUps();
		this.clinic = m.clinic;
		this.endHr = m.endHr;
		this.hollydays = m.hollydays;
		this.phone = m.phone;
		this.rating = m.rating;
		this.recipes = m.recipes;
		this.startHr = m.startHr;
		this.tip = m.tip;
		this.user = m.user;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Checkup> getCheckUps() {
		return checkUps;
	}

	public void setCheckUps(Set<Checkup> checkUps) {
		this.checkUps = checkUps;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Clinic getClinic() {
		return clinic;
	}

	public String getType() {
		return tip;
	}

	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}

	/*
	 * public Set<Patient> getPatients() { return patients; }
	 * 
	 * public void setPatients(Set<Patient> patients) { this.patients = patients; }
	 */

	public Set<Absence> getHollydays() {
		return hollydays;
	}

	public void setType(String type) {
		this.tip = type;
	}

	public void setHollydays(Set<Absence> hollydays) {
		this.hollydays = hollydays;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getStartHr() {
		return startHr;
	}

	public void setStartHr(int startHr) {
		this.startHr = startHr;
	}

	public int getEndHr() {
		return endHr;
	}

	public void setEndHr(int endHr) {
		this.endHr = endHr;
	}

	public Set<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(Set<Recipe> recipes) {
		this.recipes = recipes;
	}

	public Set<Recipe> getRecipesDoctor() {
		return recipesDoctor;
	}

	public void setRecipesDoctor(Set<Recipe> recipesDoctor) {
		this.recipesDoctor = recipesDoctor;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}