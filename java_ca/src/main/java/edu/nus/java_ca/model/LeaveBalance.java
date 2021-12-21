package edu.nus.java_ca.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	private Integer medical=60;
	private Integer annual;
	private Integer compensation;

	
	public LeaveBalance() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public LeaveBalance(Integer balId, Integer annual, Integer compensation) {
		super();
		this.balId = balId;
		this.annual = annual;
		this.compensation = compensation;
	}

	public Integer getBalId() {
		return balId;
	}

	public void setBalId(Integer balId) {
		this.balId = balId;
	}

	public Integer getMedical() {
		return medical;
	}

	public void setMedical(Integer medical) {
		this.medical = medical;
	}

	public Integer getAnnual() {
		return annual;
	}

	public void setAnnual(Integer annual) {
		this.annual = annual;
	}

	public Integer getCompensation() {
		return compensation;
	}

	public void setCompensation(Integer compensation) {
		this.compensation = compensation;
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
