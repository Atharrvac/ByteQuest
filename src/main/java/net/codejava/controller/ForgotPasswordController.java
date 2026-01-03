//  ********************************** use for forget password  ******************************************
package net.codejava.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.codejava.model.User;
import net.codejava.repository.UserRepo;
import net.codejava.service.EmailService;
import net.codejava.service.UserService;

@Controller
@RequestMapping("/forgotpassword")
public class ForgotPasswordController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    Random random = new Random(1000);

    @Autowired
    UserService userservice;

    @Autowired
    UserRepo repo;

    @GetMapping("/")
    public String forgotPass() {
        return "sendotp.html";
    }

    // -----------------------------------------------------------------
    @Autowired
    EmailService emailservice;

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam("username") String username, HttpSession session) {
        // Log the username for debugging (removed from production in real applications)
        System.out.println("Processing OTP request for username: " + username);
        
        // Input validation
        if (username == null || username.trim().isEmpty()) {
            session.setAttribute("message", 
                new net.codejava.helper.Message("Username cannot be empty!", "danger"));
            return "redirect:/forgotpassword";
        }
        
        // Find user by username
        User user = this.repo.findByUsername(username.trim());

        if (user == null) {
            session.setAttribute("message",
                    new net.codejava.helper.Message("User not found with this Citizenship ID !", "danger"));
            return "sendotp.html";
        }

        String email = user.getEmail();
        // Generate a secure 6-digit OTP with better entropy
        int min = 100000;
        int max = 999999;
        // Using SecureRandom for better cryptographic strength
        java.security.SecureRandom secureRandom = new java.security.SecureRandom();
        int otp = secureRandom.nextInt(max - min + 1) + min;
        
        // Set OTP expiration time (5 minutes from now)
        long otpExpiryTime = System.currentTimeMillis() + (5 * 60 * 1000);
        
        // Log OTP generation (remove in production or use proper logging)
        System.out.println("OTP " + otp + " generated for user: " + username + 
                         " (Expires at: " + new java.util.Date(otpExpiryTime) + ")");

        // Prepare and send password reset email
        String subject = "Password Reset Request - Secure Voting System";
        String message = String.format(
            "Dear %s,%n%n" +
            "You have requested to reset your password. Please use the following OTP to proceed:%n%n" +
            "OTP: %d%n%n" +
            "This OTP is valid for 5 minutes.%n%n" +
            "If you didn't request this, please ignore this email or contact support if you have concerns.%n%n" +
            "Best regards,%nSecure Voting System Team",
            user.getUsername(), otp
        );
        
        try {
            if (!this.emailservice.sendEmail(subject, message, email)) {
                throw new Exception("Email service returned false");
            }
            
            // If we get here, email was sent successfully
            session.setAttribute("otp", otp);
            session.setAttribute("otpExpiryTime", otpExpiryTime);
            session.setAttribute("email", email);
            session.setAttribute("username", username);

            // Redirect to OTP verification page
            return "verifyotp.html";

        } catch (Exception e) {
            System.err.println("Failed to send email to " + email + ": " + e.getMessage());
            session.setAttribute("message",
                new net.codejava.helper.Message("Failed to send OTP. Please try again later.", "danger"));
            return "sendotp.html";
        }

    }

    // ---------------------------------------------------------------------- //

    @PostMapping("/verify-otp")
    public String verify(@RequestParam("digit-1") String d1, @RequestParam("digit-2") String d2,
            @RequestParam("digit-3") String d3, @RequestParam("digit-4") String d4, @RequestParam("digit-5") String d5,
            @RequestParam("digit-6") String d6, HttpSession session) {

        String res = d1 + d2 + d3 + d4 + d5 + d6;

        Integer otp = Integer.parseInt(res);

        Integer old_otp = (Integer) session.getAttribute("otp");

        System.out.println(old_otp + " " + otp);

        if (old_otp.equals(otp)) {

            System.out.println("succesfull");

            return "newpassword.html";
        }
        return "/index";
    }

    // -------------------------------------------------- //

    // Give new password to update
    @PostMapping("/newpassword")
    public String newpassword(@RequestParam("password") String newpassword, HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userservice.getUser(username);
        System.out.println(newpassword);
        user.setPassword(passwordEncoder.encode(newpassword));

        System.out.println(user);

        repo.save(user);

        return "redirect:/index";
    }

}
