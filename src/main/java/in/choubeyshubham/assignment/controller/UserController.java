package in.choubeyshubham.assignment.controller;

import in.choubeyshubham.assignment.model.User;
import in.choubeyshubham.assignment.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UserController {


    //    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        Optional<User> existingUser = userService.findByEmail(user.getEmail());

        if (existingUser.isPresent() &&
                new BCryptPasswordEncoder().matches(user.getPassword(), existingUser.get().getPassword())) {

            String token = userService.generateToken(existingUser.get()); // Unwrapping Optional<User>
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request) {
        if (!userService.isAdmin(request.getAttribute("email").toString())) {
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        String response = userService.resetPassword(email, newPassword);

        if (response.equals("User not found.")) {
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, HttpServletRequest req) {
        if (!userService.isAdmin(req.getAttribute("email").toString())) {
            return ResponseEntity.status(403).body("Unauthorized");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<String> updateUserRole(@PathVariable Long id, @RequestBody Map<String, Boolean> request, HttpServletRequest req) {
        if (!userService.isAdmin(req.getAttribute("email").toString())) {
            return ResponseEntity.status(403).body("Unauthorized");
        }
        boolean isAdmin = request.get("isAdmin");
        userService.updateUserRole(id, isAdmin);
        return ResponseEntity.ok("User role updated successfully.");
    }


}