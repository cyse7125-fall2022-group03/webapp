package cyse7125.fall2022.group03.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cyse7125.fall2022.group03.model.User;
import cyse7125.fall2022.group03.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
    @Autowired
    UserService userService;
	
	@PostMapping("/create")
    public ResponseEntity<JSONObject> createUser(@RequestBody User user){

        return userService.createUser(user);
    }

	/*@GetMapping("/login")
	public boolean validateLogin() {
		return userDao.validateLogin();
	}*/
	
	

}
