package tim31.pswisa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tim31.pswisa.model.Authority;
import tim31.pswisa.model.Patient;
import tim31.pswisa.model.User;
import tim31.pswisa.repository.PatientRepository;
import tim31.pswisa.repository.UserRepository;

@Service
public class LoggingService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PatientRepository patientRepo;

	@Autowired
	private AuthorityService authService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Patient registerUser(Patient p) {
		User user = (User) loadUserByUsername(p.getUser().getEmail());

		if (user != null) // there is already user with that email
			return null;
		else {
			p.getUser().setPassword(passwordEncoder.encode(p.getUser().getPassword()));
			p.getUser().setEnabled(true);
			p.getUser().setActivated(false);
			p.getUser().setType("PACIJENT");
			List<Authority> auth = authService.findByname("PACIJENT");
			p.getUser().setAuthorities(auth);
			p.setProcessed(false);
			patientRepo.save(p);
		}

		return p;

	}

	public User loginUser(User u) {
		User user = (User) loadUserByUsername(u.getEmail());
		if (user == null)
			return null;
		if (user != null && user.getType().equals("PACIJENT") /* && user.getActivated() */)
			return user;
		else if (!user.getFirstLogin() && !user.getType().equals("PACIJENT")) {
			// user.setFirstLogin(true);
			return user;
		} else if (!user.getType().equals("PACIJENT"))
			return user;
		else
			return null;
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		User user = userRepo.findOneByEmail(email);
		/*
		 * if (user == null) { throw new
		 * UsernameNotFoundException(String.format("No user found with email '%s'.",
		 * email)); }
		 */
		return user;

	}	

	/**
	 * method for getting user role according to email of logged user
	 * @param email
	 * @return
	 */
	public String getRole(String email) {
		User loggedUser =userRepo.findOneByEmail(email);
		if (loggedUser != null) {
			return loggedUser.getType();	// what if 
		} else {
			return "NONEXISTENT";			//in case of empthy string in user.getType()
		}
	}
}
