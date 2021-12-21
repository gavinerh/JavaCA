package edu.nus.java_ca.controller;

import java.time.LocalDate;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.LeaveStatus;
import edu.nus.java_ca.model.SessionClass;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.repository.LeaveBalanceRepo;
import edu.nus.java_ca.service.LeaveService;
import edu.nus.java_ca.service.LeaveServiceImpl;
import edu.nus.java_ca.service.UserService;
import edu.nus.java_ca.service.UserServiceImpl;
import edu.nus.java_ca.validator.LeaveValidator;



@Controller
@RequestMapping(value = "/staff")
public class StaffController {
	 @InitBinder
	  protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new LeaveValidator());
	  }
	@Autowired
	LeaveBalanceRepo lbrepo;
	@Autowired
	private LeaveService lservice;
	@Autowired
	public void setLeaveService (LeaveServiceImpl lservice) {
		this.lservice = lservice;
	}
	@Autowired
	private UserService uservice;
	@Autowired
	public void setUserService (UserServiceImpl uservice) {
		this.uservice = uservice;
	}
	private User user(HttpSession ses) {
		SessionClass session = (SessionClass)ses.getAttribute("uSession");
		String email = session.getEmail();
		User user = uservice.findByUserEmail(email);
		return user;
	}
	
	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/home";

	}
	
	@RequestMapping(value="/leave/list")
	public String showLeaves (Model model) {
		model.addAttribute("leaves", lservice.findAppliedLeaves());
		return "staff/leave-list";
	}
	
	@GetMapping(value = "/leave/new")
	public ModelAndView newLeave() {
		ModelAndView mav = new ModelAndView("staff/staff-new-leave");
		mav.addObject("leave", new Leave());
		return mav;
	}

	@PostMapping(value = "/leave/new")
	public String createNewLeave(@ModelAttribute("leave")@Valid Leave leave, BindingResult result,Model model,HttpSession ses) {
		if (result.hasErrors()){
			return("staff/staff-new-leave");}
		User u = user(ses);
		if(lservice.checkDupes(leave.getStartDate(), leave.getEndDate(),u)) {
			model.addAttribute("errormsg", "**You've already Applied the same period**");
			return("staff/staff-new-leave");
		}
	
		Long count = lservice.countLeaves(leave.getStartDate(), leave.getEndDate());
		System.out.println("Total leave days: "+count);
		if(!lservice.deductleave(leave, u, count.intValue())) {
			model.addAttribute("errormsg", "**Leave Application Failed! You don't Have Enough Leave**");
			return("staff/staff-new-leave");
		}
		else {
		LocalDate now = LocalDate.now();
		leave.setAppliedDate(now);
		leave.setStatus(LeaveStatus.APPLIED);
		leave.setUser(u);
		lbrepo.saveAndFlush(u.getLb());
		lservice.createLeave(leave);
		String message = "New course " + leave.getLeaveId()+" Created ";
		System.out.println(message);
		System.out.println(u.getLb());
		return "forward:/staff/leave/list";}
	}

	@GetMapping(value = "/leave/edit/{id}")
	public ModelAndView editLeavePage(@PathVariable ("id")long id) {
		ModelAndView mav = new ModelAndView("staff/leave-edit");
		Leave leave = lservice.findLeaveById(id);
		mav.addObject("leave", leave);
		
		return mav;
	}

	@PostMapping(value = "/leave/edit")
	public String editLeave(@ModelAttribute ("leave")@Valid Leave l, BindingResult result, Model model){
		
		if (result.hasErrors())
			return "staff/leave-edit";
		l.setStatus(LeaveStatus.UPDATED);
		LocalDate now = LocalDate.now();
		l.setAppliedDate(now);
		lservice.changeLeave(l);
		return "forward:/staff/leave/list";
	}
	@RequestMapping(value = "/leave/delete/{id}")
	public String deleteLeave(@PathVariable("id") long id) {
		Leave l = lservice.findLeaveById(id);
		l.setStatus(LeaveStatus.DELETED);
		lservice.changeLeave(l);
		return "forward:/staff/leave/list";
	}
	@RequestMapping(value = "/leave/cancel/{id}")
	public String cancelLeave(@PathVariable("id") long id) {
		Leave l = lservice.findLeaveById(id);
		l.setStatus(LeaveStatus.CANCELLED);
		lservice.changeLeave(l);
		return "forward:/staff/leave/list";
	}
}
