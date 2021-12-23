package edu.nus.java_ca.repository;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.nus.java_ca.JavaCaApplication;
import edu.nus.java_ca.service.LeaveService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes=JavaCaApplication.class)
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class ReportRepositoryTest {
	
	@Autowired
	LeaveRepo lRepo;
	
	@Autowired
	LeaveService lService;
	
	@Autowired
	UserRepository uRepo;
	
	@Autowired
	ReportRepository rRepo;
	
	@Autowired
	LeaveBalanceRepo lbRepo;

//	@Test
//	@Order(1)
//	public void testAddLeaveAndUser() {
//		User u1 = new User();
//		u1.setDepartment(Department.Coporate); u1.setEmail("manager@manager"); u1.setFirstName("manager");
//		u1.setPosition(Position.Manager);
//		User u5 = new User();
//		u5.setDepartment(Department.Financial); u5.setEmail("manager2@manager"); u5.setFirstName("manager");
//		u5.setPosition(Position.Manager);
//		User u2 = new User();
//		u2.setDepartment(Department.Coporate); u2.setEmail("staff1@staff"); u2.setFirstName("staff1");
//		u2.setPosition(Position.Staff); u2.setApprovingOfficer(u1);
//		User u3 = new User();
//		u3.setDepartment(Department.Financial); u3.setEmail("staff2@staff"); u3.setFirstName("staff1");
//		u3.setPosition(Position.Staff); u3.setApprovingOfficer(u1);
//		User u4 = new User();
//		u4.setDepartment(Department.Financial); u4.setEmail("staff3@staff"); u4.setFirstName("staff1");
//		u4.setPosition(Position.Staff); u4.setApprovingOfficer(u5);
//		
//		uRepo.saveAndFlush(u5);
//		uRepo.saveAndFlush(u1); uRepo.saveAndFlush(u2); uRepo.saveAndFlush(u3); uRepo.saveAndFlush(u4);
//		
//		
//	}
//	
//	@Test
//	@Order(2)
//	public void testfindLeaveByUser() {
//		User manager = uRepo.findByUserEmail("manager@manager").get(0);
//		List<User> users = uRepo.findUsersByApprovingOfficer(manager);
//		System.out.println(users.size());
//		List<Leave> result = new ArrayList<Leave>();
//		for(User u : users) {
//			result.addAll(rRepo.findLeaveByUser(u));
//		}
//		System.out.println(result.size());
//		for(Leave l : result) {
//			System.out.println(l.getStartDate() + " " +  l.getEndDate());
//		}
//	}
	
//	@Test
//	public void addUserLeave() {
//		User manager = uRepo.findByUserEmail("manager@manager").get(0);
//		User u2 = uRepo.findByUserEmail("staff1@staff").get(0);
//		System.out.println("User is found: " + u2.getEmail());
//		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//		
//		for(int i=10; i<29; i++) {
//			String date = "2021-12-";
//			String time = " 13:00";
//			int start = i;
//			String startStr = date;
//			startStr += Integer.toString(i);
//			startStr += time;
//			String endStr = date;
//			endStr += Integer.toString(i+1);
//			endStr += time;
//			LocalDate startD = LocalDateTime.parse(startStr, df).atZone(ZoneId.of("Asia/Singapore")).toLocalDate();
//			LocalDate endD = LocalDateTime.parse(endStr, df).atZone(ZoneId.of("Asia/Singapore")).toLocalDate();
//			Leave l1 = new Leave();
//			l1.setStartDate(startD); l1.setEndDate(endD); 
//			l1.setContactdetail("99999"); l1.setStatus(LeaveStatus.APPLIED);
//			l1.setUser(u2);
//			rRepo.saveAndFlush(l1);
//		}
//	}
//	
//	@Test
////	@Order(3)
//	public void testfindLeaveByApprovingOfficer2() {
//		User manager = uRepo.findByUserEmail("manager2@manager").get(0);
//		List<Leave> result = rRepo.findLeaveByApprovingOfficer(manager);
//		for(Leave l : result) {
//			System.out.println(l.getUser());
//		}
//	}
	
	
//	@Test
//	public void testFindDistinctLeaveType() {
//		List<String> lists = lbRepo.findDistinctLeaveType();
//		for(String s : lists) {
//			System.out.println(s);
//		}
//	}
	

	
	
}
