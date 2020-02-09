package tim31.pswisa.dto;

import tim31.pswisa.model.Patient;

public class PatientDTO {

	private Long id;
	private UserDTO user;
	private String phoneNumber;
	private String jbo;
	private String city;
	private String state;
	private String address;
	private boolean processed;

	public PatientDTO(Patient p) {
		this(p.getId(), new UserDTO(p.getUser()), p.getPhoneNumber(), p.getJbo(), p.getCity(), p.getState(),
				p.getAddress(), p.isProcessed());
	}

	public PatientDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PatientDTO(Long id, UserDTO user, String phoneNumber, String jbo, String city, String state, String address,
			boolean processed) {
		super();
		this.id = id;
		this.user = user;
		this.phoneNumber = phoneNumber;
		this.jbo = jbo;
		this.city = city;
		this.state = state;
		this.address = address;
		this.processed = processed;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getJbo() {
		return jbo;
	}

	public void setJbo(String jbo) {
		this.jbo = jbo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

}
