package com.electronics.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.electronics.model.User;

@Repository
@Transactional
public interface UserRepo extends JpaRepository<User,Integer> {

	// Get List of All Users
	@Query(value = "SELECT * FROM user_table ORDER BY first_name ASC", nativeQuery = true)
	public List<User> getAllUsers();
	
	// Get List of Inactive Users
	@Query(value = "SELECT * FROM user_table WHERE emp_status = false ORDER BY first_name ASC", nativeQuery = true)
	public List<User> getInactiveUsers();
	
	// Get List of Active Users
	@Query(value = "SELECT * FROM user_table WHERE emp_status = true ORDER BY first_name ASC", nativeQuery = true)
	public List<User> getActiveUsers();

	// Get User by userID
	@Query(value = "SELECT * FROM user_table WHERE user_id = ?", nativeQuery = true)
	public User getUserById(int userId);

	// Get User by firstName (or if contains part of the name)
	@Query(value = "SELECT * FROM user_table WHERE LOWER(first_name) LIKE %?% ORDER BY first_name ASC", nativeQuery = true)
	public List<User> getUserByName(String firstName);

	// Get User by userName - Active Only
	@Query(value = "SELECT * FROM user_table WHERE LOWER(email) = ? AND emp_status = true", nativeQuery = true)
	public User getUserByUsername(String email);

	// Change Password by userName
	@Modifying
	@Query(value = "UPDATE user_table SET passwd = ? WHERE email = ?", nativeQuery = true)
	public void changePassword(String password, String email);

	// Delete User by userID
	@Modifying
	@Query(value = "DELETE FROM user_table WHERE user_id = ?", nativeQuery = true)
	public void deleteUser(int userId);

}
