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
	public Integer pagesize;
	/** Leave type and Balance to be use in Thymleaf*/
	private ArrayList<String> s = new ArrayList<>();
	private ArrayList<String> t = new ArrayList<>();
	
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
		return "forward:/home";

	}
	
	
	@GetMapping(value = "/leave/new")
	public ModelAndView newLeave(HttpSession ses) {
		User u = user(ses);
		/**find leaves for current user and his leave types*/
		ArrayList<LeaveBalance> lb = lbservice.findByUser(u);
		s.clear();
		t.clear();
		/**Save in private array in the controller class*/
		for(LeaveBalance b:lb) {
			s.add(b.getLeavetype().toUpperCase());
			t.add(b.getLeavetype().toUpperCase()+":\t"+b.getBalance().toString());
		}
		ModelAndView mav = new ModelAndView("staff/staff-new-leave");
		mav.addObject("leave", new Leave());
		mav.addObject("types",s);
		mav.addObject("bal",t);
		return mav;
	}

	@PostMapping(value = "/leave/new")
	public String createNewLeave(@ModelAttribute("leave")@Valid Leave leave, BindingResult result,Model model,HttpSession ses) {
		User u = user(ses);
		
		model.addAttribute("types",s);
		model.addAttribute("bal",t);
		
		if (result.hasErrors()){
			return("staff/staff-new-leave");}
		
		/**Check for Duplication and return error**/
		if(lservice.checkDupes(leave.getStartDate(), leave.getEndDate(),u)) {
			model.addAttribute("errormsg", "**You've already Applied the same period**");
			return("staff/staff-new-leave");
		}
		
		/**Count the number of leaves and return error if the user has not enough leave**/
		Long count = lservice.countLeaves(leave.getStartDate(), leave.getEndDate(),u);
		System.out.println("Total leave days: "+count);
		if(count==0) {	model.addAttribute("errormsg", "**Leave Application Failed!! You Applied on Holidays**");
		return("staff/staff-new-leave");}
		if(!lservice.deductleave(leave, u, count.intValue())) {
			model.addAttribute("errormsg", "**Leave Application Failed! You don't Have Enough Leave**");
			return("staff/staff-new-leave");
		}
		else {
		LocalDate now = LocalDate.now();
		leave.setAppliedDate(now);
		leave.setLeavetaken(count.intValue());
		leave.setStatus(LeaveStatus.APPLIED);
		leave.setUser(u);
		lservice.createLeave(leave);
		
		String message = "New Leave " + leave.getLeaveId()+" Created ";
		System.out.println(message);
		u.getLb().forEach(System.out::println);
		return "redirect:/staff/list";}
	}

	@GetMapping(value = "/leave/edit/{id}")
	public ModelAndView editLeavePage(@PathVariable ("id")long id,HttpSession ses) {
		User u = user(ses);
		Leave l = lservice.findLeaveById(id);
		ModelAndView mav = new ModelAndView("staff/leave-edit");
		mav.addObject("leave", l);
		mav.addObject("types",s);
		mav.addObject("bal",t);
		return mav;
	}

	@PostMapping(value = "/leave/edit")
	public String editLeave(@ModelAttribute ("leave")@Valid Leave l, BindingResult result, Model model,HttpSession ses){
		User u = user(ses);
		model.addAttribute("types",s);
		model.addAttribute("bal",t);
		if (result.hasErrors()) {
			return "staff/leave-edit";}
		if(lservice.checkDupes(l.getStartDate(), l.getEndDate(),u)) {
			model.addAttribute("errormsg", "**You've already Applied the same period**");
			return("staff/leave-edit");
		}
		Long count = lservice.countLeaves(l.getStartDate(), l.getEndDate(),u);
		System.out.println("Total leave days: "+count);
		if(count==0) {	model.addAttribute("errormsg", "**Leave Application Failed!! You Applied on Holidays**");
		return("staff/leave-edit");}
		if(!lservice.deductleave(l, u, count.intValue())) {
			model.addAttribute("errormsg", "**Leave Application Failed! You don't Have Enough Leave**");
			return("staff/leave-edit");
		}
		else {
		lservice.refundleave(l, u,l.getLeavetaken());
		
		l.setStatus(LeaveStatus.UPDATED);
		l.setLeavetaken(count.intValue());
		LocalDate now = LocalDate.now();
		l.setAppliedDate(now);
		lservice.changeLeave(l);
		return "redirect:/staff/list";
		}
	}
	
	@RequestMapping(value = "/leave/delete/{id}")
	public String deleteLeave(@PathVariable("id") long id,HttpSession ses) {
		User u = user(ses);
		Leave l = lservice.findLeaveById(id);
		/**To refund leave*/
		lservice.refundleave(l, u,l.getLeavetaken());
		l.setStatus(LeaveStatus.DELETED);
		lservice.changeLeave(l);
		return "redirect:/staff/leave/list";
	}
	@RequestMapping(value = "/leave/cancel/{id}")
	public String cancelLeave(@PathVariable("id") long id,HttpSession ses) {
		User u = user(ses);
		Leave l = lservice.findLeaveById(id);

		lservice.refundleave(l, u,l.getLeavetaken());
		l.setStatus(LeaveStatus.CANCELLED);
		lservice.changeLeave(l);
		return "redirect:/staff/leave/list";
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
	@RequestMapping(value = "/leave/list")
	public String list(Model model, HttpSession session) {
		
	
		this.pagesize = 10;
		User u = user(session);
		int currentpage = 0;
		int num = 10;
		List<Leave> listWithPagination = lservice.getAllLeaves(currentpage, num,u);
		int top = listWithPagination.size();
		int top1 = (top/num)+1;
		Leave lea = (Leave) session.getAttribute("currentLeave");
		
		model.addAttribute("leave", lea);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", currentpage);
		model.addAttribute("top1",top1);

		return "staff/staff-leave-history";
	}

	@RequestMapping(value = "/leave/navigate")
	public String customlist(@RequestParam(value = "pageNo") int pageNo, Model model, HttpSession session) {
		User u = user(session);
		List<Leave> listWithPagination = lservice.getAllLeaves(pageNo-1,pagesize,u);
		Leave lea = (Leave) session.getAttribute("currentLeave");
		
		int top = listWithPagination.size();
		int top1 = (top/pagesize)+1;
		

		model.addAttribute("leave", lea);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", pageNo-1);
		model.addAttribute("top1",top1);
		return "staff/staff-leave-history";
	}

	@GetMapping(value = "/leave/forward/{currentPage}")
	public String arrowlist(@PathVariable(value = "currentPage") String pageNo, Model model, HttpSession session) {
		Integer i = Integer.parseInt(pageNo);
		User u = user(session);
		List<Leave> userList =lservice.findByUser(u);
		int top = userList.size();
		int top1;
	if (top % pagesize>0)
	{
		 top1 = (top/pagesize)+1;}
	else {
		 top1 = top/pagesize;
	}
		if (i >= top1-1)
			i--;
		
		
		List<Leave> listWithPagination = lservice.getAllLeaves(i+1,pagesize,u);
		
        Leave lea = (Leave) session.getAttribute("currentLeave");
		
		
		model.addAttribute("leave", lea);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", i+1);
		model.addAttribute("top1",top1);
		
		return "staff/staff-leave-history";
	}

	@GetMapping(value = "/leave/backward/{currentPage}")
	public String backlist(@PathVariable(value = "currentPage")String pageNo ,Model model, HttpSession session) {
		User u = user(session);
		Integer i = Integer.parseInt(pageNo);
		if (i == 0)
			i++;

	
		List<Leave> listWithPagination = lservice.getAllLeaves(i-1,pagesize,u);
		List<Leave> userList =lservice.findByUser(u);
        Leave lea = (Leave) session.getAttribute("currentLeave");
		int top = userList.size();
		int top1;
	if (top % pagesize>0)
	{
		 top1 = (top/pagesize)+1;}
	else {
		 top1 = top/pagesize;
	}
	
		
		
	
	
		model.addAttribute("leave", lea);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", i-1);
		model.addAttribute("top1",top1);
		
		return "staff/staff-leave-history";
	}
	
	@GetMapping(value = "/leave/list/{id}")
	public String list(@PathVariable("id") int id ,Model model, HttpSession session) {

	this.pagesize= id;
		
		User u = user(session);
		int currentpage = 0;
		List<Leave> userList =lservice.findByUser(u);
		int top = userList.size();
		int top1;
	if (top % pagesize>0)
	{
		 top1 = (top/pagesize)+1;}
	else {
		 top1 = top/pagesize;
	}


		List<Leave> listWithPagination = lservice.getAllLeaves(currentpage, pagesize,u);
		Leave lea = (Leave) session.getAttribute("currentLeave");
		
		
		model.addAttribute("leave", lea);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", currentpage);
		model.addAttribute("top1",top1);
		
		return "staff/staff-leave-history";
}
}
