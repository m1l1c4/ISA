package tim31.pswisa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim31.pswisa.dto.CodebookDTO;
import tim31.pswisa.model.Codebook;
import tim31.pswisa.repository.CodebookRepository;

@Service
public class CodebookService {

	@Autowired
	private CodebookRepository codebookRepository;

	public List<Codebook> findAll() {
		return codebookRepository.findAll();
	}

	/**
	 * Method for getting one code from the codebook
	 * @param code - code of the code that will be returned
	 * @return - (Codebook) This method returns code that is found
	 */
	public Codebook findOneByCode(String code) {
		return codebookRepository.findOneByCode(code);
	}

	/**
	 * Method for adding new code in the codebook
	 * @param c - code that will be added
	 * @return - (Codebook) This method returns code that is created
	 */
	public Codebook save(CodebookDTO c) {
		Codebook codebook = new Codebook();
		codebook.setName(c.getName());
		codebook.setCode(c.getCode());
		codebook.setType(c.getType());

		Codebook code = findOneByCode(codebook.getCode());
		if (code != null) {
			return null;
		}

		return codebookRepository.save(codebook);
	}

	/**
	 * Method for deleting code from codebook
	 * @param code - code of the code that will be deleted
	 * @return - (String) This method returns message about success of deleting code
	 */
	public void remove(String code) {
		codebookRepository.delete(findOneByCode(code));
	}
}