package tim31.pswisa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim31.pswisa.model.Authority;
import tim31.pswisa.repository.AuthorityRepository;

@Service
public class AuthorityService {

	@Autowired
	private AuthorityRepository authorityRepository;

	public List<Authority> findById(Long id) {
		Authority auth = this.authorityRepository.getOne(id);
		List<Authority> auths = new ArrayList<>();
		auths.add(auth);
		return auths;
	}

	public List<Authority> findByname(String name) {
		Authority auth = this.authorityRepository.findByName(name);
		List<Authority> auths = new ArrayList<>();
		auths.add(auth);
		return auths;
	}

}
