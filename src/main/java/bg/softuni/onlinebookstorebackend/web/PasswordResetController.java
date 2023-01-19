package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.error.InvalidTokenException;
import bg.softuni.onlinebookstorebackend.user.forgotten_password.ForgottenPasswordService;
import bg.softuni.onlinebookstorebackend.user.forgotten_password.ResetPasswordData;
import bg.softuni.onlinebookstorebackend.user.forgotten_password.ResetPasswordEmailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/password")
public class PasswordResetController {
    private final static String REDIRECT_LOGIN = "redirect:/users/login";

    private final MessageSource messageSource;
    private final ForgottenPasswordService passwordService;

    @ModelAttribute("emailDTO")
    public ResetPasswordEmailDTO initEmail() {
        return new ResetPasswordEmailDTO();
    }

    @GetMapping("/reset")
    public String resetPassword() {
        return "reset-password-request";
    }

    @PostMapping("/reset")
    public String sendResetPasswordEmail(@Valid ResetPasswordEmailDTO emailDTO,
                                         BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("emailDTO", emailDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.emailDTO",
                    bindingResult);
            return "redirect:/password/reset";
        }

        try {
            passwordService.forgottenPassword(emailDTO.getEmail());
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("successMessage",
                messageSource.getMessage("user.password.reset.email.sent",
                        null,
                        LocaleContextHolder.getLocale()));

        return "redirect:/password/reset";
    }

    @GetMapping("/change")
    public String changePassword(@RequestParam(required = false) String token,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (token == null) {
            redirectAttributes.addFlashAttribute("tokenError",
                    messageSource
                            .getMessage("user.registration.verification.missing.token",
                                    null,
                                    LocaleContextHolder.getLocale()));

            return REDIRECT_LOGIN;
        }

        if (!model.containsAttribute("data")) {
            ResetPasswordData passwordData = new ResetPasswordData();
            passwordData.setToken(token);
            model.addAttribute("data", passwordData);
        }

        return "change-password";
    }

    @PostMapping("/change")
    public String changePassword(@Valid ResetPasswordData data,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("data", data);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.data",
                    bindingResult);
            return String.format("redirect:/password/change?token=%s", data.getToken());
        }

        try {
            passwordService.updatePassword(data.getPassword(), data.getToken());
        } catch (InvalidTokenException | UsernameNotFoundException e) {
            redirectAttributes.addFlashAttribute("tokenError",
                    messageSource.getMessage("user.registration.verification.invalid.token",
                            null, LocaleContextHolder.getLocale()));

            return REDIRECT_LOGIN;
        }

        redirectAttributes.addFlashAttribute("successMessage", messageSource
                .getMessage("user.password.changed.successfully",
                        null, LocaleContextHolder.getLocale()));
        return REDIRECT_LOGIN;
    }
}
