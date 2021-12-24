package edu.nus.java_ca.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import edu.nus.java_ca.model.Department;
import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.LeaveStatus;
import edu.nus.java_ca.model.Position;
import edu.nus.java_ca.model.SessionClass;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.security.Hash;
import edu.nus.java_ca.service.EmailService;
import edu.nus.java_ca.service.LeaveBalanceService;
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
	@Autowired
	private LeaveBalanceService lbService;
	public Integer pagesize;

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
		ArrayList <Leave> pendingleave = (ArrayList<Leave>) lservice.listLeaveToApprove(department);
		model.addAttribute("pendingleave", pendingleave);
		
		String	em = sess.getUserEmail(sessions);
	    User result = uservice.findByUserEmail(em);
		System.out.println(result.getLastName());
		model.addAttribute("usernow", result);	
		return "manager/manager";
	}
	
	@GetMapping(value = "/leave/new")
	public ModelAndView newLeave(HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return new ModelAndView("redirect:/");
		ModelAndView mav = new ModelAndView("manager/manager-new-leave");
		User u = user(ses);
		ArrayList<LeaveBalance> lb = lbService.findByUser(u);
		ArrayList<String> s = new ArrayList<String>();
		for(LeaveBalance b:lb) {
			s.add(b.getLeavetype());
		}
		mav.addObject("leave", new Leave());
		mav.addObject("types",s);
		return mav;
	}
	@PostMapping(value = "/leave/new")
	public String createNewLeave(@ModelAttribute("leave")@Valid Leave leave, BindingResult result,Model model,HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return "redirect:/";
		User u = user(ses);
		ArrayList<LeaveBalance> lb = lbService.findByUser(u);
		ArrayList<String> s = new ArrayList<String>();
		for(LeaveBalance b:lb) {
			s.add(b.getLeavetype());
		}
		model.addAttribute("types",s);
		if (result.hasErrors()){
			return("manager/manager-new-leave");}
		if(lservice.checkDupes(leave.getStartDate(), leave.getEndDate(),u)) {
			model.addAttribute("errormsg", "**You've already Applied the same period**");
			return("manager/manager-new-leave");
		}
		Long count = lservice.countLeaves(leave.getStartDate(), leave.getEndDate(),u);
		System.out.println("Total leave days: "+count);
		if(!lservice.deductleave(leave, u, count.intValue())) {
			model.addAttribute("errormsg", "**Leave Application Failed! You don't Have Enough Leave**");
			return("manager/manager-new-leave");
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
		return "redirect:/manager/home";}
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
	private User user(HttpSession ses) {		
		String email = sess.getUserEmail(ses);
		User user = uservice.findByUserEmail(email);
		return user;
	}
	private void If(boolean b) {
		// TODO Auto-generated method stub	
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView editUser(@PathVariable Long id, SessionStatus status, HttpSession session) {
		if (!sess.isLoggedIn(session, status)) return new ModelAndView("redirect:/");
		ModelAndView mav = new ModelAndView("manager/manageredit", "user", uservice.findByUserId(id));

		return mav;
	}
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
	public String editUser(@ModelAttribute @Valid User user, BindingResult result, 
			@PathVariable Long id, HttpSession session, SessionStatus status) {
		if (!sess.isLoggedIn(session, status)) return "redirect:/";
		if (result.hasErrors()) {
			return "manager/manageredit";
		}
		String hashPassword = Hash.hashPassword(user.getPassword());
		user.setPassword(hashPassword);
		uservice.saveUser(user);
		return "forward:/manager/home";
	}
	
	
	@GetMapping(value = "/leave/list")
	public String list(Model model, HttpSession session, SessionStatus status) {
		if (!sess.isLoggedIn(session, status)) return "redirect:/";
		User u = user(session);
		int currentpage = 0;
		List<Leave> listWithPagination = lservice.getAllLeaves(currentpage, 10,u);
		long top = listWithPagination.size();
		long top1 = top/10+1;
		Leave lea = (Leave) session.getAttribute("currentLeave");
		
		model.addAttribute("leave", lea);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", currentpage);
		model.addAttribute("top1",top1);
		return "manager/manager-leave-history";
	}

	@GetMapping(value = "/leave/navigate")
	public String customlist(@RequestParam(value = "pageNo") int pageNo, Model model, HttpSession session, SessionStatus status) {
		if (!sess.isLoggedIn(session, status)) return "redirect:/";
		User u = user(session);
		List<Leave> listWithPagination = lservice.getAllLeaves(pageNo-1,pagesize,u);
		Leave lea = (Leave) session.getAttribute("currentLeave");		
		long top = listWithPagination.size();
		long top1 = top/pagesize+1;
		model.addAttribute("leave", lea);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", pageNo-1);
		model.addAttribute("top1",top1);
		return "manager/manager-leave-history";
	}

	@GetMapping(value = "/leave/forward/{currentPage}")
	public String arrowlist(@PathVariable(value = "currentPage") String pageNo, Model model, HttpSession session, SessionStatus status) {
		if (!sess.isLoggedIn(session, status)) return "redirect:/";
		Integer i = Integer.parseInt(pageNo);
		if (i == 2)
			i--;
		User u = user(session);
		List<Leave> listWithPagination = lservice.getAllLeaves(i+1,pagesize,u);
		Leave lea = (Leave) session.getAttribute("currentLeave");		
		long top = listWithPagination.size();
		long top1 = top/pagesize+1;
		model.addAttribute("leave", lea);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", i+1);
		model.addAttribute("top1",top1);		
		return "manager/manager-leave-history";
	}

	@GetMapping(value = "/leave/backward/{currentPage}")
	public String backlist(@PathVariable(value = "currentPage")String pageNo ,Model model, HttpSession session, SessionStatus status) {
		if (!sess.isLoggedIn(session, status)) return "redirect:/";
		User u = user(session);
		Integer i = Integer.parseInt(pageNo);
		if (i == 0)
			i++;
		List<Leave> listWithPagination = lservice.getAllLeaves(i-1, pagesize,u);
		Leave lea = (Leave) session.getAttribute("currentLeave");		
		long top = listWithPagination.size();
		long top1 = top/pagesize+1;
		model.addAttribute("leave", lea);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", i-1);
		model.addAttribute("top1",top1);		
		return "manager/manager-leave-history";
	}
	
	@GetMapping(value = "/leave/list/{id}")
	public String list(@PathVariable("id") int id ,Model model, HttpSession session, SessionStatus status) {
		if (!sess.isLoggedIn(session, status)) return "redirect:/";
		this.pagesize= id;
		User u = user(session);
		int currentpage = 0;
		List<Leave> listWithPagination = lservice.getAllLeaves(currentpage, pagesize,u);
		Leave lea = (Leave) session.getAttribute("currentLeave");		
		long top = listWithPagination.size();
		long top1 = top/pagesize+1;
		model.addAttribute("leave", lea);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", currentpage);
		model.addAttribute("top1",top1);
		return "manager/manager-leave-history";
}
	
	//NEW EDIT!
	//Manager Actions
	@RequestMapping(value = "/pendingapprovelist")
	public String list(HttpSession sessions, Model model, SessionStatus status) {
		if (!sess.isLoggedIn(sessions, status)) return "redirect:/";
		if (!checkManager(sessions))
		{
			return "redirect:/logout";
		}
		SessionClass session = (SessionClass)sessions.getAttribute("uSession");
		String emailString = session.getEmail();
		User user = uservice.findByUserEmail(emailString);
		Department department = user.getDepartment();
		ArrayList <Leave> pendingleave = (ArrayList<Leave>) lservice.listLeaveToApprove(department);
		model.addAttribute("leaves", pendingleave);
//		model.addAttribute("leaves", lservice.listLeaveToApprove());
		return "leaves/leave-toapprove";
	}

	@RequestMapping(value="/send/{id}")
	public String managerSetStatus(@PathVariable("id")Long id, 
			Model model, HttpSession session, SessionStatus status) {
		if (!sess.isLoggedIn(session, status)) return "redirect:/";
		List<String> msetstatus = Arrays.asList("APPROVED", "REJECTED");
		model.addAttribute("msetstatus", msetstatus);
		model.addAttribute("leaveapplied",lservice.findLeaveById(id));
		return "leaves/manager-setstatus";
	}
	@PostMapping(value="/confirm")
	public String ApproveRejectLeave(@RequestParam("leaveId")String id, 
		@RequestParam("mset")String mset, 
		@RequestParam("mreason")String mrea, Model model, SessionStatus status, HttpSession session) {
		if (!sess.isLoggedIn(session, status)) return "redirect:/";
			Leave ls = lservice.findLeaveById(Long.parseLong(id));
			User user = ls.getUser();
			Integer count = lservice.countLeaves(ls.getStartDate(), ls.getEndDate(),user).intValue();
			ls.setMreason(mrea);
			LeaveStatus stat = Enum.valueOf(LeaveStatus.class, mset);
			if(stat.equals(LeaveStatus.APPROVED))
			{
				lservice.approveLeave(ls);
				eService.sendEmailApprove(ls);
			}					
			if(stat.equals(LeaveStatus.REJECTED))
			{
				lservice.rejectLeave(ls);	
				eService.sendEmailReject(ls);
				lservice.refundleave(ls, user, count);
			}	
			return "forward:/manager/home";
	}

	
}