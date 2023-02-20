package com.example.resumegenerator;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.resumegenerator.model.Resume;
import com.example.resumegenerator.model.ResumeRepository;
import com.example.resumegenerator.model.User;
import com.example.resumegenerator.model.UserRepository;

@SpringBootApplication
public class ResumegeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumegeneratorApplication.class, args);
	}

//	@Bean
//	ApplicationRunner init(UserRepository userRepo) {
//		return arg -> {
//			userRepo.save(new User("Smart", "Egbuchulem" , "smart@gmail.com", "12345"));
////			userRepo.save(new User("Uche", "Xavier" , "smart@gmail.com", "12345"));
////			userRepo.save(new User("Glass", "Joe" , "smart@gmail.com", "12345"));
//			userRepo.findAll().forEach(System.out::print);
//		};
//	}
//	@Bean
//	ApplicationRunner init(ResumeRepository resumeRepo) {
//		return arg -> {
//			resumeRepo.save(new Resume("3", "Egbuchulem" ));
////			userRepo.save(new User("Uche", "Xavier" , "smart@gmail.com", "12345"));
////			userRepo.save(new User("Glass", "Joe" , "smart@gmail.com", "12345"));
//			resumeRepo.findAll().forEach(System.out::print);
//		};
//	}
	
	
}
