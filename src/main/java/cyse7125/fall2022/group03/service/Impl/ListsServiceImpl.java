package cyse7125.fall2022.group03.service.Impl;

import java.time.LocalDateTime;
import java.util.Collections;
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
import cyse7125.fall2022.group03.model.Task;
import cyse7125.fall2022.group03.model.User;
import cyse7125.fall2022.group03.repository.ListsRepository;
import cyse7125.fall2022.group03.repository.TaskRepository;
import cyse7125.fall2022.group03.service.ListsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.prometheus.client.Histogram;
import io.prometheus.client.CollectorRegistry;

@Service
public class ListsServiceImpl implements ListsService {
	private static final Logger logger = LoggerFactory.getLogger(ListsServiceImpl.class);
	private final Histogram requestLatency_listDb;
	
	public ListsServiceImpl(CollectorRegistry registry) {
		requestLatency_listDb = Histogram.build()
                .name("requests_latency_seconds_listDb").help("listDb Request latency in seconds").register(registry);
    }

	@Autowired
	ListsRepository listsRepository;

	@Autowired
	UserServiceImpl userServiceImpl; 

	@Autowired
	TaskRepository taskRepository;

	@Override
	public ResponseEntity<JSONObject> createList(Lists newList) {
		logger.info("Service - create a list");

		try {

			User user = userServiceImpl.getCurrentUser();

			Lists listToPut = new Lists(user.getUserId(), newList.getName(), LocalDateTime.now(), LocalDateTime.now());
			
			Histogram.Timer requestTimer = requestLatency_listDb.startTimer();
			newList = listsRepository.save(listToPut);
			requestTimer.observeDuration();

		}catch (Exception e){
			logger.error("createList - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return generateResponse(newList, HttpStatus.CREATED);
	}



	@Override
	public ResponseEntity<JSONObject> getAllLists() {
		logger.info("Service - get all lists of a user");
		try {

			User user = userServiceImpl.getCurrentUser();
			
			Histogram.Timer requestTimer = requestLatency_listDb.startTimer();
			List<Lists> listOfLists = listsRepository.findByUserId(user.getUserId());
			requestTimer.observeDuration();

			if( listOfLists == null || listOfLists.isEmpty()) {
				logger.error("getAllLists - You dont have any lists");

				return generateResponse("{\"error\":\"You dont have any list\"}", HttpStatus.NOT_FOUND);
			}


			return generateResponse(listOfLists, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("getAllLists - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<JSONObject> getAList(String id) {
		logger.info("Service - get a list");
		try {

			User user = userServiceImpl.getCurrentUser();
			
			Histogram.Timer requestTimer = requestLatency_listDb.startTimer();
			Optional<Lists> list = listsRepository.findById(new ListsIdentity(user.getUserId(), id));
			requestTimer.observeDuration();
			
			if( list == null || list.isEmpty()) {
				logger.error("getAList - You dont have such a list");

				return generateResponse("{\"error\":\"You dont have such a list\"}", HttpStatus.NOT_FOUND);
			}

			List<Lists> actualLists = list.isPresent() ? Collections.singletonList(list.get()) : Collections.emptyList();

			System.out.println(actualLists.toString());
			return generateResponse(actualLists, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("getAList - Exception");
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



	@Override
	public ResponseEntity<JSONObject> updateList(Lists  newLists) {
		logger.info("Service - update a list");
		try {
			User user = userServiceImpl.getCurrentUser();
			
			Histogram.Timer requestTimer = requestLatency_listDb.startTimer();
			Lists existingLists =  listsRepository.findTaskByListIdAndUserId(newLists.getListId(), user.getUserId());
			requestTimer.observeDuration();
			
			if( existingLists == null) {
				logger.error("updateList - You dont have such a list");

				return generateResponse("{\"error\":\"You dont have such a list\"}", HttpStatus.NOT_FOUND);
			}

			existingLists.setName(newLists.getName());
			existingLists.setAccountUpdated(LocalDateTime.now());

			Histogram.Timer requestTimer2 = requestLatency_listDb.startTimer();
			listsRepository.save(existingLists);
			requestTimer2.observeDuration();

		} catch (Exception e){
			logger.error("updateList - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return generateResponse("{\"success\":\"Update is done!\"}", HttpStatus.OK);
	}



	@Override
	public ResponseEntity<JSONObject> deleteList(String listId) {
		logger.info("Service - delete a list");
		try {
			User user = userServiceImpl.getCurrentUser();
			
			Histogram.Timer requestTimer = requestLatency_listDb.startTimer();
			Lists existingLists =  listsRepository.findTaskByListIdAndUserId(listId, user.getUserId());
			requestTimer.observeDuration();
			
			if( existingLists == null) {
				logger.error("deleteList - You dont have such a list");

				return generateResponse("{\"error\":\"You dont have such a list\"}", HttpStatus.NOT_FOUND);
			}

			Histogram.Timer requestTimer2 = requestLatency_listDb.startTimer();
			listsRepository.delete(existingLists);
			requestTimer2.observeDuration();

			//will delete corresponding tasks? no, have to do manually
			deleteTasksOfList(listId, user.getUserId());

		} catch (Exception e) {
			logger.error("deleteList - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return generateResponse("{\"success\":\"List is deleted!\"}", HttpStatus.OK);
	}


	public void deleteTasksOfList(String listId, String userID) throws Exception {	
		logger.info("Service - deleteTasksOfList");	
		//assumed to call from list method after all lists are deleted for this user        

		Histogram.Timer requestTimer = requestLatency_listDb.startTimer();
		List<Task> tasksOfList = taskRepository.findTaskByListIdAndUserId(listId, userID);
		requestTimer.observeDuration();
		
		if( tasksOfList == null || tasksOfList.isEmpty()) {
			return;
		}

		for (Task task : tasksOfList) {
			taskRepository.delete(task);
		}
	}

}
