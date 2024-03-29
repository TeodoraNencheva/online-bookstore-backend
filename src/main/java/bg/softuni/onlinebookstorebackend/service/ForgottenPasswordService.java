package bg.softuni.onlinebookstorebackend.service;

import bg.softuni.onlinebookstorebackend.model.email.ForgotPasswordEmailContext;
import bg.softuni.onlinebookstorebackend.model.entity.SecureTokenEntity;
import bg.softuni.onlinebookstorebackend.model.entity.UserEntity;
import bg.softuni.onlinebookstorebackend.model.exception.InvalidTokenException;
import bg.softuni.onlinebookstorebackend.repositories.SecureTokenRepository;
import bg.softuni.onlinebookstorebackend.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ForgottenPasswordService {
    private final UserService userService;
    private final SecureTokenService secureTokenService;
    private final SecureTokenRepository secureTokenRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void forgottenPassword(String email, String baseUrl) {
        UserEntity user = userService.getUser(email);
        sendResetPasswordEmail(user, baseUrl);
    }

    private void sendResetPasswordEmail(UserEntity user, String baseUrl) {
        SecureTokenEntity secureToken = secureTokenService.createSecureToken();
        secureToken.setUser(user);
        secureTokenRepository.save(secureToken);

        ForgotPasswordEmailContext emailContext = new ForgotPasswordEmailContext();
        emailContext.init(user);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl(baseUrl, secureToken.getToken());

        try {
            emailService.sendEmail(emailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(String password, String token) {
        Optional<SecureTokenEntity> tokenOpt = secureTokenRepository.findByToken(token);

        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired()) {
            throw new InvalidTokenException();
        }

        SecureTokenEntity secureToken = tokenOpt.get();
        UserEntity user = secureToken.getUser();
        if (user == null) {
            throw new UsernameNotFoundException("");
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        secureTokenRepository.delete(secureToken);
    }
}
