package br.com.goes.analyzer;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@EnableAutoConfiguration
@SpringBootApplication
public class AnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalyzerApplication.class, args);
	}

	@Value("${spring.mail.host}")
	private String EMAIL_HOST;
	@Value("${spring.mail.username}")
	private String EMAIL_USERNAME;
	@Value("${spring.mail.password}")
	private String EMAIL_PASSWORD;
	@Value("${spring.mail.port}")
	private int EMAIL_PORT;
	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost(EMAIL_HOST);
	    mailSender.setPort(EMAIL_PORT);
	    
	    mailSender.setUsername(EMAIL_USERNAME);
	    mailSender.setPassword(EMAIL_PASSWORD);
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    
	    return mailSender;
	}
}
