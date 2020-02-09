package tim31.pswisa.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim31.pswisa.dto.CodebookDTO;
import tim31.pswisa.model.Codebook;
import tim31.pswisa.service.CodebookService;

@RestController
@RequestMapping(value = "/codebook", produces = MediaType.APPLICATION_JSON_VALUE)
public class CodebookController {

	@Autowired
	private CodebookService codebookService;

	/**
	 * Method for adding new code in the codebook
	 * @param c - code that will be added
	 * @return - (String) This method returns message about success of adding new code
	 * 
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveCodebook(@RequestBody CodebookDTO c) {
		Codebook codebook = codebookService.save(c);
		if (codebook == null)
			return new ResponseEntity<>("Sifra mora biti jedinstvena.", HttpStatus.NOT_ACCEPTABLE);
		return new ResponseEntity<>("Uspjesno dodata nova stavka.", HttpStatus.CREATED);
	}

	/**
	 * Method for getting the whole codebook 
	 * @return - (List<CodebookDTO>) This method returns all codes from the codebook
	 */
	@GetMapping()
	public ResponseEntity<List<CodebookDTO>> getCodebook() {
		List<Codebook> codebook = codebookService.findAll();
		List<CodebookDTO> codebooksDTO = new ArrayList<CodebookDTO>();
		for (Codebook c : codebook) {
			codebooksDTO.add(new CodebookDTO(c));
		}
		return new ResponseEntity<>(codebooksDTO, HttpStatus.OK);
	}

	/**
	 * Method for deleting code from codebook
	 * @param code - code of the code that will be deleted
	 * @return - (String) This method returns message about success of deleting code
	 */
	@PostMapping(value = "/{code}")
	public ResponseEntity<String> deleteCode(@PathVariable String code) {
		codebookService.remove(code);
		return new ResponseEntity<>("Obrisano", HttpStatus.OK);
	}

}
