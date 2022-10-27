package cyse7125.fall2022.group03.service.Impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cyse7125.fall2022.group03.model.Lists;
import cyse7125.fall2022.group03.model.ListsIdentity;
import cyse7125.fall2022.group03.model.User;
import cyse7125.fall2022.group03.repository.ListsRepository;
import cyse7125.fall2022.group03.service.ListsService;

@Service
public class ListsServiceImpl implements ListsService {
    
    @Autowired
    ListsRepository listsRepository;
    
    @Autowired
    UserServiceImpl userServiceImpl; 

    @Override
    public ResponseEntity<JSONObject> createList(Lists newList) {
        
        try {
            
            User user = userServiceImpl.getCurrentUser();
            
            Lists listToPut = new Lists(user.getUserId(), newList.getName(), String.valueOf(new Date()), String.valueOf(new Date()));
            
            newList = listsRepository.save(listToPut);
    
        }catch (Exception e){
            e.printStackTrace();
            return generateResponse(null, HttpStatus.BAD_REQUEST);
        }
        
        return generateResponse(newList, HttpStatus.CREATED);
    }
    
    

    @Override
    public ResponseEntity<JSONObject> getAllLists() {
        
        User user = userServiceImpl.getCurrentUser();
        List<Lists> listOfLists = listsRepository.findByUserId(user.getUserId());
        
        if( listOfLists == null || listOfLists.isEmpty()) {
            return generateResponse("{\"error\":\"You dont have any list\"}", HttpStatus.BAD_REQUEST);
        }
        
        
        return generateResponse(listOfLists, HttpStatus.CREATED);
    }
    
    @Override
    public ResponseEntity<JSONObject> getAList(String id) {
        
        User user = userServiceImpl.getCurrentUser();
        Optional<Lists> list = listsRepository.findById(new ListsIdentity(user.getUserId(), id));
        
        if( list == null || list.isEmpty()) {
            return generateResponse("{\"error\":\"You dont have such a list\"}", HttpStatus.BAD_REQUEST);
        }
        
        List<Lists> actualLists = list.isPresent() ? Collections.singletonList(list.get()) : Collections.emptyList();
        
        System.out.println(actualLists.toString());
        return generateResponse(actualLists, HttpStatus.CREATED);
    }
    
    
    
    public ResponseEntity<JSONObject> generateResponse(Object messageObject, HttpStatus status) {
        if(messageObject instanceof List<?>) {
            if (((List<?>) messageObject).size()==0) {
                messageObject = ((List<?>) messageObject).get(0);
            } else {
                JSONObject obj2=new JSONObject();
                obj2.put("List", messageObject);
                messageObject = obj2;
            }
        }
        
        return new ResponseEntity<>(JSONObject.parseObject(JSON.toJSONString(messageObject)),status);
    }
    public ResponseEntity<JSONObject> generateResponse(String messageString, HttpStatus status) {
        
        return new ResponseEntity<>(JSONObject.parseObject(messageString),status);
    }

    
}
