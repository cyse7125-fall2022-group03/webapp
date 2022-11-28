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

@RestController
@RequestMapping("/v1/user")
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<JSONObject> getAllLists(@RequestParam String keyword) {
        logger.info("Get - all lists");
        
        return searchService.getSearchTasks(keyword);
    }
    
}
