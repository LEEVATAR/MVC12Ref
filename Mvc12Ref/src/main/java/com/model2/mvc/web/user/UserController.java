package com.model2.mvc.web.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.UserService;
import com.model2.mvc.service.domain.User;

import jakarta.servlet.http.HttpSession;


@Controller
public class UserController {
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;

	public UserController(){
	    System.out.println(this.getClass());
	}

	@Value("${common.pageUnit}")
	int pageUnit;

	@Value("${common.pageSize}")	
	int pageSize;

	@GetMapping("/user/addUser")
	public String addUser() throws Exception{

	    System.out.println("/user/addUser : GET");
	    
	    return "redirect:/user/addUserView.jsp";
	}

	@PostMapping("/user/addUser")
	public String addUser( @ModelAttribute("user") User user ) throws Exception {

	    System.out.println("/user/addUser : POST");
	    userService.addUser(user);
	    
	    return "redirect:/user/loginView.jsp";
	}


	@GetMapping("/user/getUser")
	public String getUser( @RequestParam("userId") String userId , Model model ) throws Exception {
	    
	    System.out.println("/user/getUser : GET");
	    User user = userService.getUser(userId);
	    model.addAttribute("user", user);
	    
	    return "forward:/user/getUser.jsp";
	}


	@GetMapping("/user/updateUser")
	public String updateUser( @RequestParam("userId") String userId , Model model ) throws Exception{

	    System.out.println("/user/updateUser : GET");
	    User user = userService.getUser(userId);
	    model.addAttribute("user", user);
	    
	    return "forward:/user/updateUser.jsp";
	}

	@PostMapping("/user/updateUser")
	public String updateUser( @ModelAttribute("user") User user , Model model , HttpSession session) throws Exception{

	    System.out.println("/user/updateUser : POST");
	    userService.updateUser(user);
	    
	    String sessionId=((User)session.getAttribute("user")).getUserId();
	    if(sessionId.equals(user.getUserId())){
	        session.setAttribute("user", user);
	    }
	    
	    return "redirect:/user/getUser?userId="+user.getUserId();
	}


	@GetMapping("/user/login")
	public String login() throws Exception{
	    
	    System.out.println("/user/logon : GET");

	    return "redirect:/user/loginView.jsp";
	}

	@PostMapping("/user/login")
	public String login(@ModelAttribute("user") User user , HttpSession session ) throws Exception{
	    
	    System.out.println("/user/login : POST");
	    User dbUser=userService.getUser(user.getUserId());
	    
	    if( user.getPassword().equals(dbUser.getPassword())){
	        session.setAttribute("user", dbUser);
	    }
	    
	    return "redirect:/index.jsp";
	}
	    

	@GetMapping("/user/logout")
	public String logout(HttpSession session ) throws Exception{
	    
	    System.out.println("/user/logout : POST");
	    
	    session.invalidate();
	    
	    return "redirect:/index.jsp";
	}

	@PostMapping("/user/checkDuplication")
	@ResponseBody
	public boolean checkDuplication(@RequestParam String userId) throws Exception {
	boolean result = userService.checkDuplication(userId);
	return result;
	}

	@RequestMapping(value = "listUser")
	public String listUser(@ModelAttribute("search") Search search, Model model) throws Exception {
	    if (search.getCurrentPage() == 0) {
	        search.setCurrentPage(1);
	    }
	    search.setPageSize(pageSize);

	    List<Map<String, Object>> resultList = userService.getUserList(search);
	    List<User> userList = new ArrayList<User>();
	    for (Map<String, Object> map : resultList) {
	        User user = (User) map.get("user");
	        userList.add(user);
	    }

	    int totalCount = (int) resultList.get(0).get("totalCount");

	    Page resultPage = new Page(search.getCurrentPage(), totalCount, pageUnit, pageSize);

	    model.addAttribute("list", userList);
	    model.addAttribute("resultPage", resultPage);
	    model.addAttribute("search", search);

	    return "user/listUser";
	}
	@RequestMapping(value = "autocomplete", produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<Map<String, Object>> autocomplete(@RequestParam("keyword") String keyword) throws Exception {
	    List<Map<String, Object>> resultList = userService.autocomplete(keyword);
	    return resultList;
	}
}