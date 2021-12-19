package edu.nus.java_ca.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.nus.java_ca.JavaCaApplication;
import edu.nus.java_ca.model.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes=JavaCaApplication.class)
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

	@Autowired
	UserRepository uRepo;
	
	@Test
	@Order(1)
	public void testAddingUser() {
		User u1 = new User();
		u1.setEmail("test@gmail");
		u1.setFirstName("test");
		u1.setLastName("test");
		u1.setPassword("password");
		//u1.setDepartment("test");
		uRepo.saveAndFlush(u1);
		System.out.println("Added one user");
	}
	
	@Test
	@Order(2)
	public void testFindByUserEmail() {
		List<User> result = new ArrayList<User>();
		result.addAll(uRepo.findByUserEmail("test@gmail"));
		if(result.isEmpty()){
			System.out.println("result is empty");
		}
		assertEquals(1, result.size());
		System.out.print(result.get(0));
	}
	
	@Test
	@Order(3)
	public void testUpdateLoginDate() {
		List<User> result = new ArrayList<User>();
		result.addAll(uRepo.findByUserEmail("test@gmail"));
		User u1 = result.get(0);
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		Instant now = LocalDateTime.now().atZone(ZoneId.of("Asia/Singapore")).toInstant();
		u1.setLastLoginDate(Date.from(now));
		uRepo.save(u1);
	}
	
	
	
	
}
