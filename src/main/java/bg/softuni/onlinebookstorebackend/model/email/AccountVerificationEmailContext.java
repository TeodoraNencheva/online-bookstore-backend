package bg.softuni.onlinebookstorebackend.model.email;

import bg.softuni.onlinebookstorebackend.model.entity.UserEntity;
import org.springframework.web.util.UriComponentsBuilder;

public class AccountVerificationEmailContext extends AbstractEmailContext {

    private String token;


    @Override
    public <T> void init(T context) {
        //we can do any common configuration setup here
        // like setting up some base URL and context
        UserEntity customer = (UserEntity) context;
        put("fullName", customer.getFullName());
        setTemplateLocation("account-verification-email");
        setSubject("Complete your registration");
        setFrom("no-reply@bookstore.com");
        setTo(customer.getEmail());
    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

    public void buildVerificationUrl(final String baseURL, final String token) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/users/register/verify").queryParam("token", token).toUriString();
        put("verificationURL", url);
    }
}
