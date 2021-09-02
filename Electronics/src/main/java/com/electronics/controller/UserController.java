package com.electronics.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.electronics.model.AccessGroup;
import com.electronics.model.User;
import com.electronics.model.UserInfo;
import com.electronics.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "user")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@AllArgsConstructor
public class UserController {

	private UserService userService;
	public static String uName;
	public static String uEmail;

	// ADD NEW USER
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping(value = "addNewUser.iwh")
	public void addUser(HttpServletRequest req, @RequestBody User user) {
		userService.addUser(user);
	}
	
	// GET ALL USERS
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "listAllUsers.iwh")
	public List<User> getAllUsers(HttpServletRequest req) {
		return userService.getAllUsers();
	}
	
	// GET INACTIVE USERS
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "listInactiveUsers.iwh")
	public List<User> getInactiveUsers(HttpServletRequest req) {
		return userService.getInactiveUsers();
	}
	
	// GET ACTIVE USERS
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "listActiveUsers.iwh")
	public List<User> getActiveUsers(HttpServletRequest req) {
		return userService.getActiveUsers();
	}	
	
	// GET USER BY userID
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "getUser.iwh:Id={userId}")
	public User getUserById(HttpServletRequest req, @PathVariable int userId) {
		return userService.getUserById(userId);
	}
	
	// GET USER BY firstNAME (OR CONTAINS PART OF THE NAME)
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "getUser.iwh:firstName={firstName}")
	public List<User> getUserByName(HttpServletRequest req, @PathVariable String firstName) {
		return userService.getUserByName(firstName);
	}
	
	// USER LOGIN
	@GetMapping(value = "userLogin.iwh:Username={username}&Password={password}")
	public UserInfo userLogin(HttpServletRequest req, HttpServletResponse resp, @PathVariable String username, @PathVariable String password) {
		User user = userService.getUserByUsername(username);
		UserInfo userInfo = new UserInfo();
		
		if(userService.userLogin(username, password) & user != null & user.getEmpStatus()) {
			uName = user.getFirstName() + " " + user.getLastName();
			uEmail = user.getEmail();
			req.getSession().setAttribute("userFullName", uName);
			resp.setStatus(200);
			//System.out.println(req.getSession().getAttribute("userFullName") != null);  //TRUE
			userInfo.setUserId(user.getUserId());
			userInfo.setFullName(user.getFirstName() + " " + user.getLastName());
			userInfo.setEmail(user.getEmail());
			userInfo.setJobTitle(user.getJobTitle());
		} else {
			resp.setStatus(401);
		}
		
		return userInfo;
	}
	
	// USER LOGOUT
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "userLogout.iwh")
	public void userLogout(HttpServletRequest req, HttpServletResponse resp) {
		req.getSession().invalidate();
		
		uName = null;
		uEmail = null;
		req.getSession().setAttribute("userFullName", uName);
		//System.out.println(req.getSession().getAttribute("userFullName") != null);  //FALSE
	}
	
	// GET USER AUTHORIZATION/PERMISSIONS
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "getUserPermissions.iwh:Username={username}")
	public AccessGroup authUser(HttpServletRequest req, @PathVariable String username) {
		AccessGroup group = userService.getUserByUsername(username).getAccessGroup();
		return group;
	}
	
	// CHANGE PASSWORD BY userID
	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping(value = "changePassword.iwh:Username={username}&Password={password}")
	public void changePassword(HttpServletRequest req, @PathVariable String password, @PathVariable String username) {
		userService.changePassword(password, username);
	}
	
	// UPDATE USER
	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping(value = "updateUser.iwh:Id={userId}")
	public void updateUser(HttpServletRequest req, @RequestBody User user, @PathVariable int userId) {
		userService.updateUser(user);
	}
	
	// DELETE USER By userID
	@ResponseStatus(value = HttpStatus.OK)
	@DeleteMapping(value = "deleteUser.iwh:Id={userId}")
	public void deleteUser(HttpServletRequest req, @PathVariable int userId) {
		userService.deleteUser(userId);
	}

}
