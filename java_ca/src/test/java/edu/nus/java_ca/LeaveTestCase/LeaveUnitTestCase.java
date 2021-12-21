//package edu.nus.java_ca.LeaveTestCase;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//import org.junit.jupiter.api.TestMethodOrder;
//import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import sg.iss.laps.JavaCaApplication;
//import sg.iss.laps.model.Leave;
//import sg.iss.laps.model.LeaveBalance;
//import sg.iss.laps.model.LeaveStatus;
//import sg.iss.laps.model.LeaveType;
//import sg.iss.laps.model.User;
//import sg.iss.laps.repository.LeaveBalanceRepo;
//import sg.iss.laps.repository.LeaveRepo;
//import sg.iss.laps.repository.UserRepo;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = JavaCaApplication.class)
//@TestMethodOrder(OrderAnnotation.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class LeaveUnitTestCase {
//
//	@Autowired
//	LeaveRepo lrepo;
//	@Autowired
//	UserRepo urepo;
//	@Autowired
//	LeaveBalanceRepo lbrepo;
//
//	
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
//		User u = new User((long)340, "Nike@com", "Nike", "Greek", d, "Branding", "12345678");
//		User u1 = new User((long)341, "Zeus@com", "Zeus", "Greek", d, "Security", "12345678");
//		User u2 = new User((long)342, "Hades@com", "Hades", "Greek", d, "Marketing", "12345678");
//		User u3 = new User((long)343, "Persephone@com", "Persephone", "Greek", d, "Management", "12345678");
//	
//		User u4 = new User((long)340, "Nike");
//		User u5 = new User((long)341, "Zeus");
//		User u6 = new User((long)342, "Hades");
//		User u7 = new User((long)343, "Persephone");
//		ArrayList<User> ulist = new ArrayList<User>();
//		ulist.add(u);ulist.add(u1);ulist.add(u2);ulist.add(u3);
//		urepo.saveAllAndFlush(ulist);
//			
//		LeaveStatus ap = LeaveStatus.APPLIED;
//		LeaveType ml = LeaveType.MEDICALLEAVE; LeaveType cl = LeaveType.COMPENSATIONLEAVE;
//		LeaveType al = LeaveType.ANNUALlEAVE;
//		
//		LeaveBalance lb = new LeaveBalance(123, u, al, 50);
//		LeaveBalance lb1 = new LeaveBalance(124, u1, al, 50);
//		LeaveBalance lb2 = new LeaveBalance(125, u2, al, 50);
//		LeaveBalance lb3 = new LeaveBalance(126, u3, al, 50);
//		ArrayList<LeaveBalance> lblist = new ArrayList<LeaveBalance>();
//		lblist.add(lb);lblist.add(lb1);lblist.add(lb2);lblist.add(lb3);
//		lbrepo.saveAllAndFlush(lblist);
//		
//		Leave l = new Leave(u, ap, ml, "cough, fever", 
//				"George will take over the project temporarily", d, d, d2);
//		Leave l1 = new Leave(u1, ap, cl, "Family time", 
//				"George will cover my appointments.", d, d1, d3);
//		Leave l2 = new Leave(u2, ap, al, "Anniversary", 
//				"George will helm Project Delta for 1 day.", d1, d2, d3);
//		Leave l3 = new Leave(u3, ap, al, "Anniversary", 
//				"All appointments have been postponed", d1, d2, d3);
//		ArrayList<Leave> list = new ArrayList<Leave>();
//		list.add(l);list.add(l1);list.add(l2);list.add(l3);
//		lrepo.saveAllAndFlush(list);
//		
//	ArrayList<Leave> llist = (ArrayList) lrepo.findLeaveToApprove(ap, LeaveStatus.UPDATED);
//	for (Leave current : llist) {
//		System.out.println(current.toString());
//	}
//	assertEquals(llist.size(), 4);
//	}
//}
