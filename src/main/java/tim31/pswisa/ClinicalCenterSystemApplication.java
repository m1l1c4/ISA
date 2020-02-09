package tim31.pswisa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ClinicalCenterSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicalCenterSystemApplication.class, args);
	}

}
