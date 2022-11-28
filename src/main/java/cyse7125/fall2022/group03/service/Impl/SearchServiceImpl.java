package cyse7125.fall2022.group03.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cyse7125.fall2022.group03.repository.TaskRepository;
import cyse7125.fall2022.group03.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cyse7125.fall2022.group03.model.User;
import cyse7125.fall2022.group03.model.Task;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
	private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
			
	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Autowired
	TaskRepository taskRepository;

	@Override
	public ResponseEntity<JSONObject> getSearchTasks(String keyword) {
		logger.info("Service - get search tasks");
		try {

			User user = userServiceImpl.getCurrentUser();
			List<Task> listOfTasks = taskRepository.findByUserId(user.getUserId());

			if( listOfTasks == null || listOfTasks.isEmpty()) {
				logger.info("getSearchTasks - You have no tasks, start creating");

				return generateResponse("{\"success\":\"You have no tasks, start creating\"}", HttpStatus.BAD_REQUEST);
			}
			
			if (keyword == null || keyword.isEmpty() || keyword.isBlank()) {
				logger.info("getSearchTasks - keyword must be specified");

				return generateResponse("{\"error\":\"keyword must be specified\"}", HttpStatus.OK);
			}
			
			List<Task> listOfSearchTasks = new ArrayList<Task>();
			for (Task task : listOfTasks) {
				if(task.toString().toLowerCase().contains(keyword.toLowerCase())) {
					listOfSearchTasks.add(task);
				}
			}
			
			if (!listOfSearchTasks.isEmpty()) {
				return generateResponse(listOfSearchTasks, HttpStatus.OK);
			}else {
				logger.info("getSearchTasks - No task matching the keyword found");
				
				return generateResponse("{\"success\":\"No task matching the keyword found\"}", HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("getSearchTasks - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
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
