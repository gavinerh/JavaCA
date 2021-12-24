package edu.nus.java_ca.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nus.java_ca.model.Department;
import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.Position;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.security.Hash;

@Service
public class SeedingImpl {
	
	@Autowired
	UserService uService;

	public void createSeedingUser() {
		for(int i=0; i<3; i++) {
			User u = new User();
			String email = Integer.toString(i);
			String name = "hello" + email;
			email += "@mail.com";
			u.setPassword(Hash.hashPassword("password"));
			u.setEmail(email); u.setFirstName(name); u.setLastName(name);
			u.setDepartment(Department.Production); u.setPosition(Position.Manager);
			Collection<LeaveBalance> lb = new ArrayList<LeaveBalance>();
			LeaveBalance lbAnnual = new LeaveBalance("annual", 18, u);
			LeaveBalance lbCompensation = new LeaveBalance("compensation", 0, u);
			LeaveBalance lbMedical = new LeaveBalance("medical", 60, u);
			
			lb.add(lbMedical);
			lb.add(lbCompensation);
			lb.add(lbAnnual);
			
			u.setLb(lb);
			uService.saveUser(u);
			
		}
		for(int i=0; i<20; i++) {
			User u = new User();
			String email = Integer.toString(i);
			String name = "hello" + email;
			email += "@mail.com";
			u.setEmail(email); u.setFirstName(name); u.setLastName(name);
			u.setDepartment(Department.Production); u.setPosition(Position.Staff);
			u.setPassword(Hash.hashPassword("password"));
			u.setApprovingOfficer(uService.findByUserEmail("1@mail.com"));
			Collection<LeaveBalance> lb = new ArrayList<LeaveBalance>();
			LeaveBalance lbAnnual = new LeaveBalance("annual", 18, u);
			LeaveBalance lbCompensation = new LeaveBalance("compensation", 0, u);
			LeaveBalance lbMedical = new LeaveBalance("medical", 60, u);

			lb.add(lbMedical);
			lb.add(lbCompensation);
			lb.add(lbAnnual);
			u.setLb(lb);
			uService.saveUser(u);
			
		}
		
	}
}
