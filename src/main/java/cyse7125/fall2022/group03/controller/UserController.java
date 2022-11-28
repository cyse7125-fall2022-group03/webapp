package cyse7125.fall2022.group03.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cyse7125.fall2022.group03.model.User;
import cyse7125.fall2022.group03.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/v1")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
    UserService userService;
	
	@PostMapping("/create")
    public ResponseEntity<JSONObject> createUser(@RequestBody User user){
        logger.info("Post - create a user");

        return userService.createUser(user);
    }

	@GetMapping("/user")
	public ResponseEntity<JSONObject> getUserDetails() {
        logger.info("Get - all details of a user");

		return userService.getUserDetails();
	}

    @PutMapping("/user/updateEmail")
    public ResponseEntity<JSONObject> updateEmail(@RequestBody Map<String, String> request){
        logger.info("Put - update email of a user");

        return userService.updateEmail(request);
    }
	
    @PutMapping("/user/update")
	public ResponseEntity<JSONObject> updateUser(@RequestBody User newUserValues) {
        logger.info("Put - update details of a user");

    	return userService.updateUser(newUserValues);
    }
	

}
