package edu.nus.java_ca;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import sg.iss.laps.model.Leave;
import sg.iss.laps.model.LeaveBalance;
import sg.iss.laps.model.LeaveStatus;
import sg.iss.laps.model.LeaveType;
import sg.iss.laps.model.User;
import sg.iss.laps.repository.LeaveBalanceRepo;
import sg.iss.laps.repository.LeaveRepo;
import sg.iss.laps.repository.UserRepo;

@SpringBootApplication
public class JavaCaApplication {

	@Autowired
	LeaveRepo lrepo;
	@Autowired
	UserRepo urepo;
	@Autowired
	LeaveBalanceRepo lbrepo;
	
	public static void main(String[] args) {
		SpringApplication.run(JavaCaApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
			LocalDate d = LocalDate.parse("2022-02-01");
			LocalDate d1 = LocalDate.parse("2022-02-02");
			LocalDate d2 = LocalDate.parse("2022-02-03");
			LocalDate d3 = LocalDate.parse("2022-02-04");

			//user
			User u = new User(); 
			User u1 = new User();
			User u2 = new User();
			User u3 = new User();
			u.setEmail("Zeus@gmail");u.setFirstName("Zeus"); u.setLastName("Greek");u.setPassword("password"); u.setDepartment("Security");
			u1.setEmail("Nike@gmail");u1.setFirstName("Nike");u1.setLastName("Greek");u1.setPassword("password"); u1.setDepartment("Branding");
			u2.setEmail("Hade@gmail");u2.setFirstName("Hade"); u2.setLastName("Greek");u2.setPassword("password"); u2.setDepartment("Marketing");
			u3.setEmail("Pers@gmail");u3.setFirstName("Persephone"); u3.setLastName("Greek");u2.setPassword("password"); u2.setDepartment("Management");

//			User u = new User((long)340, "Nike@com", "Nike", "Greek", d, "Branding", "12345678");
//			User u1 = new User((long)341, "Zeus@com", "Zeus", "Greek", d, "Security", "12345678");
//			User u2 = new User((long)342, "Hades@com", "Hades", "Greek", d, "Marketing", "12345678");
//			User u3 = new User((long)343, "Persephone@com", "Persephone", "Greek", d, "Management", "12345678");
			ArrayList<User> ulist = new ArrayList<User>();
			ulist.add(u);ulist.add(u1);ulist.add(u2);ulist.add(u3);
			urepo.saveAllAndFlush(ulist);
				
			LeaveStatus ap = LeaveStatus.APPLIED;
			LeaveType ml = LeaveType.MEDICALLEAVE; LeaveType cl = LeaveType.COMPENSATIONLEAVE;
			LeaveType al = LeaveType.ANNUALlEAVE;
			
			LeaveBalance lb = new LeaveBalance(123, u, al, 13);
			LeaveBalance lb1 = new LeaveBalance(124, u1, ml, 10);
			LeaveBalance lb2 = new LeaveBalance(125, u2, ml, 5);
			LeaveBalance lb3 = new LeaveBalance(126, u3, cl, (long)2.5);
			ArrayList<LeaveBalance> lblist = new ArrayList<LeaveBalance>();
			lblist.add(lb);lblist.add(lb1);lblist.add(lb2);lblist.add(lb3);
			lbrepo.saveAllAndFlush(lblist);
			
			Leave l = new Leave(u, ap, ml, "cough, fever", 
					"George will take over the project temporarily", d, d, d2);
			Leave l1 = new Leave(u1, ap, cl, "Family time", 
					"George will cover my appointments.", d, d1, d3);
			Leave l2 = new Leave(u2, ap, al, "Anniversary", 
					"George will helm Project Delta for 1 day.", d1, d2, d3);
			Leave l3 = new Leave(u3, ap, al, "Anniversary", 
					"All appointments have been postponed", d1, d2, d3);
			ArrayList<Leave> list = new ArrayList<Leave>();
			list.add(l);list.add(l1);list.add(l2);list.add(l3);
			lrepo.saveAllAndFlush(list);
			
			ArrayList<Leave> llist = (ArrayList) lrepo.findAll();
			for(Leave current : llist) {
				System.out.println(current.toString());
			}			
		};
	}
}
