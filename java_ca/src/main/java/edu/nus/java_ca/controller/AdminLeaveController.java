package edu.nus.java_ca.controller;

import java.time.LocalDate;
import java.util.ArrayList;
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

import edu.nus.java_ca.model.Holidays;
import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.LeaveStatus;
import edu.nus.java_ca.model.SessionClass;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.repository.HolidayRepo;
import edu.nus.java_ca.service.EmailService;
import edu.nus.java_ca.service.HolidayService;
import edu.nus.java_ca.service.LeaveBalanceService;
import edu.nus.java_ca.service.LeaveService;
import edu.nus.java_ca.service.SessionManagement;
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
	private EmailService eService;
	
	@Autowired
	private LeaveService lservice;
	
	@Autowired
	private HolidayService hService;

	@Autowired
	SessionManagement sess;
	private ArrayList<String> s = new ArrayList<>();
	private ArrayList<String> t = new ArrayList<>();
	private String type;
	
	private User user(HttpSession ses) {
		SessionClass session = (SessionClass)ses.getAttribute("uSession");
		String email = session.getEmail();
		User user = uService.findByUserEmail(email);
		return user;
	}
	
	@GetMapping(value = "/leave/new")
	public ModelAndView newLeave(HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return new ModelAndView("redirect:/");
		User u = user(ses);
		/**find leaves for current user and his leave types*/
		ArrayList<LeaveBalance> lb = lbService.findByUser(u);
		s.clear();
		t.clear();
		/**Save in private array in the controller class*/
		for(LeaveBalance b:lb) {
			s.add(b.getLeavetype().toUpperCase());
			t.add(b.getLeavetype().toUpperCase()+":\t"+b.getBalance().toString());
		}
		
		ModelAndView mav = new ModelAndView("admin/admin-new-leave");
		mav.addObject("leave", new Leave());
		mav.addObject("types",s);
		mav.addObject("bal",t);
		return mav;
	}

	@PostMapping(value = "/leave/new")
	public String createNewLeave(@ModelAttribute("leave")@Valid Leave leave, BindingResult result,Model model,HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return "redirect:/";
		User u = user(ses);
		
		model.addAttribute("types",s);
		model.addAttribute("bal",t);
		
		if (result.hasErrors()){
			return("admin/admin-new-leave");}
		
		/**Check for Duplication and return error**/
		if(lservice.checkDupes(leave.getStartDate(), leave.getEndDate(),u)) {
			model.addAttribute("errormsg", "**You've already Applied the same period**");
			return("admin/admin-new-leave");
		}
		
		//check for approving officer to send email to
		if(u.getApprovingOfficer() == null) {
			model.addAttribute("errormsg", "**You have no approving officer to approve your leave**");
			return("staff/staff-new-leave");
		}
		
		/**Count the number of leaves and return error if the user has not enough leave**/
		Long count = lservice.countLeaves(leave.getStartDate(), leave.getEndDate(),u);
		System.out.println("Total leave days: "+count);
		if(count==0) {	model.addAttribute("errormsg", "**Leave Application Failed!! You Applied on Holidays**");
		return("Admin/admin-new-leave");}
		if(!lservice.deductleave(leave, u, count.intValue())) {
			model.addAttribute("errormsg", "**Leave Application Failed! You don't Have Enough Leave**");
			return("AdminLeave/admin-new-leave");
		}
		else {
		LocalDate now = LocalDate.now();
		leave.setAppliedDate(now);
		leave.setLeavetaken(count.intValue());
		leave.setStatus(LeaveStatus.APPLIED);
		
		leave.setUser(u);
		lservice.createLeave(leave);
		eService.sendEmailApply(leave);
		String message = "New Leave " + leave.getLeaveId()+" Created ";
		System.out.println(message);
		u.getLb().forEach(System.out::println);
		return "redirect:/AdminUser";}
	}
	@GetMapping(value = "/leave/edit/{id}")
	public ModelAndView editLeavePage(@PathVariable ("id")long id,HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return new ModelAndView("redirect:/");
		User u = user(ses);
		/**find leaves for current user and his leave types*/
		ArrayList<LeaveBalance> lb = lbService.findByUser(u);
		s.clear();
		t.clear();
		/**Save in private array in the controller class*/
		for(LeaveBalance b:lb) {
			s.add(b.getLeavetype().toUpperCase());
			t.add(b.getLeavetype().toUpperCase()+":\t"+b.getBalance().toString());
		}
		Leave l = lservice.findLeaveById(id);
		this.type=l.getType();
		ModelAndView mav = new ModelAndView("admin/leave-edit");
		mav.addObject("leave", l);
		mav.addObject("types",s);
		mav.addObject("bal",t);
		return mav;
	}

	@PostMapping(value = "/leave/edit")
	public String editLeave(@ModelAttribute ("leave")@Valid Leave l, BindingResult result, Model model,HttpSession ses, SessionStatus status){
		if (!sess.isLoggedIn(ses, status)) return "redirect:/";
		User u = user(ses);
		model.addAttribute("types",s);
		model.addAttribute("bal",t);
		if (result.hasErrors()) {
			return "admin/leave-edit";}
		if(lservice.checkDupes(l.getStartDate(), l.getEndDate(),u)) {
			model.addAttribute("errormsg", "**You've already Applied the same period**");
			return("admin/leave-edit");
		}
		if(u.getApprovingOfficer() == null) {
			model.addAttribute("errormsg", "**You have no approving officer to approve your leave**");
			return("admin/leave-edit");
		}
		Long count = lservice.countLeaves(l.getStartDate(), l.getEndDate(),u);
		System.out.println("Total leave days: "+count);
		if(count==0) {	model.addAttribute("errormsg", "**Leave Application Failed!! You Applied on Holidays**");
		return("admin/leave-edit");}
		if(!lservice.deductleave(l, u, count.intValue())) {
			model.addAttribute("errormsg", "**Leave Application Failed! You don't Have Enough Leave**");
			return("admin/leave-edit");
		}
		else {
		lservice.refundleave(this.type, u, l.getLeavetaken());
		l.setStatus(LeaveStatus.UPDATED);
		l.setLeavetaken(count.intValue());
		LocalDate now = LocalDate.now();
		l.setAppliedDate(now);
		lservice.changeLeave(l);
		return "redirect:/home";
		}
	}
	
	@RequestMapping(value = "/holiday")
	public String holiday(Model model) {
		List<Holidays> holidays = hService.findAll();
		model.addAttribute("holidays", holidays);
		return "admin/holiday";
	}
	
	@RequestMapping(value = "/holiday/new")
	public ModelAndView newHoliday(HttpSession ses) {
		ModelAndView mav = new ModelAndView("admin/holiday-form");
		
		mav.addObject("holiday", new Holidays());
		
		return mav;
	}
	
	@RequestMapping(value = "/holiday/new", method = RequestMethod.POST)
	public String saveMember(Model model, @ModelAttribute("holiday")  Holidays holidays,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "admin/holiday-form";
		}
		
			hService.createHoliday(holidays);
		

		return "forward:/AdminLeave/holiday";
	}
	
	@RequestMapping(value = "holiday/delete/{id}")
	public String deleteHoliday(@PathVariable("id") Integer id, HttpSession ses, SessionStatus status) {
		
		hService.deleteById(id);


		return "forward:/AdminLeave/holiday";
	}
	@RequestMapping({ "/", "" })
	public String dashboard(Model model, HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return "redirect:/";
		List<LeaveBalance> balancelist = lbService.findByLeavetype2("annual");
		model.addAttribute("lblist", balancelist);
		return "admin/leavebalances";
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView editLeave(@PathVariable Integer id, HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return new ModelAndView("redirect:/");
		ModelAndView mav = new ModelAndView("admin/leave-form", "lbuser", lbService.findByBalId(id));
		return mav;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editLeave(@ModelAttribute @Valid LeaveBalance lb, BindingResult result, HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return "redirect:/";
		if (result.hasErrors()) {
			return "admin/leave-form";
		}
		lbService.saveLeaveBalance(lb);
		return "forward:/AdminLeave/";
	}
	
	@GetMapping(value = "/leave/balance")
	public ModelAndView checkbalance(HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return new ModelAndView("redirect:/");
		User u = user(ses);
		ArrayList<LeaveBalance> lb = lbService.findByUser(u);
		ModelAndView mav = new ModelAndView("admin/leave-balance");
		mav.addObject("lbalance",lb);
		return mav;
	}
	
	@RequestMapping(value = "/leave/list")
	public String list(Model model, HttpSession session, SessionStatus status) {
		if (!sess.isLoggedIn(session, status)) return "redirect:/";
	
		
		this.pagesize = 10;
		User u = user(session);
		int currentpage = 0;
		int num = 10;
		List<Leave> listWithPagination = lservice.getAllLeaves(currentpage, num,u);
		int top = listWithPagination.size();
		int top1 = (top/num)+1;
		model.addAttribute("pageSize", this.pagesize);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", currentpage);
		model.addAttribute("top1",top1);

		return "admin/admin-leave-history";
	}

	@RequestMapping(value = "/leave/navigate")
	public String customlist(@RequestParam(value = "pageNo") int pageNo, Model model, HttpSession session) {
		User u = user(session);
		List<Leave> listWithPagination = lservice.getAllLeaves(pageNo-1,pagesize,u);
		List<Leave> userList =lservice.findByUser(u);
		int top = userList.size();
		int top1;
		if (top % pagesize>0)
		{
			 top1 = (top/pagesize)+1;}
		else {
			 top1 = top/pagesize;
		}
		
		model.addAttribute("pageSize", this.pagesize);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", pageNo-1);
		model.addAttribute("top1",top1);
		return "admin/admin-leave-history";
	}

	@GetMapping(value = "/leave/forward/{currentPage}")
	public String arrowlist(@PathVariable(value = "currentPage") String pageNo, Model model, HttpSession session, SessionStatus status) {
		if (!sess.isLoggedIn(session, status)) return "redirect:/";
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
    model.addAttribute("pageSize", this.pagesize);
	model.addAttribute("leaves", listWithPagination);
	model.addAttribute("currentPage", i+1);
	model.addAttribute("top1",top1);
	
		
		return "admin/admin-leave-history";
	}

	@GetMapping(value = "/leave/backward/{currentPage}")
	public String backlist(@PathVariable(value = "currentPage")String pageNo ,Model model, HttpSession session, SessionStatus status) {
		if (!sess.isLoggedIn(session, status)) return "redirect:/";
		User u = user(session);
		Integer i = Integer.parseInt(pageNo);
		if (i == 0)
			i++;
		List<Leave> listWithPagination = lservice.getAllLeaves(i-1,pagesize,u);
		List<Leave> userList =lservice.findByUser(u);
		int top = userList.size();
		int top1;
		if (top % pagesize>0)
		{
			 top1 = (top/pagesize)+1;}
		else {
			 top1 = top/pagesize;
		}
		model.addAttribute("pageSize", this.pagesize);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", i-1);
		model.addAttribute("top1",top1);
		
		
		return "admin/admin-leave-history";
	}
	
	@GetMapping(value = "/leave/list/{id}")
	public String list(@PathVariable("id") int id ,Model model, HttpSession session, SessionStatus status) {
		if (!sess.isLoggedIn(session, status)) return "redirect:/";
		System.out.println("Page Size:" + id);
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
		model.addAttribute("pageSize", this.pagesize);
		model.addAttribute("leaves", listWithPagination);
		model.addAttribute("currentPage", currentpage);
		model.addAttribute("top1",top1);
		
		return "admin/admin-leave-history";
}
	
	
	
	
	
	
}