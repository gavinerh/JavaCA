package edu.nus.java_ca.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.nus.java_ca.model.Position;
import edu.nus.java_ca.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE u.email = :email")
	List<User> findByUserEmail(@Param("email") String email);
	
//	@Modifying
//	@Query("UPDATE User u SET u.lastLoginDate = :date WHERE u.userId = :id")
//	int updateUserLoginDate(@Param("date") Date date, @Param("id") UUID id);
	
	@Query("SELECT u FROM User u WHERE u.userId = :userId")
	User  findByUserId(@Param("userId") Long userId);
	
	
	
	
	
	@Modifying
	@Query("update User u set u.lastLoginDate = ?1 where u.userId = ?2")
	void updateLoginDate(Date date, UUID userId);
	
	@Query("SELECT u FROM User u WHERE u.position = :position AND u.deleted = false")
	List<User> findByPosition(Position position);
	
	@Query("SELECT u FROM User u WHERE u.deleted = false")
	List<User> findAll();
}
