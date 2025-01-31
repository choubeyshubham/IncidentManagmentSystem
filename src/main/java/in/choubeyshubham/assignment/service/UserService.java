package in.choubeyshubham.assignment.service;

import in.choubeyshubham.assignment.model.User;
import in.choubeyshubham.assignment.repository.UserRepository;
import in.choubeyshubham.assignment.util.EmailService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(EmailService emailService, BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    private final String SECRET_KEY = "mySecretKey1234567890123456";
    public final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));


    //---------------------------------------

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail()) // Updated for JJWT 0.12.6
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(key) // Updated signing method
                .compact();
    }
    public boolean isAdmin(String email) {
        return userRepository.findByEmail(email)
                .map(User::isAdmin) // Assuming User entity has an `isAdmin` field
                .orElse(false);
    }

    public boolean resetPassword(String email, String newPassword) {
        return userRepository.findByEmail(email).map(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }).orElse(false);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUserRole(Long id, boolean isAdmin) {
        userRepository.findById(id).ifPresent(user -> {
            user.setAdmin(isAdmin);
            userRepository.save(user);
        });
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    public void requestPasswordReset(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String resetLink = "http://localhost:4200/reset-password?email=" + email;
            emailService.sendEmail(email, "Password Reset Request",
                    "Click the following link to reset your password: " + resetLink);
        });
    }

}