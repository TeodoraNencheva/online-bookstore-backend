package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.user.UserRegistrationDTO;
import bg.softuni.onlinebookstorebackend.model.error.InvalidTokenException;
import bg.softuni.onlinebookstorebackend.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserRegistrationController {
    private static final String REDIRECT_LOGIN= "redirect:/users/login";

    private final UserService userService;
    private final MessageSource messageSource;

    public UserRegistrationController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @ModelAttribute("userModel")
    public UserRegistrationDTO initUserModel() {
        return new UserRegistrationDTO();
    }

    @GetMapping("/register")
    public String register() {
        return "auth-register";
    }

    @PostMapping("/register")
    public String register(@Valid UserRegistrationDTO userModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userModel", userModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userModel",
                    bindingResult);
            return "redirect:/users/register";
        }

        this.userService.register(userModel);

        return "need-for-verification";
    }

    @GetMapping("/register/verify")
    public String verifyAccount(@RequestParam(required = false) String token, Model model,
                                RedirectAttributes redirectAttributes) {
        if (token == null) {
            redirectAttributes.addFlashAttribute("tokenError", messageSource
                    .getMessage("user.registration.verification.missing.token",
                            null,
                            LocaleContextHolder.getLocale()));

            return REDIRECT_LOGIN;
        }

        try {
            userService.verifyUser(token);
        } catch (InvalidTokenException e) {
            redirectAttributes.addFlashAttribute("tokenError",
                    messageSource.getMessage("user.registration.verification.invalid.token",
                            null,
                            LocaleContextHolder.getLocale()));
            return REDIRECT_LOGIN;
        }

        redirectAttributes.addFlashAttribute("successMessage",
                messageSource.getMessage("user.registration.verification.success",
                        null,
                        LocaleContextHolder.getLocale()));

        return REDIRECT_LOGIN;
    }
}