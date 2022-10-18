package cyse7125.fall2022.group03.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cyse7125.fall2022.group03.dao.UserDao;
import cyse7125.fall2022.group03.model.User;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserDao userDao;
	
	@PostMapping("/create")
	public String create(@RequestBody User user) {
		userDao.createUser(user);
		return "success";
	}

	/*@GetMapping("/login")
	public boolean validateLogin() {
		return userDao.validateLogin();
	}*/
	
	

}
