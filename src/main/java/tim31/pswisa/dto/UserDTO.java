package tim31.pswisa.dto;

import tim31.pswisa.model.User;

public class UserDTO {

	private Long id;
	private String email;
	private String name;
	private String surname;
	private String type;

	public UserDTO(User u) {
		this(u.getId(), u.getEmail(), u.getName(), u.getSurname(), u.getType());
	}

	public UserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDTO(Long id, String email, String name, String surname, String type) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

}
