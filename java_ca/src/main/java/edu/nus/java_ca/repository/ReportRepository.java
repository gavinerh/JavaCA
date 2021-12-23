package edu.nus.java_ca.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.User;

public interface ReportRepository extends JpaRepository<Leave, Long> {
	@Query("SELECT l FROM Leave l WHERE l.user = :u")
	List<Leave> findLeaveByUser(@Param("u") User user);
	
	@Query("SELECT l FROM Leave l WHERE l.user.approvingOfficer = :approving")
	List<Leave> findLeaveByApprovingOfficer(@Param("approving") User manager);
	
}
