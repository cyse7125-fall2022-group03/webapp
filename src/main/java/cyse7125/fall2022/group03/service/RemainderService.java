package cyse7125.fall2022.group03.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cyse7125.fall2022.group03.model.Remainder;

@Service
public interface RemainderService {
    
    ResponseEntity<JSONObject> createRemainder(Remainder remainder);

}
