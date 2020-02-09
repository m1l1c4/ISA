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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class CheckUpType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "typeName", unique = true, nullable = false)
	private String name;

	@Column(name = "typePrice", unique = false, nullable = false)
	private double typePrice;

	// @JsonManagedReference(value="type_mov")
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "CLINIC_AND_TYPE", joinColumns = {
			@JoinColumn(name = "check_up_type_id") }, inverseJoinColumns = { @JoinColumn(name = "clinic_id") })
	private Set<Clinic> clinics = new HashSet<Clinic>();

	@JsonManagedReference(value = "checkup")
	@OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Checkup> checkups = new HashSet<Checkup>();

	public CheckUpType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CheckUpType(Long id, String name, Set<Checkup> checkups, Set<Clinic> clinics, double t) {
		super();
		this.id = id;
		this.name = name;
		this.checkups = checkups;
		this.clinics = clinics;
		this.typePrice = t;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public double getTypePrice() {
		return typePrice;
	}

	public void setTypePrice(double typePrice) {
		this.typePrice = typePrice;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Checkup> getCheckups() {
		return checkups;
	}

	public void setCheckups(Set<Checkup> checkups) {
		this.checkups = checkups;
	}

	public Set<Clinic> getClinics() {
		return clinics;
	}

	public void setClinics(Set<Clinic> clinics) {
		this.clinics = clinics;
	}

}