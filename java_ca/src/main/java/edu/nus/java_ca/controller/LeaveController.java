package edu.nus.java_ca.controller;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.iss.laps.model.Leave;
import sg.iss.laps.model.User;
import sg.iss.laps.service.UserService;
import sg.iss.laps.service.LeaveBalanceService;
import sg.iss.laps.service.LeaveService;
import sg.iss.laps.service.LeaveServiceImpl;

@Controller
@RequestMapping("/leave")
public class LeaveController {

	@Autowired
	private LeaveService lservice;
	@Autowired
	private UserService uservice;
	@Autowired
	public void setLeaveService(LeaveServiceImpl lserviceImpl) {
		this.lservice = lserviceImpl;
	}
	
//	@Autowired
//	private LeaveBalanceService lbservice;
	
	@RequestMapping(value="/all")
	public String listAll(Model model) {
		model.addAttribute("leaves", lservice.listAllLeaves());
		return "allleaves";
	}
 
	@RequestMapping(value="/mvt-reg")
	public String viewMvtReg(Model model) {
		model.addAttribute("leave", new Leave());	
		return "mvt-reg";
	}
	@PostMapping(value="/view") 
	public String viewMvtRegChooseMth(@RequestParam("startDate")
	@DateTimeFormat(pattern="dd-MM-yyyy") LocalDate startDate, Model model) {
		model.addAttribute("startDate", startDate);		
		ArrayList<Leave> mls = (ArrayList<Leave>) lservice.findLeavesByDate(startDate);
		model.addAttribute("mvtleaves", mls);
		return "forward:/leave/mvt-reg";
	}
	
	//initial view of leave history of respective employee
	@RequestMapping(value="/empl-search")
	public String empLeaveHistSearchPage(Model model) {
		model.addAttribute("leave", new Leave());
		return "empl-search";
	}
	//after entering employee Id, cross map to staff history 
	@PostMapping(value="/search")
	public String searchLeavesByUserId(@ModelAttribute("leave") 
		@ Valid Leave ls, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "empl-search";
		}
//		model.addAttribute("leave", new Leave());
		User u = uservice.findUserById(ls.getUser().getUserId());
		ArrayList<Leave> lls = (ArrayList<Leave>) 
				lservice.listLeavesByUserId(u.getUserId());
		model.addAttribute("emleaves", lls);
		return "forward:/leave/empl-leavehistory";
	}
		
	//manager actions
	@RequestMapping(value = "/list")
	public String list(Model model) {
		model.addAttribute("leaves", lservice.listLeaveToApprove());
		return "leave-toapprove";
	}
	
	@RequestMapping(value = "/approve/{id}")
	public String approveLeave(@PathVariable("id") Integer id) {
		lservice.approveLeave(lservice.findLeaveById(id));
		//leave balance has to be reduced for corresponding user
		return "forward:/leave/list";
	}
	
	//rejection require comment
	@RequestMapping(value = "/reject/{id}")
	public String rejectLeave(@PathVariable("id") Integer id) {
		lservice.rejectLeave(lservice.findLeaveById(id));
		return "forward:/leave/list";
	}

	
}
