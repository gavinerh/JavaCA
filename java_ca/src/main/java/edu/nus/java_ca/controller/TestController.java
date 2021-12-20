package edu.nus.java_ca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.nus.java_ca.service.LeaveService;

@Controller
public class TestController {

	@Autowired
	LeaveService lService;
	
	@RequestMapping("/staff/list")
	public String index() {
		return "staff/staff";
	}
	
	@RequestMapping("/manager/list")
	public String manager() {
		return "manager/manager";
	}
}
