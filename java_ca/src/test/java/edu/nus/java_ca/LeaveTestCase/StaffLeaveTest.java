package edu.nus.java_ca.LeaveTestCase;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
import edu.nus.java_ca.service.UserService;

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
	UserService uservice;
	@Autowired
	LeaveBalanceRepo lbrepo;
	
	@Test
	@Order(1)
	public void testSubmitLeave(){
		List<User> u = uservice.findAll();
	ArrayList<LeaveBalance> ll = new ArrayList<LeaveBalance>();
	for(User us:u) {
		LeaveBalance lb = new LeaveBalance(0,"Child Care Leave",7,us);
			ll.add(lb);
	}
		
		lbrepo.saveAllAndFlush(ll);
		User us = uservice.findByUserId((long)11);
		LeaveBalance lb = new LeaveBalance("ChildCare Leave",7,us);
		LeaveBalance lb1 = new LeaveBalance("Annual",14,us);
		LeaveBalance lb2 = new LeaveBalance("Medical",60,us);
		us.addLeaveBalance(lb);
		us.addLeaveBalance(lb1);
		us.addLeaveBalance(lb2);
		/* uservice.saveUser(us); */
	/* LeaveBalance lb = lbrepo.findByUserAndLeavetype(us, "Annual"); */
		System.out.println(lb);
	us.getLb().forEach(System.out::println);
		LocalDate ld = LocalDate.of(2021, 11, 20);
		ArrayList<Object> holidays = new ArrayList<>(Arrays.asList(new LocalDate[] {LocalDate.of(2021, 11, 20),LocalDate.of(2021, 11, 21)}));		//lservice.addHoliday(LocalDate.of(2021, 11, 20));
		holidays.add(LocalDate.of(2022, 11, 1));
		holidays.forEach(System.out::println);
		System.out.println(holidays.contains(LocalDate.of(2022, 11, 1)));
		Long i = lservice.countLeaves(LocalDate.of(2021, 12, 21), LocalDate.of(2021, 12, 22));
		System.out.println(i);
	}

}
