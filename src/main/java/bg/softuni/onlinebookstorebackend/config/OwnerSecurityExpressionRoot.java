package bg.softuni.onlinebookstorebackend.config;

import bg.softuni.onlinebookstorebackend.service.OrderService;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public class OwnerSecurityExpressionRoot extends SecurityExpressionRoot
        implements MethodSecurityExpressionOperations {

    private final Authentication authentication;
    private final OrderService orderService;
    private Object filterObject;
    private Object returnObject;


    public OwnerSecurityExpressionRoot(Authentication authentication, OrderService orderService) {
        super(authentication);
        this.authentication = authentication;
        this.orderService = orderService;
    }

    public boolean isOwner(UUID id) {
        if (authentication.getPrincipal() == null) {
            return false;
        }

        String username = authentication.getName();

        return orderService.isOwner(username, id) || hasAuthority("ROLE_ADMIN");
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return null;
    }
}
