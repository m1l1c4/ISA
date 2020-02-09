package tim31.pswisa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim31.pswisa.dto.CheckUpTypeDTO;
import tim31.pswisa.dto.ClinicDTO;
import tim31.pswisa.model.CheckUpType;
import tim31.pswisa.model.Clinic;
import tim31.pswisa.model.ClinicAdministrator;
import tim31.pswisa.model.MedicalWorker;
import tim31.pswisa.model.User;
import tim31.pswisa.repository.CheckUpTypeRepository;

@Service
public class CheckUpTypeService {

	@Autowired
	private ClinicService clinicService;

	@Autowired
	private CheckUpTypeRepository checkUpTypeRepository;

	@Autowired
	private MedicalWorkerService medicalWorkerService;
	
	@Autowired
	private ClinicAdministratorService clinicAdministratorService;

	/**
	 * This method servers for getting all types of check-ups from database
	 * 
	 * @return - (List<Checkup>) This method returns all check-up types from
	 *         database
	 */
	public List<CheckUpType> findAll() {
		return checkUpTypeRepository.findAll();
	}

	/**
	 * This method servers for getting all types of check-ups from database
	 * 
	 * @param name - name of check-up type that has to be returned
	 * @return - (CheckUpType) This method returns one type of check-up
	 */
	public CheckUpType findOneByName(String name) {
		return checkUpTypeRepository.findOneByName(name);
	}

	public ArrayList<CheckUpTypeDTO> getAllTypes(User user) {
		ArrayList<CheckUpTypeDTO> retValue = new ArrayList<CheckUpTypeDTO>();
		ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
		if (clinicAdministrator != null) {
			Clinic clinic = clinicService.findOneById(clinicAdministrator.getClinic().getId());
			Set<CheckUpType> lista = clinic.getCheckUpTypes();
			for (CheckUpType c : lista) {
				retValue.add(new CheckUpTypeDTO(c));
			}
			return retValue;
		}
		return null;
	}

	/**
	 * This method servers for deleting type of check-up by name in clinic
	 * 
	 * @param name                - name of type that have to be deleted
	 * @param clinicAdministrator - logged clinicAdministrator to know clinic
	 * @return - (String) This method returns 'Obrisano' if type is deleted or '' if
	 *         that type doesn't exist in clinic
	 */
	public String deleteType(User user, String name) {
		ClinicAdministrator clinicAdministrator = clinicAdministratorService.findByUser(user.getId());
		if (clinicAdministrator != null) {
			Clinic clinic = clinicService.findOneById(clinicAdministrator.getClinic().getId());
			Set<CheckUpType> tipovi = clinic.getCheckUpTypes();
			for (CheckUpType t : tipovi) {
				if (t.getName().equals(name)) {
					Set<MedicalWorker>pomDoctors = medicalWorkerService.findAllByTipAndClinicId(name,clinic.getId());
					if(pomDoctors.size()>0) {
						return null;
					}
					clinic.getCheckUpTypes().remove(t);
					CheckUpType temp = findOneByName(name);
					temp.getClinics().remove(clinic);
					temp = saveTwo(temp);
					return "Obrisano";
				}
			}
			return "";
		}

		return "";
	}

	/**
	 * This method servers for saving new type of check-up in clinic
	 * 
	 * @param ct - check-up type that have to be saved
	 * @return - (CheckUpType) check-up if it is saved or null if already exist
	 */
	public CheckUpType save(CheckUpType ct) {
		List<CheckUpType> cek = checkUpTypeRepository.findAll();

		for (CheckUpType c : cek) {
			if (c.getName().equals(ct.getName())) {
				return null;
			}
		}
		return checkUpTypeRepository.save(ct);
	}

	/**
	 * This method servers for updating type of check-up in clinic
	 * 
	 * @param ct - check-up type that have to be updated if database
	 * @return - (CheckUpType) updated check-up type
	 */
	public CheckUpType saveTwo(CheckUpType ct) {
		return checkUpTypeRepository.save(ct);
	}

	/**
	 * @param c     - check-up type that have to be updated
	 * @param after - new name of check-up type
	 * @param price - new price of check-up
	 * @return - check-up if it is successfully updated or null if type with same
	 *         name already exists
	 */
	public CheckUpType update(CheckUpType c, String after, String price) {
		List<CheckUpType> allTypes = findAll();
		for (CheckUpType cek : allTypes) {
			if (cek.getName().equals(after)) {
				return null;
			}
		}
		c.setName(after);
		int pri = Integer.parseInt(price);
		c.setTypePrice(pri);
		return checkUpTypeRepository.save(c);
	}

	/**
	 * This method servers for adding new type of check-up in clinic
	 * 
	 * @param type                - check-up type that have to be added
	 * @param clinicAdministrator - logged clinic administrator
	 * @return - (CheckUpType) check-up if it is successfully added or null if type
	 *         with same name already exists
	 */
	public CheckUpType addType(CheckUpTypeDTO type, ClinicAdministrator clinicAdministrator) {
		CheckUpType tip = new CheckUpType();
		tip.setName(type.getName());
		tip.setTypePrice(type.getTypePrice());
		List<CheckUpType> allTypes = findAll();
		Clinic klinika = new Clinic();
		klinika = clinicService.findOneById(clinicAdministrator.getClinic().getId());
		int x = 0;
		for (CheckUpType t : klinika.getCheckUpTypes()) {
			if (t.getName().equals(tip.getName())) {
				x = 1;
			}
		}

		int o = 0;
		for (CheckUpType c : allTypes) {
			if (c.getName().equals(type.getName())) {
				o = 1;
			}
		}
		if (o == 0) {
			tip.setName(type.getName());
			tip = save(tip);
		}

		if (x == 0) {
			CheckUpType temp = findOneByName(type.getName());
			temp.getClinics().add(klinika);
			temp = save(temp);
			klinika.getCheckUpTypes().add(temp);
			try {
				klinika = clinicService.updateClinic(clinicAdministrator, new ClinicDTO(klinika));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return temp;
		} else
			return null;
	}

	public Set<Clinic> findClinics(String name) {
		CheckUpType type = checkUpTypeRepository.findOneByName(name);
		return type.getClinics();
	}
	
	/** ZA SADA NADJI RUCNO, A PITAJ KAKO ZA MEDJURELACIJU
	 * method for getting all checkup types in one clinic from database
	 * 
	 * @param id
	 * @return
	 */
	public List<CheckUpTypeDTO> findAllByClinicId(Long id) {
		Clinic cl = clinicService.findOneById(id);
		List<CheckUpTypeDTO> ret = new ArrayList<CheckUpTypeDTO>();
		for (CheckUpType type : cl.getCheckUpTypes()) {
			ret.add(new CheckUpTypeDTO(type));
		}		
		return ret;
	}

	/**
	 * creating list of checkup type dto list from all checkup types
	 * @param dbTypes
	 * @return
	 */
	public List<CheckUpTypeDTO> findAllOptimised() {
		List<CheckUpType> dbTypes = findAll();
		List<CheckUpTypeDTO> ret = new ArrayList<CheckUpTypeDTO>(dbTypes.size());
		for (CheckUpType checkUpType : dbTypes) {
			ret.add(new CheckUpTypeDTO(checkUpType));
		}	
		return ret;
	}

	
}
