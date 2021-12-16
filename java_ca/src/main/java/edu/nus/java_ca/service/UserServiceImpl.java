package edu.nus.java_ca.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nus.java_ca.model.User;
import edu.nus.java_ca.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository uRepo;
	
	@Override
	public List<User> findAll() {
		return uRepo.findAll();
	}

	@Override
	public User findByUserEmail(String email) {
		List<User> result = new ArrayList<User>();
		result.addAll(uRepo.findByUserEmail(email));
		if(result.isEmpty() || result == null) {
			return null;
		}
		return result.get(0);
	}

	@Override
	public void saveUser(User user) {
		uRepo.saveAndFlush(user);
	}
	

}