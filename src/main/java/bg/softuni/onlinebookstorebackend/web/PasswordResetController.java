package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.service.ResponseService;
import bg.softuni.onlinebookstorebackend.service.ForgottenPasswordService;
import bg.softuni.onlinebookstorebackend.model.dto.password.ResetPasswordData;
import bg.softuni.onlinebookstorebackend.model.dto.password.ResetPasswordEmailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/password")
public class PasswordResetController {

    private final MessageSource messageSource;
    private final ForgottenPasswordService passwordService;

    @PostMapping("/reset")
    public ResponseEntity<Object> sendResetPasswordEmail(@Valid @RequestBody ResetPasswordEmailDTO emailDTO) {
        passwordService.forgottenPassword(emailDTO.getEmail(), emailDTO.getBaseUrl());

        Map<String, Object> body = ResponseService.generateGeneralResponse(messageSource
                .getMessage("user.password.reset.email.sent",
                        null, LocaleContextHolder.getLocale()));
        return ResponseEntity.ok(body);
    }

    @PostMapping("/change")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody ResetPasswordData data) {

        passwordService.updatePassword(data.getPassword(), data.getToken());

        Map<String, Object> body = ResponseService.generateGeneralResponse(messageSource
                .getMessage("user.password.changed.successfully",
                        null, LocaleContextHolder.getLocale()));
        return ResponseEntity.ok(body);
    }
}
