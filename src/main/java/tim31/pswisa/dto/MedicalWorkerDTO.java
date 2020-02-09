package tim31.pswisa.dto;

import java.util.HashMap;
import java.util.List;

import tim31.pswisa.model.MedicalWorker;

public class MedicalWorkerDTO {

	private Long id;
	private UserDTO user;
	private String phone;
	private double rating;
	private int startHr;
	private int endHr;
	private String type;
	private ClinicDTO clinic;
	private HashMap<String, List<String>> availableCheckups = new HashMap<String, List<String>>();

	public MedicalWorkerDTO(MedicalWorker mw) {
		this(mw.getId(), new UserDTO(mw.getUser()), mw.getPhone(), mw.getRating(), mw.getStartHr(), mw.getEndHr(),
				mw.getType(), new ClinicDTO(mw.getClinic()));
	}

	public MedicalWorkerDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MedicalWorkerDTO(Long id, UserDTO user, String phone, double rating, int startHr, int endHr, String type,
			ClinicDTO clinic) {
		super();
		this.id = id;
		this.user = user;
		this.phone = phone;
		this.rating = rating;
		this.startHr = startHr;
		this.endHr = endHr;
		this.type = type;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HashMap<String, List<String>> getAvailableCheckups() {
		return availableCheckups;
	}

	public void setAvailableCheckups(HashMap<String, List<String>> availableCheckups) {
		this.availableCheckups = availableCheckups;
	}

	public ClinicDTO getClinic() {
		return clinic;
	}

	public void setClinic(ClinicDTO clinic) {
		this.clinic = clinic;
	}

	

}
