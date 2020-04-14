package io.ashimjk.template.mail;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.net.URL;

@SpringBootApplication
public class MailApp {

//    @Bean
//    public JavaMailSender getJavaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//
//        mailSender.setUsername("ashim");
//        mailSender.setPassword("ashim");
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//
//        return mailSender;
//    }

    public static void main(String[] args) {
        SpringApplication.run(MailApp.class, args);
    }

    @Component
    public static class MailRunner implements CommandLineRunner {

        @Autowired
        public JavaMailSender emailSender;

        @Override
        public void run(String... args) {
            String to = "ashim@gmail.com";
            String subject = "testing";
            String body = "hello world";

            sendSimpleMail(to, subject, body);

            URL resource = MailRunner.class.getResource("/application.yml");
            sendMessageWithAttachment(to, subject, body, resource.getFile());
        }

        private void sendSimpleMail(String to, String subject, String body) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            emailSender.send(message);
        }

        @SneakyThrows
        void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) {
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file
                    = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Invoice", file);

            emailSender.send(message);
        }
    }
}
