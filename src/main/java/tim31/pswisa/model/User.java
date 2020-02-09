package tim31.pswisa.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "korisnik")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "active", nullable = true)
	private boolean activated; // if patient activated his account

	@Column(name = "firstLogin", nullable = true) // default: false
	private boolean firstLogin;

	@Column(name = "ime", nullable = true)
	private String name;

	@Column(name = "prezime", nullable = true)
	private String surname;

	@Column(name = "type", nullable = true)
	private String type;

	@Column(name = "enabled", nullable = true)
	private boolean enabled; // authorization for accessing methods

	/*
	 * @Transient
	 * 
	 * @Autowired private PasswordEncoder passwordEncoder;
	 */

	@Column(name = "last_password_reset_date", nullable = true)
	private Timestamp lastPasswordResetDate;

	@JsonBackReference(value = "user_movement")
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Patient p;

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	@JsonBackReference(value = "medworker_movement")
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private MedicalWorker medicalWorker;

	@JsonBackReference(value = "cadmin_movement")
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private ClinicAdministrator clinicAdministrator;

	// @JsonBackReference()
	@JsonBackReference(value = "ccadmin_movement")
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private ClinicalCenterAdministrator ccAdmin;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private List<Authority> authorities;

	public User() {

	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public Timestamp getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	public ClinicAdministrator getClinicAdministrator() {
		return clinicAdministrator;
	}

	public void setClinicAdministrator(ClinicAdministrator clinicAdministrator) {
		this.clinicAdministrator = clinicAdministrator;
	}

	public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

	public boolean getFirstLogin() {
		return firstLogin;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setFirstLogin(boolean firstLogin) {
		this.firstLogin = firstLogin;
	}

	public Patient getP() {
		return p;
	}

	public void setP(Patient p) {
		this.p = p;
	}

	public MedicalWorker getMedicalWorker() {
		return medicalWorker;
	}

	public void setMedicalWorker(MedicalWorker medicalWorker) {
		this.medicalWorker = medicalWorker;
	}

	public ClinicalCenterAdministrator getCcAdmin() {
		return ccAdmin;
	}

	public void setCcAdmin(ClinicalCenterAdministrator ccAdmin) {
		this.ccAdmin = ccAdmin;
	}

	public User(String email, String password, String name, String surname) {
		super();
		this.email = email;
		this.password = password;
		this.name = name;
		this.surname = surname;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean getActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		Timestamp now = new Timestamp(DateTime.now().getMillis());
		this.setLastPasswordResetDate(now);
		// this.password = passwordEncoder.encode(password);
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		/*Authority auth = new Authority();
		auth.setName("ROLE_" + type);
		this.authorities.add(auth);*/
		return this.authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enabled;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.getEmail();
	}

}
