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

import com.example.resumegenerator.model.User;
import com.example.resumegenerator.model.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins="http://localhost:8081")
@RequestMapping("/api")
@RestController
public class UserController {

	@Autowired
	UserRepository userRepo;
	
	@GetMapping("/users/list")
	public ResponseEntity <List<User>> getAllUsers(@RequestParam(required=false) String firstname){
		try {
			List<User> users = new ArrayList<>();
			if(firstname == null) {
				userRepo.findAll().forEach(users::add);
			}else {
				userRepo.findByFirstName(firstname).forEach(users::add);
			}
			if(users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(users, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUser(@PathVariable long id){
		try {
			Optional <User> user = userRepo.findById(id);
			return new ResponseEntity<>(user.get(), HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/users/signup")
	public ResponseEntity<User> signUpUser(@RequestBody User user){
		try {
			User newUser = userRepo.save(new User(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword()));
			return new ResponseEntity<>(newUser, HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/users/signin")
	public ResponseEntity<User> signInUser(@RequestBody User user){
		try {
			String userEmail = user.getEmail();
			String password = user.getPassword();
			User storedUserData = userRepo.findByEmail(userEmail).get(0);
			
			if(storedUserData.getPassword().equalsIgnoreCase(password)) {
				return new ResponseEntity<>(storedUserData, HttpStatus.OK);
			}else {
				return new ResponseEntity<>(null, HttpStatus.OK);
			}	
			
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/users/update/{id}")
	public ResponseEntity<User> updateUser(@RequestBody User newUser, @PathVariable long id){
		try {
			Optional <User> user = userRepo.findById(id);
			if(user == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			User newUserData = user.get();
			newUserData.setFirstName(newUser.getFirstName());
			newUserData.setLastName(newUser.getLastName());
			newUserData.setEmail(newUser.getEmail());
			newUserData.setPassword(newUser.getPassword());
			userRepo.save(newUserData);
			return new ResponseEntity<>(newUserData, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/users/delete/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable long id){
		try {
			Optional <User> user = userRepo.findById(id);
			if(user == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			userRepo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	//to be changed from User to UserAuthRequest object
	//which is a class of only user email and password
    @PostMapping("/users/loginuser")
    public ResponseEntity<User> login(@RequestBody User user, HttpSession session) {
        // Check if the user is authenticated
    	User userDBData = userRepo.findByEmail(user.getEmail()).get(0);
    	if(userDBData==null) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
    	if (user.getEmail().equals(userDBData.getEmail()) && user.getPassword().equals(userDBData.getPassword())) {
            // Set user as authenticated by adding user object to the session            
            session.setAttribute("userEmail", user.getEmail());
        }
    	return new ResponseEntity<>(HttpStatus.OK);
       
    }

    
    @PostMapping("/users/logoutuser")
    public ResponseEntity<User> logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

	
}
