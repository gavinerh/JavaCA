package edu.nus.java_ca.repository;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.LeaveStatus;

public interface LeaveRepo extends JpaRepository<Leave, Integer> {

	@Query("SELECT l FROM Leave l where l.leaveId = :Id")
	Leave findLeaveById(@Param("Id") Long Id);

//this is for checking EMPL LEAVE HISTORY
//	@Query("SELECT l FROM Leave l where l.user.userid = :Id")
//	Leave findLeaveByUserId(@Param("Id") Long d);
	ArrayList<Leave> findLeaveByUser_UserIdLike(Long userId);
	
	@Query("SELECT l FROM Leave l" 
			+ " WHERE l.status=:APPLIED " 
			+ "AND l.status= :UPDATED")
	 
	public ArrayList<Leave> findLeaveToApprove(@Param("APPLIED") LeaveStatus a, 
			@Param("UPDATED") LeaveStatus u);

	@Query("SELECT l FROM Leave l WHERE :date between l.startDate AND l.endDate")
	public ArrayList<Leave> findLeaveByDate(@Param("date") LocalDate date);
	
	@Query("SELECT c from Leave c WHERE c.status='APPLIED' OR c.status='APPROVED' OR c.status='UPDATED'")
	ArrayList<Leave> findAppliedLeaves();

	
}
