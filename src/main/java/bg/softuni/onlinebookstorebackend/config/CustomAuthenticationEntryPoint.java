package bg.softuni.onlinebookstorebackend.config;

import bg.softuni.onlinebookstorebackend.service.ResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Map;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{
        if (request.getRequestURI().equals("/api/auth/login") && request.getMethod().equals("POST")) {
            response.resetBuffer();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            Map<String, Object> body = ResponseService.generateGeneralResponse("User or password is incorrect.");
            response.getOutputStream()
                    .print(objectMapper.writeValueAsString(body));

            response.flushBuffer();
        } else {
            response.resetBuffer();
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            Map<String, Object> body = ResponseService.generateGeneralResponse("Access denied.");
            response.getOutputStream()
                    .print(objectMapper.writeValueAsString(body));

            response.flushBuffer();
        }
    }
}
