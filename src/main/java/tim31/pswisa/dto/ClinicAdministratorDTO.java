package tim31.pswisa.dto;

import tim31.pswisa.model.ClinicAdministrator;

public class ClinicAdministratorDTO {

	private Long id;
	private UserDTO user;
	private ClinicDTO clinic;

	public ClinicAdministratorDTO(ClinicAdministrator ca) {
		this(ca.getId(), new UserDTO(ca.getUser()), new ClinicDTO(ca.getClinic()));
	}

	public ClinicAdministratorDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClinicAdministratorDTO(Long id, UserDTO user, ClinicDTO clinic) {
		super();
		this.id = id;
		this.user = user;
		this.clinic = clinic;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public ClinicDTO getClinic() {
		return clinic;
	}

	public void setClinic(ClinicDTO clinic) {
		this.clinic = clinic;
	}

}