package edu.nus.java_ca.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@DynamicUpdate
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE user_id=?")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;
	@Length(min=1, message = "Email cannot be empty.")
	private String email;
	@Length(min=1, message = "Firstname cannot be empty.")
	private String firstName;
	@Length(min=1, message="Lastname cannot be empty")
	private String lastName;
	@DateTimeFormat(pattern = "dd/MM/YYYY','HH:mm:ss")
	private Date lastLoginDate;
	@Enumerated(EnumType.STRING)
	private Department department;
	@Enumerated(EnumType.STRING)
	private Position position;
	@Length(min = 8, message = "Password must be more than 8 char long")
	private String password;
//@DateTimeFormat(pattern = "dd/MM/YYYY','HH:mm:ss")
//      private Date lastloginDate;
	
	@ManyToOne
	private User approvingOfficer;
	private boolean deleted = Boolean.FALSE;
	
	public User() {}

	

	public Long getUserId() {
		return userId;
	}



	public void setUserId(Long userId) {
		this.userId = userId;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public Date getLastLoginDate() {
		return lastLoginDate;
	}



	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}



	public Department getDepartment() {
		return department;
	}



	public void setDepartment(Department department) {
		this.department = department;
	}



	public Position getPosition() {
		return position;
	}



	public void setPosition(Position position) {
		this.position = position;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public Date getLastloginDate() {
		return lastLoginDate;
	}



	public void setLastloginDate(Date lastloginDate) {
		this.lastLoginDate = lastloginDate;
	}



	public User getApprovingOfficer() {
		return approvingOfficer;
	}



	public void setApprovingOfficer(User approvingOfficer) {
		this.approvingOfficer = approvingOfficer;
	}



	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", lastLoginDate=" + lastLoginDate + ", department=" + department + ", position=" + position
				+ ", password=" + password + ", coveringOfficer=" + approvingOfficer + "]";
	}


	

	
}
