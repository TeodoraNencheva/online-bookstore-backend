package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.user.UserOverviewDTO;
import bg.softuni.onlinebookstorebackend.model.dto.user.UserRegistrationDTO;
import bg.softuni.onlinebookstorebackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;
    private final MessageSource messageSource;

    public UserRestController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @PostMapping(path = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> register(@Valid @RequestBody UserRegistrationDTO userModel) {

        this.userService.register(userModel);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", String.format("User %s successfully registered", userModel.getEmail()));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/register/verify")
    public ResponseEntity<Object> verifyAccount(@RequestParam(required = false) String token) {
        if (token == null) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("message", messageSource
                    .getMessage("user.registration.verification.missing.token",
                            null,
                            LocaleContextHolder.getLocale()));

            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        userService.verifyUser(token);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", messageSource.getMessage("user.registration.verification.success",
                null,
                LocaleContextHolder.getLocale()));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserOverviewDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsersOverview());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/addAdmin/{username}")
    public ResponseEntity<Object> addNewAdmin(@PathVariable("username") String username) {
        userService.addNewAdmin(username);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", String.format("User %s added as admin", username));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
