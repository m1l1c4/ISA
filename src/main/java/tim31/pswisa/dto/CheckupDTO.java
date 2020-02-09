package tim31.pswisa.dto;

import java.time.LocalDate;

import tim31.pswisa.model.Checkup;
import tim31.pswisa.model.MedicalWorker;

public class CheckupDTO {

	private Long id;
	private double discount;
	private boolean scheduled;
	private LocalDate date;
	private String time;
	private String type; // appointment or operation
	private int duration;
	private double price;
	private RoomDTO room;
	private PatientDTO patient;
	private ClinicDTO clinic;
	private MedicalWorkerDTO medicalWorker;
	private CheckUpTypeDTO checkUpType;
	private boolean finished;

	public CheckupDTO(Checkup c) {
		this(c.getId(), c.getDiscount(), c.isScheduled(), c.getDate(), c.getTime(), c.getTip(), c.getDuration(),
				c.getDiscount(), null, null, new ClinicDTO(c.getClinic()), null, new CheckUpTypeDTO(c.getCheckUpType()),
				c.isFinished());

		if (c.getPatient() != null) {
			this.patient = new PatientDTO(c.getPatient());
		}
		if (c.getRoom() != null) {
			this.room = new RoomDTO(c.getRoom());
		}

		if (c.getRoom() != null) {
			this.room = new RoomDTO(c.getRoom());
		}

		if (c.getDoctors().size() > 0) {
			this.medicalWorker = new MedicalWorkerDTO((MedicalWorker) c.getDoctors().toArray()[0]);
		}

	}

	public CheckupDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CheckupDTO(Long id, double discount, boolean scheduled, LocalDate date, String time, String type,
			int duration, double price, RoomDTO room, PatientDTO patient, ClinicDTO clinic,
			MedicalWorkerDTO medicalWorker, CheckUpTypeDTO checkUpType, boolean finished) {
		super();
		this.id = id;
		this.discount = discount;
		this.scheduled = scheduled;
		this.date = date;
		this.time = time;
		this.type = type;
		this.duration = duration;
		this.price = price;
		this.room = room;
		this.patient = patient;
		this.clinic = clinic;
		this.medicalWorker = medicalWorker;
		this.checkUpType = checkUpType;
		this.finished = finished;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public boolean isScheduled() {
		return scheduled;
	}

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public RoomDTO getRoom() {
		return room;
	}

	public void setRoom(RoomDTO room) {
		this.room = room;
	}

	public PatientDTO getPatient() {
		return patient;
	}

	public void setPatient(PatientDTO patient) {
		this.patient = patient;
	}

	public ClinicDTO getClinic() {
		return clinic;
	}

	public void setClinic(ClinicDTO clinic) {
		this.clinic = clinic;
	}

	public MedicalWorkerDTO getMedicalWorker() {
		return medicalWorker;
	}

	public void setMedicalWorker(MedicalWorkerDTO medicalWorker) {
		this.medicalWorker = medicalWorker;
	}

	public CheckUpTypeDTO getCheckUpType() {
		return checkUpType;
	}

	public void setCheckUpType(CheckUpTypeDTO checkUpType) {
		this.checkUpType = checkUpType;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

}