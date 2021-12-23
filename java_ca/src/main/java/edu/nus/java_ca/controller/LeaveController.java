package edu.nus.java_ca.controller;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.model.LeaveStatus;
import edu.nus.java_ca.model.Position;
import edu.nus.java_ca.model.SessionClass;
import edu.nus.java_ca.service.UserService;
import edu.nus.java_ca.service.UserServiceImpl;
import edu.nus.java_ca.service.LeaveBalanceService;
import edu.nus.java_ca.service.LeaveService;
import edu.nus.java_ca.service.LeaveServiceImpl;
import edu.nus.java_ca.service.SessionManagement;

@Controller
@RequestMapping("/leave")
public class LeaveController {

	
	@Autowired
	private LeaveService lservice;
	@Autowired
	public void setLeaveService(LeaveServiceImpl lserviceImpl) {
		this.lservice = lserviceImpl;
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
	public Integer pagesize;
	
	
//	@RequestMapping(value="/leaves/all")
//	public String listAll(Model model) {
//		model.addAttribute("leaves", lservice.listAllLeaves());
//		return "leaves/allleaves";
//	}
 
	//Subordinate leave history
	@RequestMapping(value="/leaves/empl-leavehistory")
	public String empLeaveHistSearchPage(Model model) {
		model.addAttribute("leave", new Leave());
		return "leaves/empl-leavehistory";
	}
	@PostMapping(value="/search")
	public String searchLeavesByUserId(@RequestParam("user.userId") 
		String UserId, Model model) {
		ArrayList<Leave> lls = (ArrayList<Leave>) 
				lservice.listLeavesByUserId(Long.parseLong(UserId));
		model.addAttribute("emleaves", lls);
		return "forward:/leave/leaves/empl-leavehistory";
	}
	
	//Movement Register
	@RequestMapping(value="/leaves/mvt-reg")
	public String viewMvtReg(Model model, HttpSession session) {
		model.addAttribute("leave", new Leave());	
		List<Integer> mthlist = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11);
		int year = Year.now().getValue();
		List<Integer> yrlist = Arrays.asList(year-1, year, year+1);
		model.addAttribute("mthlist", mthlist);
		model.addAttribute("yrlist", yrlist);
		
		//NEW ADDITION? (PAGINATION)
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
	
	
	
	//NEW ADDITION (PAGINATION STARTS)
	@GetMapping(value = "/view/{id}")
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
		return "leaves/mvt-reg";
	}

	//Pagination
	@GetMapping(value = "/navigate")
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
		return "leaves/mvt-reg";
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
		return "leaves/mvt-reg";
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
		return "leaves/mvt-reg";
	}
	//NEW ADDITION (PAGINATION)ENDS
	
	
	
}
