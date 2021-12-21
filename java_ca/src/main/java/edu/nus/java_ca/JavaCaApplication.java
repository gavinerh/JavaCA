package edu.nus.java_ca;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.nus.java_ca.model.Department;
import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.LeaveStatus;
import edu.nus.java_ca.model.LeaveType;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.repository.LeaveBalanceRepo;
import edu.nus.java_ca.repository.LeaveRepo;
import edu.nus.java_ca.repository.UserRepository;

@SpringBootApplication
public class JavaCaApplication {
	
	@Autowired
	LeaveRepo lrepo;
	@Autowired
	UserRepository urepo;
	@Autowired
	LeaveBalanceRepo lbrepo;
	
	public static void main(String[] args) {
		SpringApplication.run(JavaCaApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
//			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			LocalDate d = LocalDate.parse("2022-02-01");
			LocalDate d1 = LocalDate.parse("2022-02-02");
			LocalDate d2 = LocalDate.parse("2022-02-03");
			LocalDate d3 = LocalDate.parse("2022-02-04");

			//user
			User u = new User(); 
			User u1 = new User();
			User u2 = new User();
			User u3 = new User();
			u.setEmail("Zeus@gmail");u.setFirstName("Zeus"); u.setLastName("Greek");u.setPassword("password"); u.setDepartment(Department.HR);
			u1.setEmail("Nike@gmail");u1.setFirstName("Nike");u1.setLastName("Greek");u1.setPassword("password"); u1.setDepartment(Department.Coporate);
			u2.setEmail("Hade@gmail");u2.setFirstName("Hade"); u2.setLastName("Greek");u2.setPassword("password"); u2.setDepartment(Department.Financial);
			u3.setEmail("Pers@gmail");u3.setFirstName("Persephone"); u3.setLastName("Greek");u2.setPassword("password"); u2.setDepartment(Department.Production);

			ArrayList<User> ulist = new ArrayList<User>();
			ulist.add(u);ulist.add(u1);ulist.add(u2);ulist.add(u3);
			urepo.saveAllAndFlush(ulist);
				
			LeaveStatus ap = LeaveStatus.APPLIED;
//			LeaveType ml = LeaveType.MEDICALLEAVE; LeaveType cl = LeaveType.COMPENSATIONLEAVE;
//			LeaveType al = LeaveType.ANNUALlEAVE;
			
			LeaveBalance lb = new LeaveBalance(123, 13, 3);
			LeaveBalance lb1 = new LeaveBalance(124, 10, 2);
			LeaveBalance lb2 = new LeaveBalance(125, 5, 4);
			LeaveBalance lb3 = new LeaveBalance(126, 2, 1);
			ArrayList<LeaveBalance> lblist = new ArrayList<LeaveBalance>();
			lblist.add(lb);lblist.add(lb1);lblist.add(lb2);lblist.add(lb3);
			lbrepo.saveAllAndFlush(lblist);
			
			Leave l = new Leave(u, ap, "Medical Leave" , "cough, fever", 
					"George will take over the project temporarily", d, d, d2);
			Leave l1 = new Leave(u1, ap, "Compensation leave", "Family time", 
					"George will cover my appointments.", d, d1, d3);
			Leave l2 = new Leave(u2, ap, "Annual Leave", "Anniversary", 
					"George will helm Project Delta for 1 day.", d1, d2, d3);
			Leave l3 = new Leave(u3, ap, "Annual Leave", "Anniversary", 
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
