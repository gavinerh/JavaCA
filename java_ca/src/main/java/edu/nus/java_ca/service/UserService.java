package edu.nus.java_ca.service;

import java.util.ArrayList;
import java.util.List;

import edu.nus.java_ca.model.Position;
import edu.nus.java_ca.model.User;

public interface UserService {
	List<User> findAll();
	User findByUserEmail(String email);
	void saveUser(User user);
	User findByUserId(Long id);
	void deleteUser(User user);
	List<User> findByPosition(Position position);
	void deleteUserById(Long id);
	List<User> findAllWithDeleted();
	
	boolean checkDuplicatesEmail(String email);
	
}
