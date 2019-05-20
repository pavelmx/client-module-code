package com.innowise.SpringBootOAuthClient.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {


    @GetMapping("/secured")
    public ResponseEntity<List<String>> test(){
        List<String> list = new ArrayList<>();
        list.add("item1 8082");
        list.add("item2");
        list.add("item3");
        list.add("item4");
        list.add("item5");
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
