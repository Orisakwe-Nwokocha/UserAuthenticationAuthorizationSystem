package com.prunny.jwt_project.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @GetMapping
    public String home(Principal principal) {
        return "Hello, " + principal.getName();
    }

    @GetMapping("/admin")
    public String greetAdmin(Principal principal) {
        return "Hello, " + principal.getName();
    }
}
