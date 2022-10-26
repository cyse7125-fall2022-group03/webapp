package cyse7125.fall2022.group03.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import cyse7125.fall2022.group03.model.User;

@Service
public interface UserService {
    
    ResponseEntity<JSONObject> createUser(User user);
    ResponseEntity<JSONObject> getUserDetails();
    ResponseEntity<JSONObject> updateEmail(Map<String, String> request);
}
