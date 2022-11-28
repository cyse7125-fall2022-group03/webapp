package cyse7125.fall2022.group03.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cyse7125.fall2022.group03.model.Lists;
import cyse7125.fall2022.group03.repository.ListsRepository;
import cyse7125.fall2022.group03.service.ListsService;
import cyse7125.fall2022.group03.service.Impl.UserServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/v1/user")
public class ListsController {
    private static final Logger logger = LoggerFactory.getLogger(ListsController.class);
    
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    ListsService listsService;
    @Autowired
    ListsRepository listsRepository;
    
    @GetMapping("/lists")
    public ResponseEntity<JSONObject> getAllLists() {
        logger.info("Get - all lists");
        
        return listsService.getAllLists();         
    }
    
    @GetMapping("/list/{listId}")
    public ResponseEntity<JSONObject> getAList(@PathVariable String listId) {
        logger.info("Get - a list");

        if (listId == null || listId.isBlank()) {
            logger.error("Get - Invalid request body");

            return generateResponse("{\"error\":\"Invalid request body\"}", HttpStatus.BAD_REQUEST);
        }
        
        return listsService.getAList(listId);         
    }
    
    @PostMapping("/list/create")
    public ResponseEntity<JSONObject> createList(@RequestBody Lists newList) {
        logger.info("Post - create a list");

        if (newList == null) {
            logger.error("Post - Invalid request body");

            return generateResponse("{\"error\":\"Invalid request body\"}", HttpStatus.BAD_REQUEST);
        }
        // And other list variable validations - left
        
        if (newList.getListId() != null) {
            logger.error("Post - Request cant hold List ID");

            return generateResponse("{\"error\":\"Request cant hold List ID\"}", HttpStatus.BAD_REQUEST);
        }
        
        if (newList.getUserId() != null) {
            logger.error("Post - Request cant hold User ID - Not allowed to see others");

            return generateResponse("{\"error\":\"Request cant hold User ID - Not allowed to see others\"}", HttpStatus.BAD_REQUEST);
        }
        
        return listsService.createList(newList);
    }
    
    @PutMapping("/list/update")
    public ResponseEntity<JSONObject> updateList(@RequestBody Lists newLists) {
    	logger.info("Put - update a list");

        if (newLists == null) {
            logger.error("Put - Invalid request body");

            return generateResponse("{\"error\":\"Invalid request body\"}", HttpStatus.BAD_REQUEST);
        }
        // And other task variable validations - left
        
        if (newLists.getListId() == null) {
            logger.error("Put - Request must hold List ID");

            return generateResponse("{\"error\":\"Request must hold List ID\"}", HttpStatus.BAD_REQUEST);
        }
        
        if(newLists.getUserId()!=null) {
            logger.error("Put - Request cant hold User ID - Not allowed to see others");

        	return generateResponse("{\"error\":\"Request cant hold User ID - Not allowed to see others\"}", HttpStatus.BAD_REQUEST);
        }
        
        if (newLists.getAccountCreated()!= null || newLists.getAccountUpdated()!=null) {
            logger.error("Put - Request cant hold User ID - Not allowed to see others");

            return generateResponse("{\"error\":\"Request cant hold User ID - Not allowed to see others\"}", HttpStatus.BAD_REQUEST);
        }
    	
    	return listsService.updateList(newLists);
    }

    @DeleteMapping("/list/delete/{listId}")
    public ResponseEntity<JSONObject> deleteList(@PathVariable String listId) {
    	logger.info("Delete - delete a list with param");
        
    	if (listId == null) {
            logger.error("Delete - Invalid request Path Variable");

    		return generateResponse("{\"error\":\"Invalid request Path Variable\"}", HttpStatus.BAD_REQUEST);
    	}
    	
    	return listsService.deleteList(listId);
    }
    
    
    
    public ResponseEntity<JSONObject> generateResponse(Object messageObject, HttpStatus status) {
        
        return new ResponseEntity<>(JSONObject.parseObject(JSON.toJSONString(messageObject)),status);
    }
    public ResponseEntity<JSONObject> generateResponse(String messageString, HttpStatus status) {
        
        return new ResponseEntity<>(JSONObject.parseObject(messageString),status);
    }
}
