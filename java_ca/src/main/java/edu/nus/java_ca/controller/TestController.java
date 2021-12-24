package edu.nus.java_ca.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

import edu.nus.java_ca.service.LeaveService;
import edu.nus.java_ca.service.SessionManagement;

@Controller
public class TestController {

	@Autowired
	LeaveService lService;
	
	@Autowired
	SessionManagement sess;
	
	@RequestMapping("/staff/list")
	public String index(HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return "redirect:/";
		return "staff/staff";
	}
	
	@RequestMapping("/manager/list")
	public String manager(HttpSession session, SessionStatus status) {
		if (!sess.isLoggedIn(session, status)) return "redirect:/";
		return "manager/manager";
	}
}
