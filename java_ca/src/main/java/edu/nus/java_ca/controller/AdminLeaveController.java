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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.LeaveStatus;
import edu.nus.java_ca.model.SessionClass;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.service.LeaveBalanceService;
import edu.nus.java_ca.service.LeaveService;
import edu.nus.java_ca.service.UserService;

@Controller
@RequestMapping("/AdminLeave")
public class AdminLeaveController {
	@Autowired
	UserService uService;
	
	public Integer pagesize;
	
	@Autowired
	LeaveBalanceService lbService;
	
	@Autowired
	private LeaveService lservice;
	
	
	private User user(HttpSession ses) {
		SessionClass session = (SessionClass)ses.getAttribute("uSession");
		String email = session.getEmail();
		User user = uService.findByUserEmail(email);
		return user;
	}
	
	@GetMapping(value = "/leave/new")
	public ModelAndView newLeave(HttpSession ses) {
		ModelAndView mav = new ModelAndView("admin/admin-new-leave");
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
	public String createNewLeave(@ModelAttribute("leave")@Valid Leave leave, BindingResult result,Model model,HttpSession ses) {
		User u = user(ses);
		ArrayList<LeaveBalance> lb = lbService.findByUser(u);
		ArrayList<String> s = new ArrayList<String>();
		for(LeaveBalance b:lb) {
			s.add(b.getLeavetype());
		}
		model.addAttribute("types",s);
		if (result.hasErrors()){
			return("admin/admin-new-leave");}
		if(lservice.checkDupes(leave.getStartDate(), leave.getEndDate(),u)) {
			model.addAttribute("errormsg", "**You've already Applied the same period**");
			return("admin/admin-new-leave");
		}
	
		Long count = lservice.countLeaves(leave.getStartDate(), leave.getEndDate(),u);
		System.out.println("Total leave days: "+count);
		if(!lservice.deductleave(leave, u, count.intValue())) {
			model.addAttribute("errormsg", "**Leave Application Failed! You don't Have Enough Leave**");
			return("admin/admin-new-leave");
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
		return "redirect:/AdminUser";}
	}
	
	
	
	
	
	
	@RequestMapping({ "/", "" })
	public String dashboard(Model model) {
		List<LeaveBalance> balancelist = lbService.findByLeavetype2("annual");
		model.addAttribute("lblist", balancelist);
		return "admin/leavebalances";
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView editLeave(@PathVariable Integer id) {
		ModelAndView mav = new ModelAndView("admin/leave-form", "lbuser", lbService.findByBalId(id));
		return mav;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editLeave(@ModelAttribute @Valid LeaveBalance lb, BindingResult result) {
		if (result.hasErrors()) {
			return "admin/leave-form";
		}
		lbService.saveLeaveBalance(lb);
		return "forward:/AdminLeave/";
	}
	
	@GetMapping(value = "/leave/list")
	public String list(Model model, HttpSession session) {
		
	
		
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

		return "admin/admin-leave-history";
	}

	@GetMapping(value = "/leave/navigate")
	public String customlist(@RequestParam(value = "pageNo") int pageNo, Model model, HttpSession session) {
		User u = user(session);
		List<Leave> listWithPagination = lservice.getAllLeaves(pageNo-1,pagesize,u);
		Leave lea = (Leave) session.getAttribute("currentLeave");
		
		long top = listWithPagination.size();
		long top1 = top/pagesize+1;

		model.addAttribute("leave", lea);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", pageNo-1);
		model.addAttribute("top1",top1);
		return "admin/admin-leave-history";
	}

	@GetMapping(value = "/leave/forward/{currentPage}")
	public String arrowlist(@PathVariable(value = "currentPage") String pageNo, Model model, HttpSession session) {
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
		
		return "admin/admin-leave-history";
	}

	@GetMapping(value = "/leave/backward/{currentPage}")
	public String backlist(@PathVariable(value = "currentPage")String pageNo ,Model model, HttpSession session) {
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
		
		return "admin/admin-leave-history";
	}
	
	@GetMapping(value = "/leave/list/{id}")
	public String list(@PathVariable("id") int id ,Model model, HttpSession session) {
		
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
		
		return "admin/admin-leave-history";
}
	
	
	
	
	
	
}
