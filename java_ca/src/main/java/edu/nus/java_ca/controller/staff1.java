package edu.nus.java_ca.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import edu.nus.java_ca.model.Position;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.repository.UserRepository;
import edu.nus.java_ca.service.SessionManagement;
import edu.nus.java_ca.service.UserService;
import edu.nus.java_ca.service.UserServiceImpl;

@Controller
@RequestMapping(value = "/staff1")
public class staff1 {

	@Autowired
	UserRepository urepo;
	
	@Autowired
	UserService Uservice;
	
	@Autowired
	SessionManagement sess;
	
	@Autowired
	public void setUserService(UserServiceImpl UserviceImpl) {
		this.Uservice = UserviceImpl;
	}
	
	
	@RequestMapping({"/main"})
	public String dashboard(Model model,HttpSession session, SessionStatus status) {
		String	em = sess.getUserEmail(session);
	    User result = Uservice.findByUserEmail(em);
		System.out.println(result.getLastName());
		model.addAttribute("usernow", result);	
	    List<User> userlist = Uservice.findAll();
		userlist.remove(result);
		model.addAttribute("userlist", userlist);
	    return "staff/staff"; 
		}
	
	
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView editUser(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView("staff/staff-edit", "user", Uservice.findByUserId(id));

		return mav;
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
	public String editUser(@ModelAttribute @Valid User user, BindingResult result, 
			@PathVariable Long id) {
		if (result.hasErrors()) {
			return "staff/staff-edit";
		}
		Uservice.saveUser(user);
		return "forward:/staff1/main";
	}
	
	
	
	
	
	
}
