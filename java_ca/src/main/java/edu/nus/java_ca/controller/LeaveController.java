package edu.nus.java_ca.controller;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
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


import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.service.UserService;
import edu.nus.java_ca.service.LeaveBalanceService;
import edu.nus.java_ca.service.LeaveService;
import edu.nus.java_ca.service.LeaveServiceImpl;





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
 
	//Movement Register
	@RequestMapping(value="/leaves/mvt-reg")
	public String viewMvtReg(Model model) {
		model.addAttribute("leave", new Leave());	
		List<Integer> mthlist = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11);
		int year = Year.now().getValue();
		List<Integer> yrlist = Arrays.asList(year-1, year, year+1);
		model.addAttribute("mthlist", mthlist);
		model.addAttribute("yrlist", yrlist);
		return "leaves/mvt-reg";
	}
	@PostMapping(value="/view") 
	public String viewMvtRegChooseMth(@RequestParam("mth")String mth, 
			@RequestParam("yr")String yr, Model model) throws ParseException {				
		int mthparsed = Integer.parseInt(mth);
		int yrparsed = Integer.parseInt(yr);
		List<Leave> mls = lservice.findLeavesByYearandMonth	//NEW QUERY IN LSERVICE
				(yrparsed, mthparsed);
		model.addAttribute("mvtleaves", mls);
		return "forward:/leave/leaves/mvt-reg";
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
		User u = uservice.findByUserId(ls.getUser().getUserId());
		ArrayList<Leave> lls = (ArrayList<Leave>) 
				lservice.listLeavesByUserId(u.getUserId());
		model.addAttribute("emleaves", lls);
		return "forward:/leave/empl-leavehistory";
	}
		
	//manager actions
	@RequestMapping(value = "/list")
	public String list(Model model) {
		//model.addAttribute("leaves", lservice.listLeaveToApprove());
		return "leave-toapprove";
	}
	
	@RequestMapping(value = "/approve/{id}")
	public String approveLeave(@PathVariable("id") Long id) {
		lservice.approveLeave(lservice.findLeaveById(id));
		//leave balance has to be reduced for corresponding user
		return "forward:/leave/list";
	}
	
	//rejection require comment
	@RequestMapping(value = "/reject/{id}")
	public String rejectLeave(@PathVariable("id") Long id) {
		lservice.rejectLeave(lservice.findLeaveById(id));
		return "forward:/leave/list";
	}

	
}
