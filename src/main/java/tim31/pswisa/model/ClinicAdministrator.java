package tim31.pswisa.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class ClinicAdministrator {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonManagedReference(value = "cadmin_movement")
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private User user;

	@JsonBackReference(value = "admin_clinic_mov")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Clinic clinic;


	/*@OneToMany(mappedBy = "clinicAdministrator", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Absence> absences;*/
	
	/*@JsonManagedReference(value = "ca_mov")
	@OneToMany(mappedBy = "clinicAdministrator", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Checkup> checkupRequests;*/



	public ClinicAdministrator() {
	}

	public ClinicAdministrator(Clinic clinic) {
		super();
		this.clinic = clinic;
	}

	public User getUser() {
		return user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Clinic getClinic() {
		return clinic;
	}

	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}

	/*public Set<Absence> getAbsences() {
		return absences;
	}

	public void setAbsences(Set<Absence> absences) {
		this.absences = absences;
	}*/


}
