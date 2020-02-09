package tim31.pswisa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tim31.pswisa.dto.UserDTO;
import tim31.pswisa.model.Authority;
import tim31.pswisa.model.ClinicalCenterAdministrator;
import tim31.pswisa.model.User;
import tim31.pswisa.repository.CCAdminRepository;
import tim31.pswisa.repository.UserRepository;

@Service
public class CCAdminService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CCAdminRepository ccadminRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityService authorityService;

	/**
	 * Method for creating new clinical center administrator
	 * @param u - information about user, credentials, name, surname,...
	 * @return - (ClinicalCenterAdministrator) This method returns created new clinical center administrator
	 */
	public ClinicalCenterAdministrator save(UserDTO u) {
		User user = userRepository.findOneByEmail(u.getEmail());
		if (user != null) {
			return null;
		}
		ClinicalCenterAdministrator admin = new ClinicalCenterAdministrator();
		user = new User();
		user = new User();
		user.setName(u.getName());
		user.setSurname(u.getSurname());
		user.setEmail(u.getEmail());
		user.setType(u.getType());
		admin.setUser(user);
		admin.getUser().setPassword(passwordEncoder.encode("admin"));
		admin.getUser().setFirstLogin(false);
		admin.getUser().setEnabled(true);
		admin.getUser().setActivated(true);
		List<Authority> auth = authorityService.findByname("CCADMIN");
		admin.getUser().setAuthorities(auth);

		return ccadminRepository.save(admin);
	}

}
