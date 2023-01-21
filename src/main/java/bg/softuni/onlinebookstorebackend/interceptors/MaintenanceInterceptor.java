package bg.softuni.onlinebookstorebackend.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class MaintenanceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        boolean isClosed = now.getHour() <= 5 && now.getDayOfWeek().equals(DayOfWeek.SUNDAY);

        if (!request.getRequestURI().equals("/api/maintenance") && isClosed) {
            response.sendRedirect("/api/maintenance");
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
