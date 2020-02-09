package tim31.pswisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tim31.pswisa.model.MedicalRecord;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

	/**
	 * Method for getting one medical record by id in the database
	 * @param id - id of the medical record from the database
	 * @return - (MedicalRecors) found medical record
	 */
	MedicalRecord findOneById(Long id);
	
	MedicalRecord findOneByPatientId(Long id);
}
