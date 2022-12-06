package cyse7125.fall2022.group03.service.Impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cyse7125.fall2022.group03.model.Task;
import cyse7125.fall2022.group03.model.User;
import cyse7125.fall2022.group03.repository.TaskRepository;
import cyse7125.fall2022.group03.service.SearchService;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Histogram;
import org.springframework.beans.factory.annotation.Value;

@Service
public class SearchServiceImpl implements SearchService {
	private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
	private final Histogram requestLatency_searchTaskDb;

	@Value("${elasticsearch.host}")
    private String elasticSearchHost;

	@Value("${elasticsearch.port}")
    private String elasticSearchPort;
	
	public SearchServiceImpl(CollectorRegistry registry) {
		requestLatency_searchTaskDb = Histogram.build()
                .name("requests_latency_seconds_searchTaskDb").help("searchTaskDb Request latency in seconds").register(registry);
    }
	
	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Autowired
	TaskRepository taskRepository;

	@Override
	public ResponseEntity<JSONObject> getSearchTasks(String keyword) {
		logger.debug("csye: Service - get search tasks");
		try {
			
			User user = userServiceImpl.getCurrentUser();
			
			Histogram.Timer requestTimer = requestLatency_searchTaskDb.startTimer();
			List<Task> listOfTasks = taskRepository.findByUserId(user.getUserId());
			requestTimer.observeDuration();

			if( listOfTasks == null || listOfTasks.isEmpty()) {
				logger.info("getSearchTasks - You have no tasks, start creating");

				return generateResponse("{\"success\":\"You have no tasks, start creating\"}", HttpStatus.BAD_REQUEST);
			}
			
			if (keyword == null || keyword.isEmpty() || keyword.isBlank()) {
				logger.info("getSearchTasks - keyword must be specified");

				return generateResponse("{\"error\":\"keyword must be specified\"}", HttpStatus.OK);
			}
			
			RestHighLevelClient client = new RestHighLevelClient(
					RestClient.builder(new HttpHost(elasticSearchHost, Integer.valueOf(elasticSearchPort), "http")));
			
//			QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("userId", user.getUserId());
//		    QueryBuilder matchQueryBuilder1 = QueryBuilders.matchQuery("summary", keyword);
//		    QueryBuilder matchQueryBuilder2 = QueryBuilders.matchQuery("name", keyword);
//		    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//		    sourceBuilder.query(matchQueryBuilder);
//		    sourceBuilder.query(matchQueryBuilder1);
//		    sourceBuilder.query(matchQueryBuilder2);
		    
			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
			//MultiMatchQueryBuilder multiMatchQueryBuilder = new MultiMatchQueryBuilder(keyword, "summary", "name", "tagname", "status", "priority", "dueDate", "accountCreated", "accountUpdated");
		    MultiMatchQueryBuilder multiMatchQueryBuilder = new MultiMatchQueryBuilder(keyword, "summary", "name", "tagname", "status", "priority");
		    multiMatchQueryBuilder.operator(Operator.OR);
		    BoolQueryBuilder boolq = new BoolQueryBuilder();
		    boolq.must(QueryBuilders.matchQuery("userId", user.getUserId()));
		    boolq.must(multiMatchQueryBuilder);
		    sourceBuilder.query(boolq);
			
			SearchRequest searchRequest = new SearchRequest();
			searchRequest.indices("taskindex");
			
			searchRequest.source(sourceBuilder);
			
			SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

			//logger.debug("csye: getTotalHits="+response.getHits().getTotalHits().value);

			SearchHit[] searchHits = response.getHits().getHits();
			
			List<Task> results = new ArrayList<Task>();

			ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
			
			
			Arrays.stream(searchHits).forEach(hit -> {
			    String source = hit.getSourceAsString();
			    Task task = null;
				try {
					task = objectMapper.readValue(source, Task.class);
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			    results.add(task);
			});
			
			if (!results.isEmpty()) {
				return generateResponse(results, HttpStatus.OK);
			}else {
				logger.info("getSearchTasks - No task matching the keyword found");
				
				return generateResponse("{\"success\":\"No task matching the keyword found\"}", HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("csye: getSearchTasks - Exception");
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
