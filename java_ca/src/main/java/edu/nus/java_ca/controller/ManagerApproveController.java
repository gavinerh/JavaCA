package edu.nus.java_ca.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import antlr.collections.List;
import edu.nus.java_ca.model.Department;
import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.Position;
import edu.nus.java_ca.model.SessionClass;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.service.EmailService;
import edu.nus.java_ca.service.LeaveService;
import edu.nus.java_ca.service.LeaveServiceImpl;
import edu.nus.java_ca.service.SessionManagement;
import edu.nus.java_ca.service.UserService;

@Controller
@RequestMapping(value = "/manager")
public class ManagerApproveController {

	@Autowired
	private LeaveService lservice;
	@Autowired
	private UserService uservice;
	@Autowired
	public void setLeaveService(LeaveServiceImpl lserviceImpl) {
		this.lservice = lserviceImpl;
	}
	@Autowired
	private SessionManagement sess;
	
	@Autowired
	private EmailService eService;
//	@Autowired
//	private LeaveBalanceService lbservice;

	
//	Add
//	Cancel
	
	@RequestMapping(value="/home")
	public String managerDashboard(HttpSession sessions, Model model) {
		if (!checkManager(sessions))
		{
			return "redirect:/logout";
		}
		
		SessionClass session = (SessionClass)sessions.getAttribute("uSession");
		String emailString = session.getEmail();
		User user = uservice.findByUserEmail(emailString);
		Department department = user.getDepartment();
		
		Collection <Leave> pendingleave = lservice.listLeaveToApprove();

		model.addAttribute("pendingleave", lservice.findAppliedLeaves());
		
		return "manager/manager";
	}
	
	@RequestMapping(value = "/leave/approve/{id}")
	public String approveLeave(@PathVariable("id") Long id) {
		
		Leave leave = lservice.findLeaveById(id);
		lservice.approveLeave(leave);
		eService.sendEmailApprove(leave);
		
		
		return "forward:/manager/home";
	}
	
	@RequestMapping(value = "/leave/reject/{id}")
	public String rejectLeave(@PathVariable("id") Long id) {
		Leave leave = lservice.findLeaveById(id);
		lservice.rejectLeave(leave);
		return "forward:/manager/home";
	}
	
	public boolean checkManager (HttpSession sessions)
	{
		SessionClass session = (SessionClass)sessions.getAttribute("uSession");
		String emailString = session.getEmail();
		User user = uservice.findByUserEmail(emailString);
		Position position = user.getPosition();
		If (position == Position.Manager);
		{
			return true;	
		}
		
	}
	
	

	private void If(boolean b) {
		// TODO Auto-generated method stub
		
	}

	
}