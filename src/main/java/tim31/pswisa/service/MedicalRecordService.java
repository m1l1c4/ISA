package tim31.pswisa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim31.pswisa.dto.MedicalRecordDTO;
import tim31.pswisa.model.MedicalRecord;
import tim31.pswisa.model.Patient;
import tim31.pswisa.repository.MedicalRecordRepository;

@Service
public class MedicalRecordService {

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	/**
	 * Method for creating new medical record for the new registered patient
	 * @param p - patient that has been registered
	 * @return - (MedicalRecors) created medical record
	 */
	public MedicalRecord add(Patient p) {
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setPatient(p);
		return medicalRecordRepository.save(medicalRecord);
	}

	/**
	 * Method for getting one medical record by id in the database
	 * @param id - id of the medical record from the database
	 * @return - (MedicalRecor) Method returns updated medical record
	 */
	public MedicalRecord findOneById(Long id) {
		return medicalRecordRepository.findOneById(id);
	}
	
	/**
	 * Method for changing data in medical record
	 * @param mr - medical record with the new data
	 * @return - (MedicalRecor) Method returns updated medical record
	 */
	public MedicalRecord update(MedicalRecordDTO mr) {
		MedicalRecord medicalRecord = medicalRecordRepository.findOneById(mr.getId());
		medicalRecord.setBloodType(mr.getBloodType());
		medicalRecord.setDiopter(mr.getDiopter());
		medicalRecord.setHeight(mr.getHeight());
		medicalRecord.setWeight(mr.getWeight());
		return medicalRecordRepository.save(medicalRecord);
	}
	
	public MedicalRecord findOneByPatientId(Long id) {
		return medicalRecordRepository.findOneByPatientId(id);
	}

}
