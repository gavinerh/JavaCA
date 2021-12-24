package edu.nus.java_ca.controller;


import java.text.ParseException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.SessionClass;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.service.LeaveService;
import edu.nus.java_ca.service.LeaveServiceImpl;
import edu.nus.java_ca.service.SessionManagement;
import edu.nus.java_ca.service.UserService;
import edu.nus.java_ca.service.UserServiceImpl;

@Controller
@RequestMapping("/leave")
public class LeaveController {

	@Autowired
	SessionManagement sess;
	
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
	public Integer MM, YY;
	
	
//	@RequestMapping(value="/leaves/all")
//	public String listAll(Model model) {
//		model.addAttribute("leaves", lservice.listAllLeaves());
//		return "leaves/allleaves";
//	}
 
	//Subordinate leave history
	@RequestMapping(value="/leaves/empl-leavehistory")
	public String empLeaveHistSearchPage(Model model, SessionStatus status, HttpSession ses) {
		if (!sess.isLoggedIn(ses, status)) return "redirect:/";
		model.addAttribute("leave", new Leave());
		return "leaves/empl-leavehistory";
	}
	@PostMapping(value="/search")
	public String searchLeavesByUserId(@RequestParam("user.userId") 
		String UserId, Model model, SessionStatus status, HttpSession ses) {
		if (!sess.isLoggedIn(ses, status)) return "redirect:/";
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

//			User u = user(session);
			this.pagesize = 10;
			int currentpage = 0;
			int num = 10;
			if(YY==null) {this.YY=0;}
			if(MM==null) {this.MM=0;}
			List<Leave> listWithPagination = lservice.getMRLeaves(currentpage, num, YY, MM);
			int top = listWithPagination.size();
			int top1 = (top/num)+1;
			
			model.addAttribute("pageSize", this.pagesize);
			model.addAttribute("mvtleaves", listWithPagination);
			model.addAttribute("currentPage", currentpage);
			model.addAttribute("top1",top1);
			
			return "leaves/mvt-reg";
		}
		@RequestMapping(value="/view") 
		public String viewMvtRegChooseMth(@RequestParam("mth")String mth, 
				@RequestParam("yr")String yr, Model model, 
				HttpSession session) throws ParseException {				
				
			this.MM=Integer.parseInt(mth);
			this.YY=Integer.parseInt(yr);
			
//			User u = user(session);
			this.pagesize = 10;
			int currentpage = 0;
			int num = 10;
			List<Leave> listWithPagination = lservice.getMRLeaves(currentpage, num, YY, MM);
			int top = listWithPagination.size();
			int top1 = (top/num)+1;
			model.addAttribute("pageSize", this.pagesize);
			model.addAttribute("mvtleaves", listWithPagination);
			model.addAttribute("currentPage", currentpage);
			model.addAttribute("top1",top1);
			
			return "forward:/leave/leaves/mvt-reg/";
		}
		
		// (PAGINATION)
		@GetMapping(value = "/mvtreg/navigate")
		public String customlist(@RequestParam(value = "pageNo") int pageNo, Model model, HttpSession session) {
//			User u = user(session);
			List<Leave> listWithPagination = lservice.getMRLeaves(pageNo-1, pagesize, YY, MM);
//			List<Leave> listWithPagination = lservice.getAllLeaves(pageNo-1,pagesize,u);
			List<Leave> MRList = lservice.findLeavesByYearandMonth(YY, MM);
			int top = MRList.size();
			int top1;
			if (top % pagesize>0)
			{
				 top1 = (top/pagesize)+1;}
			else {
				 top1 = top/pagesize;
			}
			model.addAttribute("pageSize", this.pagesize);
			model.addAttribute("mvtleaves", listWithPagination);
			model.addAttribute("currentPage", pageNo-1);
			model.addAttribute("top1",top1);
			return "leaves/mvt-reg";
		}
		
		@GetMapping(value = "/mvtreg/forward/{currentPage}")
		public String arrowlist(@PathVariable(value = "currentPage") String pageNo, Model model, HttpSession session) {
			Integer i = Integer.parseInt(pageNo);
			List<Leave> MRList = lservice.findLeavesByYearandMonth(YY, MM);
			int top = MRList.size();
			int top1;
			if (top % pagesize>0)
			{
				 top1 = (top/pagesize)+1;}
			else {
				 top1 = top/pagesize;
			}
				if (i >= top1-1)
					i--;	
			List<Leave> listWithPagination = lservice.getMRLeaves(i+1,pagesize, YY, MM);
//			List<Leave> listWithPagination = lservice.getAllLeaves(i+1,pagesize,u);
		    model.addAttribute("pageSize", this.pagesize);
			model.addAttribute("mvtleaves", listWithPagination);
			model.addAttribute("currentPage", i+1);
			model.addAttribute("top1",top1);		
			
			return "leaves/mvt-reg";
		}

		@GetMapping(value = "/mvtreg/backward/{currentPage}")
		public String backlist(@PathVariable(value = "currentPage")String pageNo ,Model model, HttpSession session) {
//			User u = user(session);
			Integer i = Integer.parseInt(pageNo);
			if (i == 0)
				i++;
			List<Leave> listWithPagination = lservice.getMRLeaves(i-1,pagesize, YY, MM);
//			List<Leave> listWithPagination = lservice.getAllLeaves(i-1, pagesize,u);
			List<Leave> MRList = lservice.findLeavesByYearandMonth(YY, MM);
			int top = MRList.size();
			int top1;
			if (top % pagesize>0)
			{
				 top1 = (top/pagesize)+1;}
			else {
				 top1 = top/pagesize;
			}
			model.addAttribute("pageSize", this.pagesize);
			model.addAttribute("mvtleaves", listWithPagination);
			model.addAttribute("currentPage", i-1);
			model.addAttribute("top1",top1);
			
			return "leaves/mvt-reg";
		}
		
		@RequestMapping(value = "/mvt-reg/{id}")
		public String list(@PathVariable("id") int id, Model model, 
				HttpSession session) {
			
			this.pagesize= id;
//			User u = user(session);
			int currentpage = 0;
			List<Leave> MRList = lservice.findLeavesByYearandMonth(YY, MM);
			int top = MRList.size();
			int top1;
			if (top % pagesize>0)
			{
				 top1 = (top/pagesize)+1;}
			else {
				 top1 = top/pagesize;
			}
			List<Leave> listWithPagination = lservice.getMRLeaves(currentpage, pagesize, YY, MM);
			model.addAttribute("pageSize", this.pagesize);
			model.addAttribute("mvtleaves", listWithPagination);
			model.addAttribute("currentPage", currentpage);
			model.addAttribute("top1",top1);
			
			return "leaves/mvt-reg";
		}
		
		
	}
