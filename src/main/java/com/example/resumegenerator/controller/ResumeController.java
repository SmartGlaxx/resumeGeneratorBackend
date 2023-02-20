package com.example.resumegenerator.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.resumegenerator.model.Resume;
import com.example.resumegenerator.model.ResumeRepository;
import com.example.resumegenerator.model.User;

import jakarta.persistence.Column;

@CrossOrigin(origins="http://localhost:8081")
@RequestMapping("/resumes")
@RestController
public class ResumeController {

	@Autowired
	ResumeRepository resumeRepo;
	
	
	@GetMapping("/list")
	public ResponseEntity <List<Resume>> getResumesByExperience(@RequestParam(required=false) String experience){
		try {
			List<Resume> resumes = new ArrayList<>();
			if(experience == null) {
				resumeRepo.findAll().forEach(resumes::add);
			}else {
				resumeRepo.findByExperienceContaining(experience).forEach(resumes::add);
			}
			if(resumes.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(resumes, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/create")
	public ResponseEntity<Resume> createResume(@RequestBody Resume resume){
		try {
			Resume newResume = resumeRepo.save(new Resume(resume.getUserId(), resume.getFirstName(), 
					resume.getLastName(), resume.getEmail(), resume.getAddress(),
					resume.getPhone(), resume.getIntro(), resume.getExperience(),
					resume.getEducation(), resume.getSkills()));
			return new ResponseEntity<>(newResume, HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/list/{id}")
	public ResponseEntity<List<Resume>> getAllUserResumes(@PathVariable String id){
		try {
			List<Resume> resumes = new ArrayList<Resume>();
			resumeRepo.findByUserId(id).forEach(resumes::add);
			return new ResponseEntity<>(resumes, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/list/{userId}/{resumeId}")
	public ResponseEntity<Resume> getAUserResume(@PathVariable String userId, @PathVariable long resumeId){
		try {
			Optional<Resume> resume = resumeRepo.findById(resumeId);
			if(resume.get().getUserId().equalsIgnoreCase(userId)) {
				return new ResponseEntity<>(resume.get(), HttpStatus.OK);	
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update/{userId}/{resumeId}")
	public ResponseEntity<Resume> updateResume(@RequestBody Resume newResume, @PathVariable String userId, @PathVariable long resumeId){
		try {
			Optional <Resume> resume = resumeRepo.findById(resumeId);
			if(resume.get().getUserId().equalsIgnoreCase(userId)) {
				Resume newResumeData = resume.get();
				newResumeData.setUserId(userId);
				newResumeData.setFirstName(newResume.getFirstName());
				newResumeData.setLastName(newResume.getLastName());
				newResumeData.setEmail(newResume.getEmail());
				newResumeData.setPhone(newResume.getPhone());
				newResumeData.setIntro(newResume.getIntro());
				newResumeData.setExperience(newResume.getExperience());
				newResumeData.setEducation(newResume.getEducation());
				newResumeData.setSkills(newResume.getSkills());
				resumeRepo.save(newResumeData);
				return new ResponseEntity<>(newResumeData, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/delete/{userId}/{resumeId}")
	public ResponseEntity<Resume> deleteUser(@PathVariable String userId, @PathVariable long resumeId){
		try {
			Optional <Resume> resume = resumeRepo.findById(resumeId);
			if(resume == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			if(resume.get().getUserId().equalsIgnoreCase(userId)) {
				resumeRepo.deleteById(resumeId);				
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	
	
}
