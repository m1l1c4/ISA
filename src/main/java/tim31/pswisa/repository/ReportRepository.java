package tim31.pswisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tim31.pswisa.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

	/**
	 * Method for getting one report by id
	 * @param id - id of the report in the database
	 * @return - (report) This method returns found report
	 */
	Report findOneById(Long id);
}
