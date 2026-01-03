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
        // generating otp
        // Integer otp = random.nextInt(999999);
        int min = 100000;
        int max = 999999;
        // Generate a 6-digit OTP
        Integer otp = (int) (Math.random() * (max - min + 1) + min);
        System.out.println("Generated OTP: " + otp);

        // write code for send otp to email...

        String subject = "Forget Password";
        String message = "OTP for renewing your password = " + otp;
        String to = email;
        boolean flag = this.emailservice.sendEmail(subject, message, to);

        if (flag) {
            session.setAttribute("otp", otp);
            session.setAttribute("email", email);
            session.setAttribute("username", username);

            // Integer check=(int) session.getAttribute("otp");
            return "verifyotp.html";

        } else {
            session.setAttribute("message",
                    new net.codejava.helper.Message("Failed to send OTP. Please try again!", "danger"));
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
