package edu.nus.java_ca.LeaveTestCase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.nus.java_ca.JavaCaApplication;
import edu.nus.java_ca.model.Department;
import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.LeaveStatus;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.repository.LeaveBalanceRepo;
import edu.nus.java_ca.repository.LeaveRepo;
import edu.nus.java_ca.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JavaCaApplication.class)
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LeaveUnitTestCase {

	@Autowired
	LeaveRepo lrepo;
	@Autowired
	UserRepository urepo;
	@Autowired
	LeaveBalanceRepo lbrepo;

	
//	@Test
//	@Order(1)
//	public void testfindAllLeaveToApprove() {
//	
//		LocalDate d = LocalDate.parse("2022-02-01");
//		LocalDate d1 = LocalDate.parse("2022-02-02");
//		LocalDate d2 = LocalDate.parse("2022-02-03");
//		LocalDate d3 = LocalDate.parse("2022-02-04");
//
//		//user
//		User u = new User();User u1 = new User();User u2 = new User();User u3 = new User();
//		u.setEmail("Zeus@gmail");u.setFirstName("Zeus"); u.setLastName("Greek");u.setPassword("password"); u.setDepartment(Department.HR);
//		u1.setEmail("Nike@gmail");u1.setFirstName("Nike");u1.setLastName("Greek");u1.setPassword("password"); u1.setDepartment(Department.Coporate);
//		u2.setEmail("Hade@gmail");u2.setFirstName("Hade"); u2.setLastName("Greek");u2.setPassword("password"); u2.setDepartment(Department.Financial);
//		u3.setEmail("Pers@gmail");u3.setFirstName("Persephone"); u3.setLastName("Greek");u2.setPassword("password"); u2.setDepartment(Department.Production);
//
//		ArrayList<User> ulist = new ArrayList<User>();
//		ulist.add(u);ulist.add(u1);ulist.add(u2);ulist.add(u3);
//		urepo.saveAllAndFlush(ulist);
//			
//		LeaveStatus ap = LeaveStatus.APPLIED;
//		
//		//LEAVEBALANCE
//		LeaveBalance lb = new LeaveBalance(123, "MEDICALLEAVE", 3, u);
//		LeaveBalance lb1 = new LeaveBalance(124, "ANNUALLEAVE", 2, u1);
//		LeaveBalance lb2 = new LeaveBalance(125, "ANNUALLEAVE", 4, u2);
//		LeaveBalance lb3 = new LeaveBalance(126, "ANNUALLEAVE", 3, u3);
//		ArrayList<LeaveBalance> lblist = new ArrayList<LeaveBalance>();
//		lblist.add(lb);lblist.add(lb1);lblist.add(lb2);lblist.add(lb3);
//		lbrepo.saveAllAndFlush(lblist);
//			
//		//LEAVE
//		Leave l = new Leave(u, ap, "Medical Leave" , "cough, fever", 
//				"George will take over the project temporarily", d, d, d2);
//		Leave l1 = new Leave(u1, ap, "Compensation leave", "Family time", 
//				"George will cover my appointments.", d, d1, d3);
//		Leave l2 = new Leave(u2, ap, "Annual Leave", "Anniversary", 
//				"George will helm Project Delta for 1 day.", d1, d2, d3);
//		Leave l3 = new Leave(u3, ap, "Annual Leave", "Anniversary", 
//				"All appointments have been postponed", d1, d2, d3);
//		ArrayList<Leave> list = new ArrayList<Leave>();
//		list.add(l);list.add(l1);list.add(l2);list.add(l3);
//		lrepo.saveAllAndFlush(list);
//		
//		ArrayList<Leave> llist = (ArrayList) lrepo.findLeaveToApprove(ap, LeaveStatus.UPDATED);
//		for (Leave current : llist) {
//			System.out.println(current.toString());
//		}
//		assertEquals(llist.size(), 4);
//	}
}
