package com.mateuszjanwojtyna.personaldeveloper.Controllers;

import com.mateuszjanwojtyna.personaldeveloper.Entities.User;
import com.mateuszjanwojtyna.personaldeveloper.DTO.UserData;
import com.mateuszjanwojtyna.personaldeveloper.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping({"/users"})
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/new")
    public UserData create(@RequestBody UserData userData){
        userService.create(new User(userData));
        return userData;
    }

    @GetMapping(path = {"/user/{id}"})
    public UserData findOne(@PathVariable("id") int id){
        return new UserData(userService.findById(id));
    }

    @PutMapping
    public User update(@RequestBody User user){
        return userService.update(user);
    }

    @DeleteMapping(path ={"/user/{id}"})
    public UserData delete(@PathVariable("id") int id) {
        return new UserData(userService.delete(id));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/user")
    public List findAll(){
        List users = new ArrayList<UserData>();
        userService.findAll().forEach( user -> users.add(new UserData((User)user)));
        return users;
    }
}