package bg.softuni.onlinebookstorebackend.config;

import bg.softuni.onlinebookstorebackend.service.OrderService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BookstoreMethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    private final OrderService orderService;

    public BookstoreMethodSecurityConfig(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new BookstoreSecurityExpressionHandler(orderService);
    }
}
