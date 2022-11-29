package cyse7125.fall2022.group03.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cyse7125.fall2022.group03.service.SearchService;
import io.prometheus.client.Counter;
import io.prometheus.client.CollectorRegistry;

@RestController
@RequestMapping("/v1/user")
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    final Counter search_getAllMatches_Requests;
    
    public SearchController(CollectorRegistry registry) {
    	search_getAllMatches_Requests = Counter.build().name("search_getAllMatches").help("search getAllMatches").register(registry);
    }
    
    @Autowired
    SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<JSONObject> getAllMatches(@RequestParam String keyword) {
    	search_getAllMatches_Requests.inc();
        logger.info("Get - all matches");
        
        return searchService.getSearchTasks(keyword);
    }
    
}
