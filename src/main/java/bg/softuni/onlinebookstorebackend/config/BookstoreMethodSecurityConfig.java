package bg.softuni.onlinebookstorebackend.config;

import bg.softuni.onlinebookstorebackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BookstoreMethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    private final OrderService orderService;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new BookstoreSecurityExpressionHandler(orderService);
    }
}
