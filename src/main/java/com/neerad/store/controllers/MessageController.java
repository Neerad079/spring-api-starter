package com.neerad.store.controllers;

import com.neerad.store.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @RequestMapping("/hello")
    public Message sayhello(){

        return new Message("Hello World") ;
    }
}
