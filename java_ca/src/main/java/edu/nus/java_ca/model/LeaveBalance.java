package edu.nus.java_ca.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="leavebal")
public class LeaveBalance {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer balId;
	@OneToOne
	private User user;
	private LeaveType type; //enum
	private int balance;

	
	public LeaveBalance() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LeaveBalance(Integer balId, User user, LeaveType type) {
		super();
		this.balId = balId;
		this.user = user;
		this.type = type;
	}
	public LeaveBalance(Integer balId, User user, LeaveType type, int balance) {
		super();
		this.balId = balId;
		this.user = user;
		this.type = type;
		this.balance = balance;
	}
	public Integer getBalId() {
		return balId;
	}
	public void setBalId(Integer balId) {
		this.balId = balId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public LeaveType getType() {
		return type;
	}
	public void setType(LeaveType type) {
		this.type = type;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	@Override
	public int hashCode() {
		return Objects.hash(balId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LeaveBalance other = (LeaveBalance) obj;
		return Objects.equals(balId, other.balId);
	}


	
	
}
