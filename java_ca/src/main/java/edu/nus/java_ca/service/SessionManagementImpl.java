package edu.nus.java_ca.service;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import edu.nus.java_ca.model.SessionClass;
import edu.nus.java_ca.model.User;

@Service
@SessionAttributes("uSession")
public class SessionManagementImpl implements SessionManagement{


	@Override
	public void createSession(HttpSession session, User user) {
		// create new session base object for storing in spring session object
		SessionClass s = new SessionClass(user.getEmail(), new Date());
		session.setAttribute("uSession", s);
	}

	@Override
	public void removeSession(HttpSession session, SessionStatus status) {
		status.setComplete();
	}

	@Override
	public boolean isLoggedIn(HttpSession session, SessionStatus status) {
		if(session.getAttribute("uSession") == null) {
			return false;
		}
		// check the last logged in date
		Calendar c1 = Calendar.getInstance();
		SessionClass storedSessionClass = (SessionClass) session.getAttribute("uSession"); //stored sessionClass
		c1.setTime(new Date()); //current date
		Calendar c2 = Calendar.getInstance();
		c2.setTime(storedSessionClass.getLastLoggedIn()); // last logged in date
		// timeout in 10 sec
		System.out.println("TimeNow: " + c1.getTimeInMillis());
		System.out.println("TimeLastLogged: " + c2.getTimeInMillis());
		if((c1.getTimeInMillis() - c2.getTimeInMillis()) > 300000) {
			status.setComplete();
			return false;
		}
		return true;
	}
	
	@Override
	public String getUserEmail(HttpSession session) {
		SessionClass sClass = (SessionClass) session.getAttribute("uSession");
		return sClass.getEmail();
	}
	
	

}
