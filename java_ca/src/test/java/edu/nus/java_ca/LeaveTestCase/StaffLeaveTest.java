package edu.nus.java_ca.LeaveTestCase;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.nus.java_ca.JavaCaApplication;
import edu.nus.java_ca.model.*;
import edu.nus.java_ca.repository.LeaveBalanceRepo;
import edu.nus.java_ca.repository.UserRepository;
import edu.nus.java_ca.service.LeaveBalanceService;
import edu.nus.java_ca.service.LeaveService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JavaCaApplication.class)
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StaffLeaveTest {
	
	@Autowired
	LeaveBalanceService lbservice;
	@Autowired
	LeaveService lservice;
	@Autowired
	UserRepository urepo;
	@Autowired
	LeaveBalanceRepo lbrepo;
	
	@Test
	@Order(1)
	public void testSubmitLeave(){
		List<User> u = urepo.findAll();
		ArrayList<LeaveBalance> ll = new ArrayList<LeaveBalance>();
		for(User us:u) {
			LeaveBalance lb = new LeaveBalance(0,"Child Care Leave",7,us);
			ll.add(lb);
		}
		
		lbrepo.saveAllAndFlush(ll);
		User us = urepo.findByUserId((long)14);
//		LeaveBalance lb = new LeaveBalance(10,"ChildCare Leave",7,us);
//		lbrepo.saveAndFlush(lb);
		us.getLb().forEach(System.out::println);
	}

}
