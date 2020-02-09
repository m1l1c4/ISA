package tim31.pswisa.dto;

import java.time.LocalDate;

/**
 * made for transfering diagnose info when trying to get medical record of a patient
 * @author Ivana
 *
 */
public class DiagnoseDTO {
	private String name;	
	private LocalDate diagnostingDate;
	private String doctorName;
	private String doctorSurname;
	private String clinicName;	
	
	public DiagnoseDTO(String name, LocalDate diagnostingDate, String doctorName,
			String doctorSurname, String clinicName) {
		super();
		this.name = name;
		this.diagnostingDate = diagnostingDate;
		this.doctorName = doctorName;
		this.doctorSurname = doctorSurname;
		this.clinicName = clinicName;
	}
	public DiagnoseDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getDiagnostingDate() {
		return diagnostingDate;
	}
	public void setDiagnostingDate(LocalDate diagnostingDate) {
		this.diagnostingDate = diagnostingDate;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getDoctorSurname() {
		return doctorSurname;
	}
	public void setDoctorSurname(String doctorSurname) {
		this.doctorSurname = doctorSurname;
	}
	public String getClinicName() {
		return clinicName;
	}
	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}
	
	
}
