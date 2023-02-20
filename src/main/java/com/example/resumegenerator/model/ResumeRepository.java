package com.example.resumegenerator.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
	List<Resume> findByEmail(String email);
	List<Resume> findByFirstName(String firstName);
	List<Resume> findByUserId(String id);
	List<Resume> findByExperienceContaining(String experience);
}
