package tim31.pswisa.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim31.pswisa.model.Checkup;
import tim31.pswisa.model.Room;
import tim31.pswisa.repository.RoomRepository;

@Service
@Transactional(readOnly = true)
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;

  
	@Autowired
	private CheckUpService checkUpService;

	/**
	 * This method servers for getting all room from database
	 * 
	 * @return - (List<Room>) This method returns all rooms
	 */
	public List<Room> findAll() {
		return roomRepository.findAll();
	}

	/**
	 * This method servers for getting all rooms in clinic
	 * 
	 * @param id - id of clinic
	 * @return - (List<Room>) This method returns all rooms in clinic
	 */
	public List<Room> findAllByClinicId(Long id) {
		return roomRepository.findAllByClinicId(id);
	}

	/**
	 * This method servers for getting one room by id
	 * 
	 * @param id - id of room
	 * @return - (Room) This method returns one room
	 */
	public Room findOneById(Long id) {
		return roomRepository.findOneById(id);
	}

	/**
	 * This method servers for saving room in clinic
	 * 
	 * @param room - room that has to be saved
	 * @return - (Room) This method returns saved room
	 */
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public Room save(Room room) {
		return roomRepository.save(room);
	}

	/**
	 * This method servers for updating room in clinic
	 * 
	 * @param room - room that has to be updated
	 * @return - (Room) This method returns updated room
	 */
	@Transactional(readOnly = false)
	public Room update(Room ct) {
		return roomRepository.save(ct);
	}

	/**
	 * This method servers for saving room by clinic and room number
	 * 
	 * @param clinicId - id of clinic where room is
	 * @param number   - the number of room
	 * @return - (Room) This method returns searched room
	 */
	public Room findOneByClinicAndNumber(Long clinicId, int number) {
		return roomRepository.findOneByClinicIdAndNumber(clinicId, number);
	}

	/**
	 * Method returns all available rooms for date of the check-up from one clinic with the chosen type of check-up (operation/appointment)
	 * @param id - id of the check-up in database
	 * @return - (List<RoomDTO>) This method returns rooms in clinic by type of the check-up
	 */
	public List<Room> findAllByClinicIdAndTypeRoom(Long id) {
		Checkup checkup = checkUpService.findOneById(id);
		List<Room> rooms = roomRepository.findAllByClinicIdAndTipRoom(checkup.getClinic().getId(), checkup.getTip());
		List<Room> ret = new ArrayList<>();
		for (Room room : rooms) {
			LocalDate date = checkup.getDate();
			boolean found = false;
			while (!found) {
				List<Checkup> checkups = checkUpService.findAllByRoomIdAndScheduledAndDate(room.getId(), true, date);
				if (checkups == null || checkups.size() < 13) {
					found = true;
					room.setFirstFreeDate(date);
					ret.add(room);
				}
				date = date.plusDays(1);
			}
		}
		return ret;
	}

	/**
	 * Method returns all available terms of the room for selected date and time
	 * @param id - id of the room in database
	 * @return - (ArrayList<Integer>) This method returns list of the available terms
	 */
	public ArrayList<Integer> findRoomAvailability(Long id, String date) {
		List<Checkup> checkups = checkUpService.findAllByRoomIdAndScheduledAndDate(id, true, LocalDate.parse(date));
		ArrayList<Integer> ret = new ArrayList<>();
		ArrayList<Integer> temp = new ArrayList<>();
		for (Checkup checkup : checkups) {
			temp.add(Integer.parseInt(checkup.getTime()));
		}
		for (int i = 8; i < 21; i++) {
			if (!temp.contains(i)) {
				ret.add(i);
			}
		}
		return ret;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public Room myFindOne(Long id) {
		return roomRepository.myfindOne(id);
	}
}
