package edu.nus.java_ca.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Holidays {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer holidayId;
	private LocalDate holiday;
	public Holidays() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Holidays(LocalDate holiday) {
		super();
		this.holiday = holiday;
	}
	public Integer getHolidayId() {
		return holidayId;
	}
	public void setHolidayId(Integer holidayId) {
		this.holidayId = holidayId;
	}
	public LocalDate getHoliday() {
		return holiday;
	}
	public void setHoliday(LocalDate holiday) {
		this.holiday = holiday;
	}
	

}
