package edu.nus.java_ca.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.LeaveStatus;
import edu.nus.java_ca.model.SessionClass;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.service.LeaveBalanceService;
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
	LeaveBalanceService lbservice;
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
		return "redirect:/staff/list";

	}
	
	@RequestMapping(value="/leave/list")
	public String showLeaves (Model model) {
		model.addAttribute("leaves", lservice.findAppliedLeaves());
		return "staff/leave-list";
	}
	
	@GetMapping(value = "/leave/new")
	public ModelAndView newLeave(HttpSession ses) {
		ModelAndView mav = new ModelAndView("staff/staff-new-leave");
		User u = user(ses);
		ArrayList<LeaveBalance> lb = lbservice.findByUser(u);
		ArrayList<String> s = new ArrayList<String>();
		for(LeaveBalance b:lb) {
			s.add(b.getLeavetype());
		}
		mav.addObject("leave", new Leave());
		mav.addObject("types",s);
		return mav;
	}

	@PostMapping(value = "/leave/new")
	public String createNewLeave(@ModelAttribute("leave")@Valid Leave leave, BindingResult result,Model model,HttpSession ses) {
		User u = user(ses);
		ArrayList<LeaveBalance> lb = lbservice.findByUser(u);
		ArrayList<String> s = new ArrayList<String>();
		for(LeaveBalance b:lb) {
			s.add(b.getLeavetype());
		}
		model.addAttribute("types",s);
		if (result.hasErrors()){
			return("staff/staff-new-leave");}
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
	
		lservice.createLeave(leave);
		String message = "New Leave " + leave.getLeaveId()+" Created ";
		System.out.println(message);
		u.getLb().forEach(System.out::println);
		return "redirect:/staff/list";}
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
	
	/*
	 * @RequestMapping(value = "/leave/history") public String leaveinfo( Model
	 * model,HttpSession session) {
	 * 
	 * 
	 * Leave staff = (Leave) session.getAttribute("currentLeave");
	 * model.addAttribute("leaves", lservice.findAppliedLeaves());//2 should change
	 * to staff
	 * 
	 * return "staff/staff-leave-history"; }
	 */
	 
	@GetMapping(value = "/leave/list")
	public String list(Model model, HttpSession session) {
		
	
		
		int currentpage = 0;

		List<Leave> listWithPagination = lservice.getAllLeaves(currentpage, 5);

		Leave lea = (Leave) session.getAttribute("currentLeave");
		
		model.addAttribute("leave", lea);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", currentpage);

		return "staff/staff-leave-history";
	}

	@GetMapping(value = "/leave/navigate")
	public String customlist(@RequestParam(value = "pageNo") int pageNo, Model model, HttpSession session) {

		List<Leave> listWithPagination = lservice.getAllLeaves(pageNo-1, 5);
		Leave lea = (Leave) session.getAttribute("currentLeave");
		
		model.addAttribute("leave", lea);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", pageNo-1);
		return "staff/staff-leave-history";
	}

	@GetMapping(value = "/leave/forward/{currentPage}")
	public String arrowlist(@PathVariable(value = "currentPage") String pageNo, Model model, HttpSession session) {
		Integer i = Integer.parseInt(pageNo);
		if (i == 2)
			i--;
		
		List<Leave> listWithPagination = lservice.getAllLeaves(i+1, 5);
		Leave lea = (Leave) session.getAttribute("currentLeave");
		
		model.addAttribute("leave", lea);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", i+1);
		
		return "staff/staff-leave-history";
	}

	@GetMapping(value = "/leave/backward/{currentPage}")
	public String backlist(@PathVariable(value = "currentPage")String pageNo ,Model model, HttpSession session) {
		Integer i = Integer.parseInt(pageNo);
		if (i == 0)
			i++;
		List<Leave> listWithPagination = lservice.getAllLeaves(i-1, 5);
		Leave lea = (Leave) session.getAttribute("currentLeave");
		
		model.addAttribute("leave", lea);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", i-1);
		
		return "staff/staff-leave-history";
	}

}
