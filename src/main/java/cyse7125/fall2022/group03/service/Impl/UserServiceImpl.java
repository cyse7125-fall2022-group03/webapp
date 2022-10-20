package cyse7125.fall2022.group03.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cyse7125.fall2022.group03.model.User;
import cyse7125.fall2022.group03.repository.UserRepository;
import cyse7125.fall2022.group03.service.UserService;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
//import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service; 

@Service("userService")
public class UserServiceImpl implements UserService {
    
    @Autowired
    UserRepository userRepository;

    @SuppressWarnings("rawtypes")
    ResponseEntity BadRequest = new ResponseEntity<>(JSONObject.parseObject(JSON.toJSONString(null)), HttpStatus.BAD_REQUEST);
    @SuppressWarnings("rawtypes")
    ResponseEntity NoContent = new ResponseEntity<>(JSONObject.parseObject(JSON.toJSONString(null)), HttpStatus.NO_CONTENT);
    
    @SuppressWarnings("rawtypes")
    ResponseEntity EmailConstraint = new ResponseEntity<>(JSONObject.parseObject("{\"error\":\"Email Constraints not met\"}"), HttpStatus.BAD_REQUEST);
    @SuppressWarnings("rawtypes")
    ResponseEntity DupEmail = new ResponseEntity<>(JSONObject.parseObject("{\"error\":\"Email Already Exists\"}"), HttpStatus.BAD_REQUEST);
    @SuppressWarnings("rawtypes")
    ResponseEntity PwdConstraint = new ResponseEntity<>(JSONObject.parseObject("{\"error\":\"Password Constraints not met\"}"), HttpStatus.BAD_REQUEST);
    
    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<JSONObject> createUser(User user) {
        
        user.setAccountCreated(String.valueOf(new Date()));
        user.setAccountUpdated(String.valueOf(new Date()));
        
        if (!checkEmailFormat(user.getEmail())){
            return EmailConstraint;
        }
        
        //check email already exists
        if (userRepository.existsByEmail(user.getEmail())){
            return DupEmail;
        }
        
        //password constraints
        String password = user.getPassword();
        if (!checkPassword(password)){
            return PwdConstraint;
        }

        // decrypting password
        //String newPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        //user.setPassword(newPassword);

        try{
            System.out.println(JSON.toJSONString(user));
            userRepository.save(user);

        }catch (Exception e){
            e.printStackTrace();
            return BadRequest;
        }
        System.out.println("5");
        return new ResponseEntity<>(JSONObject.parseObject(JSON.toJSONString(user)), HttpStatus.CREATED);
    }
    
    public static boolean checkPassword(String password){
        String pattern = "^[0-9A-Za-z]{8,16}$";
        boolean isMatch = Pattern.matches(pattern,password);
        return isMatch;
    }

    public static boolean checkEmailFormat(String email){
        String pattern = "^(.+)@(.+)$";
        boolean isMatch = Pattern.matches(pattern,email);
        return isMatch;
    }
 
    
}
