package cyse7125.fall2022.group03.controller;

import java.time.LocalDateTime;

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

import cyse7125.fall2022.group03.model.Task;
import cyse7125.fall2022.group03.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.prometheus.client.Counter;
import io.prometheus.client.CollectorRegistry;

@RestController
@RequestMapping("/v1/user")
public class TasksController {
	private static final Logger logger = LoggerFactory.getLogger(TasksController.class);
	final Counter task_getAllTasks_Requests;
    final Counter task_getAllTasksByTagName_Requests;
    final Counter task_getATask_Requests;
    final Counter task_getAllTasksUnderAList_Requests;
    final Counter task_createTask_Requests;
    final Counter task_updateTask_Requests;
    final Counter task_deleteTask_Requests;
    final Counter task_changeTaskToNewList_Requests;
    
    public TasksController(CollectorRegistry registry) {
    	task_getAllTasks_Requests = Counter.build().name("task_getAllTasks").help("task getAllTasks").register(registry);
    	task_getAllTasksByTagName_Requests = Counter.build().name("task_getAllTasksByTagName").help("task getAllTasksByTagName").register(registry);
    	task_getATask_Requests = Counter.build().name("task_getATask").help("task getATask").register(registry);
    	task_getAllTasksUnderAList_Requests = Counter.build().name("task_getAllTasksUnderAList").help("task getAllTasksUnderAList").register(registry);
    	task_createTask_Requests = Counter.build().name("task_createTask").help("task createTask").register(registry);
    	task_updateTask_Requests = Counter.build().name("task_updateTask").help("task updateTask").register(registry);
    	task_deleteTask_Requests = Counter.build().name("task_deleteTask").help("task deleteTask").register(registry);
    	task_changeTaskToNewList_Requests = Counter.build().name("task_changeTaskToNewList").help("task changeTaskToNewList").register(registry);
    }
    
	@Autowired
	TaskService taskService;

	@GetMapping("/self")
	public ResponseEntity<JSONObject> getAllTasks() {
		task_getAllTasks_Requests.inc();
		logger.info("Get - all tasks of user");

		return taskService.getAllTasks();
	}

	@GetMapping("/self/{tagname}")
	public ResponseEntity<JSONObject> getAllTasksByTagName(@PathVariable String tagname) {
		task_getAllTasksByTagName_Requests.inc();
		logger.info("Get - all tasks of user with a tagname");

		if(tagname == null) {
			logger.error("Get - Invalid request body");

			return generateResponse("{\"error\":\"Invalid request body\"}", HttpStatus.BAD_REQUEST);
		}

		return taskService.getAllTasksByTagName(tagname);
	}

	@GetMapping("/task/{listId}/{taskId}")
	public ResponseEntity<JSONObject> getATask(@PathVariable String listId, @PathVariable String taskId) {
		task_getATask_Requests.inc();
		logger.info("Get - a task with taskId under listId");

		if (listId == null || listId.isBlank() || taskId == null || taskId.isBlank()) {
			logger.error("Get - Invalid request body");

			return generateResponse("{\"error\":\"Invalid request body\"}", HttpStatus.BAD_REQUEST);
		}

		return taskService.getATask(listId, taskId);

	}

	@GetMapping("/task/{listId}")
	public ResponseEntity<JSONObject> getAllTasksUnderAList(@PathVariable String listId) {
		task_getAllTasksUnderAList_Requests.inc();
		logger.info("Get - all tasks under listId");

		if (listId == null || listId.isBlank()) {
			logger.error("Get - Invalid request body");

			return generateResponse("{\"error\":\"Invalid request body\"}", HttpStatus.BAD_REQUEST);
		}

		return taskService.getAllTasksUnderAList(listId);
	}

	@PostMapping("/task/create")
	public ResponseEntity<JSONObject> createTask(@RequestBody Task newTask) {
		task_createTask_Requests.inc();
		logger.info("Post - create a task");

		if (newTask == null) {
			logger.error("Post - Invalid request body");
			
			return generateResponse("{\"error\":\"Invalid request body\"}", HttpStatus.BAD_REQUEST);
		}
		// And other task variable validations - left

		if (newTask.getTaskId() != null) {
			logger.error("Post - Request cant hold Task ID");

			return generateResponse("{\"error\":\"Request cant hold Task ID\"}", HttpStatus.BAD_REQUEST);
		}

		if (newTask.getListId() == null || newTask.getDueDate()==null) {
			logger.error("Post - Request must specify a list ID and Due date");

			return generateResponse("{\"error\":\"Request must specify a list ID and Due date\"}", HttpStatus.BAD_REQUEST);
		}

		if (newTask.getUserId() != null) {
			logger.error("Post - Request cant hold User ID - Not allowed to see others");

			return generateResponse("{\"error\":\"Request cant hold User ID - Not allowed to see others\"}", HttpStatus.BAD_REQUEST);
		}

		//        Task.Priority priority = newTask.getPriority();
		//        if (priority != Task.Priority.high || priority != Task.Priority.low || priority != Task.Priority.medium) {
		//        	return generateResponse("{\"error\":\"Priority can be only medium/low/high\"}", HttpStatus.BAD_REQUEST);
		//        }

		if (newTask.getStatus() != null) {
			logger.error("Post - Request cant hold Status value in create");

			return generateResponse("{\"error\":\"Request cant hold Status value in create\"}", HttpStatus.BAD_REQUEST);
		}
		
		if (newTask.getDueDate() != null && !newTask.getDueDate().isAfter(LocalDateTime.now())) {
			logger.error("Post - Request can hold due dates future from now only");

			return generateResponse("{\"error\":\"Request can hold due dates future from now only\"}", HttpStatus.BAD_REQUEST);
		}
		if (newTask.getPriority() != null && !(newTask.getPriority()==Task.Priority.high || newTask.getPriority()==Task.Priority.medium || newTask.getPriority()==Task.Priority.low)) {
			logger.error("Post - Request can hold priority either high/low/medium");

			return generateResponse("{\"error\":\"Request can hold priority either high/low/medium\"}", HttpStatus.BAD_REQUEST);
		}

		return taskService.createTask(newTask);
	}

	@PutMapping("/task/update")
	public ResponseEntity<JSONObject> updateTask(@RequestBody Task newTask) {
		task_updateTask_Requests.inc();
		logger.info("Put - update a task");

		if (newTask == null) {
			logger.error("Put - Invalid request body");

			return generateResponse("{\"error\":\"Invalid request body\"}", HttpStatus.BAD_REQUEST);
		}
		// And other task variable validations - left

		if (newTask.getTaskId() == null) {
			logger.error("Put - Request must hold Task ID");

			return generateResponse("{\"error\":\"Request must hold Task ID\"}", HttpStatus.BAD_REQUEST);
		}

		if (newTask.getUserId() != null) {
			logger.error("Put - Request cant hold User ID - Not allowed to see others");

			return generateResponse("{\"error\":\"Request cant hold User ID - Not allowed to see others\"}", HttpStatus.BAD_REQUEST);
		}
		
		if (newTask.getStatus() != null && !(newTask.getStatus()==Task.Status.TODO || newTask.getStatus()==Task.Status.OVERDUE || newTask.getStatus()==Task.Status.COMPLETE)) {
			logger.error("Put - Request can hold status either TODO/COMPLETE/OVERDUE");

			return generateResponse("{\"error\":\"Request can hold status either TODO/COMPLETE/OVERDUE\"}", HttpStatus.BAD_REQUEST);
		}
		if (newTask.getPriority() != null && !(newTask.getPriority()==Task.Priority.high || newTask.getPriority()==Task.Priority.medium || newTask.getPriority()==Task.Priority.low)) {
			logger.error("Put - Request can hold priority either high/low/medium");

			return generateResponse("{\"error\":\"Request can hold priority either high/low/medium\"}", HttpStatus.BAD_REQUEST);
		}

		return taskService.updateTask(newTask);
	}

	@DeleteMapping("/task/delete/{taskId}")
	public ResponseEntity<JSONObject> deleteTask(@PathVariable String taskId) {
		task_deleteTask_Requests.inc();
		logger.info("Delete - delete a taskId");

		if (taskId == null) {
			logger.error("Delete - Invalid request Path Variable");

			return generateResponse("{\"error\":\"Invalid request Path Variable\"}", HttpStatus.BAD_REQUEST);
		}

		return taskService.deleteTask(taskId);

	}

	@PutMapping("/task/change")
	public ResponseEntity<JSONObject> changeTaskToNewList (@RequestBody Task newTask) {
		task_changeTaskToNewList_Requests.inc();
		logger.info("Put - change a task to new listId");

		if (newTask == null) {
			logger.error("Put - Invalid request body");

			return generateResponse("{\"error\":\"Invalid request body\"}", HttpStatus.BAD_REQUEST);
		}
		// And other task variable validations - left
		//System.out.println("test= " + newTask.getTaskId());
		if (newTask.getTaskId() == null) {
			logger.error("Put - Request must hold Task ID");

			return generateResponse("{\"error\":\"Request must hold Task ID\"}", HttpStatus.BAD_REQUEST);
		}
		if (newTask.getListId() == null) {
			logger.error("Put - Request must hold List ID");

			return generateResponse("{\"error\":\"Request must hold List ID\"}", HttpStatus.BAD_REQUEST);
		}

		if (newTask.getUserId() != null) {
			logger.error("Put - Request cant hold User ID - Not allowed to see others");

			return generateResponse("{\"error\":\"Request cant hold User ID - Not allowed to see others\"}", HttpStatus.BAD_REQUEST);
		}

		return taskService.changeTaskToNewList(newTask);
	}

	public ResponseEntity<JSONObject> generateResponse(Object messageObject, HttpStatus status) {

		return new ResponseEntity<>(JSONObject.parseObject(JSON.toJSONString(messageObject)),status);
	}
	public ResponseEntity<JSONObject> generateResponse(String messageString, HttpStatus status) {

		return new ResponseEntity<>(JSONObject.parseObject(messageString),status);
	}

}
