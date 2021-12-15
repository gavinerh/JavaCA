package edu.nus.java_ca.model;

import java.util.Date;

public class SessionClass {
	private String email;
	private Date lastLoggedIn;
	public SessionClass(String email, Date lastLoggedIn) {
		super();
		this.email = email;
		this.lastLoggedIn = lastLoggedIn;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getLastLoggedIn() {
		return lastLoggedIn;
	}
	public void setLastLoggedIn(Date lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
	}
	
}
