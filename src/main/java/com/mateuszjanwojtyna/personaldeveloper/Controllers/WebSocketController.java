package com.mateuszjanwojtyna.personaldeveloper.Controllers;

import com.mateuszjanwojtyna.personaldeveloper.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class WebSocketController {

    private SimpMessageSendingOperations messagingTemplate;
    private UserService userService;

    public WebSocketController(SimpMessageSendingOperations messagingTemplate, UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    @MessageMapping("/check")
    @SendTo("/usernameUnique/reply")
    public boolean processMessageFromClient(@RequestBody String message) {
        return userService.usernameUnique(message);
    }

    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        messagingTemplate.convertAndSend("/errors", exception.getMessage());
        return exception.getMessage();
    }

}