package com.beside.ties;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class HelloApi {


    @GetMapping
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok().body("it's moyora's server");
    }
}
