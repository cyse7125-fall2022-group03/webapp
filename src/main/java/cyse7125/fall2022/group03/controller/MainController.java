package cyse7125.fall2022.group03.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.prometheus.client.Counter;
import io.prometheus.client.CollectorRegistry;

@RestController
@RequestMapping("/")
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    final Counter main_checkHealthz_Requests;
    
    public MainController(CollectorRegistry registry) {
    	main_checkHealthz_Requests = Counter.build().name("main_checkHealthz").help("main checkHealthz").register(registry);
    }
    
    @GetMapping("/healthz")
    public ResponseEntity<String> checkHealthz() {
    	main_checkHealthz_Requests.inc();
        logger.info("Get - healthz");

        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}
