package cyse7125.fall2022.group03.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cyse7125.fall2022.group03.model.Tag;
import cyse7125.fall2022.group03.repository.TagRepository;
import cyse7125.fall2022.group03.service.TagService;

@Service
public class TagServiceImpl implements TagService {
    
    @Autowired
    TagRepository tagRepository;
    
    @Autowired
    UserServiceImpl userServiceImpl;
    
    @Autowired
    TaskServicceImpl taskServicceImpl;
    
    //not going to call this method
    @Override
    public ResponseEntity<JSONObject> createTag(Tag newTag) {
        
        try {
            
            //User user = userServiceImpl.getCurrentUser();
            
            //Task task = taskRepository.findByTaskId(newTag.getTagId()); //byuser id and task id            
            //if( task == null) {
                //return generateResponse("{\"error\":\"You have no tasks or You dont have such a task\"}", HttpStatus.BAD_REQUEST);
            //}
            
            
            //Tag tagToPut = new Tag(user.getId(), newTag.getTagname(), String.valueOf(new Date()), String.valueOf(new Date()));
            
            newTag = tagRepository.save(newTag);
            
    
        }catch (Exception e){
            e.printStackTrace();
            return generateResponse(null, HttpStatus.BAD_REQUEST);  //null ?
        }
        
        return generateResponse(newTag, HttpStatus.CREATED);
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
