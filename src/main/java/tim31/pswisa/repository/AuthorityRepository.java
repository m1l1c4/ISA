package tim31.pswisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tim31.pswisa.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	Authority findByName(String name);
}
