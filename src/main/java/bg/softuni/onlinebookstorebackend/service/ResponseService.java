package bg.softuni.onlinebookstorebackend.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ResponseService {
    public static Map<String, Object> generateGeneralResponse(String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("message", message);
        return response;
    }
}
