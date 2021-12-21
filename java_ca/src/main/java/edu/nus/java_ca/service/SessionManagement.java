package edu.nus.java_ca.service;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.support.SessionStatus;

import edu.nus.java_ca.model.User;

public interface SessionManagement {
	void createSession(HttpSession session, User user);
	void removeSession(HttpSession session, SessionStatus status);
	boolean isLoggedIn(HttpSession session, SessionStatus status);

}
