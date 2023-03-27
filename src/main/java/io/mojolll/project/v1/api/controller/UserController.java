package io.mojolll.project.v1.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("home")
    public String home(){
        return "home";
    }
}
