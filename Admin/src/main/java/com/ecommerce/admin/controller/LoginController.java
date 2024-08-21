package com.ecommerce.admin.controller;


import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.service.impl.AdminSeviceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @Autowired
    private AdminSeviceImpl adminSevice;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("title", "Login");
        return "login";
    }

    @RequestMapping("/index")
    public String home(Model model){
        model.addAttribute("title", "Home Page");
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("title", "Register");
        model.addAttribute("adminDto",new AdminDto());
        return "register";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model){
        model.addAttribute("title", "Forgot Password");
        return "password";
    }

    @PostMapping("/regiter-new")
    public String addNewAdmin(@Valid @ModelAttribute("adminDto") AdminDto adminDto, BindingResult bindingResult, Model model){
        try{
            if(bindingResult.hasErrors()){
                model.addAttribute("adminDto",adminDto);
//                bindingResult.toString();
                return "register";
            }
            String username = adminDto.getUsername();
            Admin admin = adminSevice.findByUsername(username);
            if(admin != null){
                model.addAttribute("adminDto",adminDto);
                model.addAttribute("emailError", "Your email has been regsitered");
                return "register";
            }
            if(adminDto.getPassword().equals(adminDto.getReapeatPassword())){
                adminSevice.save(adminDto);
                model.addAttribute("adminDto", new AdminDto());
                model.addAttribute("message","Registration successful");
            }else{
                model.addAttribute("adminDto",adminDto);
                model.addAttribute("errorPassNotSame", "Password is not the same");
                return "register";
            }
        }catch (Exception e){
            model.addAttribute("errorServer", "something went wrong in server");
        }
        return "register";
    }
}
