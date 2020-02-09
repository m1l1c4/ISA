package tim31.pswisa.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tim31.pswisa.dto.CheckupDTO;
import tim31.pswisa.model.Checkup;

public interface CheckUpRepository extends JpaRepository<Checkup, Long> {

	/**
	 * This method servers for finding all check-ups by scheduled, medical worker id and date
	 * 
	 * @param scheduled - is appointment or operation scheduled
	 * @param id       - id of medical worker
	 * @param date  - date of check-ups
	 * @return - (Set<CheckupDTO>) This method returns set of check-up by sent parameters
	 * 
	 */
	@Query("select c from Checkup c inner join c.doctors d where d.id = :id and c.scheduled = :scheduled and c.date = :date")
	Set<CheckupDTO> findAllByScheduledAndMedicalWorkerIdAndDate(@Param("scheduled") boolean scheduled, @Param("id") Long id, @Param("date") LocalDate date);
	
	/**
	 * This method servers for finding all check-ups by scheduled, medical worker id and finished parametar
	 * 
	 * @param scheduled - is appointment or operation scheduled
	 * @param id       - id of medical worker
	 * @param finished  - is appointment or operation finished or not
	 * @return - (Set<CheckupDTO>) This method returns set of check-up by sent parameters
	 * 
	 */
	@Query("select c from Checkup c inner join c.doctors d where d.id = :id and c.scheduled = :scheduled and c.finished = :finished")
	Set<CheckupDTO> findAllByScheduledAndMedicalWorkerIdAndFinished(@Param("scheduled") boolean scheduled, @Param("id") Long id, @Param("finished") boolean finished);

	/**
	 * This method servers for finding all check-ups by scheduled, medical worker id and finished parametar
	 * 
	 * @param roomId - id of room
	 * @param time       - time of check-up
	 * @param date  - date of check-up
	 * @return - (Check-up) This method one check-up by sent parameters
	 * 
	 */
	Checkup findOneByRoomIdAndTimeAndDate(Long roomId, String time, LocalDate date);
	/**
	 * This method servers for getting all check-ups from database by room id
	 * 
	 * @return - (Set<Checkup>) This method returns set of check-ups by room id
	 *         where the check-up was/is examined
	 */
	Set<Checkup> findAllByRoomId(Long id);

	/**
	 * This method servers for getting all check-ups from database
	 * 
	 * @return - (List<Checkup>) This method returns list of check-ups from database
	 */
	List<Checkup> findAll();

	/**
	 * This method servers for getting all check-ups in one clinic
	 * 
	 * @param id - clinic id
	 * @return - (List<Checkup>) This method returns list of check-ups in one clinic
	 */
	List<Checkup> findAllByClinicId(Long id);

	/**
	 * This method servers for getting one check-up by id
	 * 
	 * @param id - check-up id that has to be returned
	 * @return - (Checkup) This method returns one check-up
	 */
	Checkup findOneById(@Param("id")Long id);


	/**
	 * This method servers for getting all check-ups by room id and time and date of
	 * operation or appointment
	 * 
	 * @param id   - id of room
	 * @param time - time of appointment or operation
	 * @param date - date of appointment or operation
	 * @return - (List<Checkup>) This method returns list of check-ups by id, time
	 *         and date
	 */
	List<Checkup> findAllByRoomIdAndTimeAndDate(Long id, String time, LocalDate date);

	/**
	 * This method servers for getting all check-ups by scheduled
	 * 
	 * @param ok - is/not scheduled
	 * @return - (List<Checkup>) This method returns list of check-ups by param ok
	 */
	List<Checkup> findAllByScheduled(boolean ok);

	/**
	 * Method for finding all check-ups by room id, scheduled and date
	 * 
	 * @param id        - key of the room
	 * @param scheduled - is scheduled
	 * @param date      - date of the check-ups
	 * @return - (List<Checkup>) This method returns list of found check-ups
	 */
	List<Checkup> findAllByRoomIdAndScheduledAndDate(Long id, boolean scheduled, LocalDate date);

	/**
	 * Method for finding all check-ups by room id, scheduled and date
	 * 
	 * @param time - time of the defined check-up
	 * @param date - date of the check-ups
	 * @return - (List<Checkup>) This method returns list of found check-ups
	 */
	List<Checkup> findAllByTimeAndDate(String time, LocalDate date);

	/**
	 * Method for finding all check-ups by room id, scheduled and date
	 * 
	 * @param scheduled - is scheduled
	 * @param date      - date of the check-ups
	 * @param id        - key of the patient
	 * @param type      - type of the check-up (operation/appointment)
	 * @return - (List<Checkup>) This method returns list of found check-ups
	 */
	List<Checkup> findAllByScheduledAndDateAndPatientIdAndTip(boolean scheduled, LocalDate date, Long id, String type);

	/**
	 * gets from database all checkups where type is "PREGLED" from one patient and are finished
	 * @param finished - if checkup is finished or not
	 * @param id - patient id
	 * @param type - type of checkup
	 * @return - (List<Checkup>) This method returns list of found check-ups
	 */
	List<Checkup> findAllByFinishedAndPatientIdAndTip(boolean finished, Long id, String type);
	
	/**
	 * gets from database all scheduled checkups from one clinic
	 * @param scheduled - if checkup is scheduled or not
	 * @param clinicId - id of the clinic
	 * @return - (List<Checkup>) This method returns list of found check-ups
	 */
	List<Checkup> findAllByScheduledAndClinicId(boolean scheduled, Long clinicId);
}
