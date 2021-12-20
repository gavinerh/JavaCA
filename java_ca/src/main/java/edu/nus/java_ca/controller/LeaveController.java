package edu.nus.java_ca.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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
import org.springframework.web.servlet.ModelAndView;

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
	
//	(Redirect to "/home")
	
	@RequestMapping(value="/all")
	public String listAll(Model model) {
		model.addAttribute("leaves", lservice.listAllLeaves());
		return "allleaves";
	}

	@RequestMapping(value="/mvt-reg")
	public String viewMvtReg(Model model) {
		model.addAttribute("leave", new Leave());	
		List<Integer> mthlist = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11);
		int year = Year.now().getValue();
		List<Integer> yrlist = Arrays.asList(year-1, year, year+1);
		model.addAttribute("mthlist", mthlist);
		model.addAttribute("yrlist", yrlist);
		return "mvt-reg";
	}
	@PostMapping(value="/view") 
	public String viewMvtRegChooseMth(@RequestParam("mth")String mth, 
			@RequestParam("yr")String yr, Model model) throws ParseException {				
		int mthparsed = Integer.parseInt(mth);
		int yrparsed = Integer.parseInt(yr);
		List<Leave> mls = lservice.findLeavesByYearandMonth
				(yrparsed, mthparsed);
		model.addAttribute("mvtleaves", mls);
		return "forward:/leave/mvt-reg";
	}
		
	//initial view of leave history of respective employee
	@RequestMapping(value="/empl-leavehistory")
	public String empLeaveHistSearchPage(Model model) {
		model.addAttribute("leave", new Leave());
		return "empl-leavehistory";
	}
	//after entering employee Id, cross map to staff history 
	@PostMapping(value="/search")
	public String searchLeavesByUserId(@RequestParam("user.userId") 
		String UserId, Model model) {
		ArrayList<Leave> lls = (ArrayList<Leave>) 
				lservice.listLeavesByUserId(Long.parseLong(UserId));
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
