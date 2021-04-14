package ml.mixko.shopkoapi.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class SendMailTest {
    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping(path =  "/email")
    public Map<String, Object> mail() {
        System.out.println("Connected");
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("mixko@mixko.ml");
        msg.setTo("apisitmaneerat@gmail.com","apisitmaneerat@hotmail.co.th");

        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");

        javaMailSender.send(msg);
        return null;
    }
}
