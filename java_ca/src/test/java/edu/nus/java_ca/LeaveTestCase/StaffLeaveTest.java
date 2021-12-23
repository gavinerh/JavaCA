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
import java.util.stream.Collectors;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.nus.java_ca.JavaCaApplication;
import edu.nus.java_ca.model.*;
import edu.nus.java_ca.repository.HolidayRepo;
import edu.nus.java_ca.repository.LeaveBalanceRepo;
import edu.nus.java_ca.repository.UserRepository;
import edu.nus.java_ca.service.HolidayService;
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
	HolidayService hservice;
	@Autowired
	HolidayRepo hrepo;
	
	
	@Test
	@Order(1)
	public void testSubmitLeave(){
//		List<User> u = uservice.findAll();
//	ArrayList<LeaveBalance> ll = new ArrayList<LeaveBalance>();
//	for(User us:u) {
//		LeaveBalance lb = new LeaveBalance(0,"Child Care Leave",7,us);
//			ll.add(lb);
//	}
//		
//		lbrepo.saveAllAndFlush(ll);
//		User us = uservice.findByUserId((long)11);
//		LeaveBalance lb = new LeaveBalance("ChildCare Leave",7,us);
//		LeaveBalance lb1 = new LeaveBalance("Annual",14,us);
//		LeaveBalance lb2 = new LeaveBalance("Medical",60,us);
//		us.addLeaveBalance(lb);
//		us.addLeaveBalance(lb1);
//		us.addLeaveBalance(lb2);
//		/* uservice.saveUser(us); */
//	/* LeaveBalance lb = lbrepo.findByUserAndLeavetype(us, "Annual"); */
//		System.out.println(lb);
//	us.getLb().forEach(System.out::println);
//		LocalDate ld = LocalDate.of(2021, 11, 20);
//	    ArrayList<Holidays> holidays = new ArrayList<>(Arrays.asList(new Holidays[] {new Holidays(LocalDate.of(2021, 11, 20)),new Holidays(LocalDate.of(2021, 11, 21))}));		
//  
//		hservice.createHoliday(new Holidays(LocalDate.of(2021, 11, 20)));
//    	hrepo.saveAllAndFlush(holidays);
		
		User u = uservice.findByUserId((long)14);
		ArrayList<Leave> c = lservice.findByUser(u);
		ArrayList<Leave> le = c.stream()
				.filter(l-> !(l.getStatus().equals(LeaveStatus.DELETED)||
					l.getStatus().equals(LeaveStatus.CANCELLED)))
				.collect(Collectors
	                    .toCollection(ArrayList::new));
		ArrayList<LocalDate> userleave = new ArrayList<>();
		for(Leave lea:le) {
			userleave.addAll(lea.getStartDate().datesUntil(lea.getEndDate().plusDays(1))
					.collect(Collectors
		                    .toCollection(ArrayList::new)));}
		userleave.forEach(System.out::println);
		//Long count = lservice.countLeaves(LocalDate.of(2021, 12, 23), LocalDate.of(2021, 12, 25), u);
		//System.out.println(count);

		
	}

}
