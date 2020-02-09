package tim31.pswisa.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import tim31.pswisa.dto.CheckupDTO;

@Component
@Aspect
public class SendingEmailAspect {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private Environment env;
	
	/*@Autowired
	private ClinicAdministratorService clinicAdministratorService;*/
	
	@AfterReturning(pointcut = "execution(* tim31.pswisa.service.CheckUpService.checkupToAdmin(..)) && args(ch,email,..)",
			returning = "result")
	public void sampleAdviceReturning(JoinPoint joinPoint, CheckupDTO ch, String email, boolean result) throws Throwable {
		SimpleMailMessage msg = new SimpleMailMessage();
		
			msg.setTo("pswisa.tim31.2019@gmail.com");		// for testing purposes all emails are sent to this address
			msg.setFrom(env.getProperty("spring.mail.username"));
			msg.setSubject("Zahtev za zakazivanje pregleda");
			String content = "Primili ste zahtev za zakazivanje pregleda.\n" ;
			content+="Informacije o pregledu:\n" ;
			//content+="Pacijent: " + ch.getPatient().getUser().getName() + " " + ch.getPatient().getUser().getSurname();
			content+="Doktor: " + ch.getMedicalWorker().getUser().getName() + " " +  ch.getMedicalWorker().getUser().getSurname() + "\n";
			content+="Tip pregleda: " + ch.getType() + "\n";
			content+="Datum i vreme: " + ch.getDate() + " " + ch.getTime() + "h\n";
			msg.setText(content);		
			javaMailSender.send(msg);
		

	}
}
