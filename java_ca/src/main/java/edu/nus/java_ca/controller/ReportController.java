package edu.nus.java_ca.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import edu.nus.java_ca.model.FilterDate;
import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.LeaveReport;
import edu.nus.java_ca.model.PageDetails;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.service.LeaveReportService;
import edu.nus.java_ca.service.LeaveService;
import edu.nus.java_ca.service.ReportService;
import edu.nus.java_ca.service.SessionManagement;
import edu.nus.java_ca.service.UserService;

@Controller
public class ReportController {

	@Autowired
	LeaveService lService;
	
	@Autowired
	ReportService rService;
	
	@Autowired
	SessionManagement sess;
	
	@Autowired
	UserService uService;
	
	@Autowired
	LeaveReportService lrService;
	
	private List<Leave> cachedLeave;
	
	private List<Leave> cachedLeaveByDate;
	
	private boolean visited;
	
	private String endStr;
	private String startStr;
	
	
	private int rowsPerPage = 3;
	
	@RequestMapping("/testing123")
	public String test() {
		return "manager/test";
	}
	
	// get all the leave
	@RequestMapping("/manager/report/all")
	public String listAll(HttpSession session, Model model, SessionStatus status) {
		if(!sess.isLoggedIn(session, status)) return "redirect:/";
		visited = false;
		PageDetails pageD = new PageDetails();
		setPageDetails(pageD, 1);
		
		List<Leave> all = getLeaveList(session);
		if(all.size() < rowsPerPage) {
			pageD.setNext(null);
		}
		cachedLeave = all;
		List<Leave> filtered = paginateView(all, 1);
		if(filtered.size() == 0) {
			all = null;
		}
		model.addAttribute("leaves", filtered);
		model.addAttribute("pageDetails", pageD);
		return "manager/test";
	}
	
	// filter paginated pages for leaves
	@RequestMapping("/manager/report")
	public String listWholeReportByPages(@RequestParam("page") Integer page, HttpSession session, Model model,
			SessionStatus status) {
		if(!sess.isLoggedIn(session, status)) return "redirect:/";
		List<Leave> filtered = null;
		FilterDate fd = null;
		if(visited == false) {
			filtered = paginateView(cachedLeave, page);
		} else {
			filtered = paginateView(cachedLeaveByDate, page);
			fd = new FilterDate();
			fd.setStartDate(startStr); fd.setEndDate(endStr);
		}
		
		model.addAttribute("leaves", filtered);
		PageDetails pageD = new PageDetails();
		pageD.setPage(page); pageD.setPrev(page-1); pageD.setNext(page+1);
		// check the filtered if size is less than normal
		if(filtered.size() < rowsPerPage) {
			pageD.setNext(null);
		}
		model.addAttribute("filtering", fd);
		model.addAttribute("pageDetails", pageD);
		return "manager/test";
	}
	
	// filter the leaves by date duration, refreshes from database every time it is called
	@RequestMapping("/manager/report/filterDate")
	public String filterByDate(@RequestParam("startDate") String start, @RequestParam("endDate") String end,
			Model model, HttpSession session, SessionStatus status) {
		if(!sess.isLoggedIn(session, status)) return "redirect:/";
		visited = true;
		System.out.println(start + " " + end);
		cachedLeaveByDate = getLeaveList(session);
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-d");
		LocalDate localDateStart = LocalDate.parse(start, df);
		LocalDate localDateEnd = LocalDate.parse(end, df);
		FilterDate fd = new FilterDate(); fd.setEndDate(end); fd.setStartDate(start);
		this.startStr = start; this.endStr = end;
		// modify local cached list with filtered date list
		cachedLeaveByDate = filterByDate(cachedLeaveByDate, localDateStart, localDateEnd);
		return "redirect:/manager/report?page=1";

	}
	
	
	@RequestMapping("/manager/exportCSV")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		if(visited == true) {
			lrService.exportToCSV(response, cachedLeaveByDate);
		}else {
			lrService.exportToCSV(response, cachedLeave);
		}
	}
	
	private List<Leave> filterByDate(List<Leave> list, LocalDate localDateStart, LocalDate localDateEnd){
		return list.stream()
		.filter(x -> {
			int first = x.getStartDate().compareTo(localDateStart); 
			int second = x.getStartDate().compareTo(localDateEnd); 
			int third = x.getEndDate().compareTo(localDateEnd); 
			int fourth = x.getEndDate().compareTo(localDateStart); 
			if((first >= 0) && (second <= 0) || (third <= 0) && (fourth >=0)){
				return true;
			}
			return false;
		}).collect(Collectors.toList());
	}
	
	private List<Leave> paginateView(List<Leave> leaves, int val){
		val -= 1;
		List<Leave> filtered = leaves.stream().skip(val*rowsPerPage).limit(rowsPerPage)
		.collect(Collectors.toList());
		return filtered;
	}
	
	private static void setPageDetails(PageDetails pd, int page) {
		pd.setPage(page);
		pd.setPrev(page - 1);
		pd.setNext(page + 1);
	}
	
	private List<Leave> getLeaveList(HttpSession session){
		User manager = uService.findByUserEmail(sess.getUserEmail(session));
		return rService.findLeaveByApprovingOfficer(manager);
	}
	
	
}
