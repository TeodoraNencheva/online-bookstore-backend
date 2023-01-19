package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.response.GeneralResponse;
import bg.softuni.onlinebookstorebackend.model.dto.user.LoginDTO;
import bg.softuni.onlinebookstorebackend.model.dto.user.UserOverviewDTO;
import bg.softuni.onlinebookstorebackend.model.dto.user.UserRegistrationDTO;
import bg.softuni.onlinebookstorebackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserRestController {
    private final UserService userService;
    private final MessageSource messageSource;
    private final AuthenticationManager authenticationManager;

    @PostMapping(path = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GeneralResponse> authenticateUser(@RequestBody LoginDTO loginDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        userService.login(loginDTO.getUsername());

        GeneralResponse body = new GeneralResponse(
                String.format("User %s signed-in successfully!", loginDTO.getUsername()));
        return ResponseEntity.ok().body(body);
    }

    @PostMapping(path = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GeneralResponse> register(@Valid @RequestBody UserRegistrationDTO userModel) {

        this.userService.register(userModel);

        GeneralResponse body = new GeneralResponse(
                String.format("User %s successfully registered", userModel.getEmail()));
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/register/verify")
    public ResponseEntity<GeneralResponse> verifyAccount(@RequestParam(required = false) String token) {
        if (token == null) {
            GeneralResponse body = new GeneralResponse(messageSource
                    .getMessage("user.registration.verification.missing.token",
                            null,
                            LocaleContextHolder.getLocale()));

            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        userService.verifyUser(token);

        GeneralResponse body = new GeneralResponse(messageSource
                .getMessage("user.registration.verification.success",
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
    public ResponseEntity<GeneralResponse> addNewAdmin(@PathVariable("username") String username) {
        userService.addNewAdmin(username);

        GeneralResponse body = new GeneralResponse(String.format("User %s added as admin", username));
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
