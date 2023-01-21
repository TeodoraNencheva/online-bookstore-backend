package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {
    @GetMapping
    public ResponseEntity<Map<String, Object>> maintenance() {
        Map<String, Object> body = ResponseService.generateGeneralResponse("Online bookstore is closed for maintenance.");
        return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
