package com.mateuszjanwojtyna.personaldeveloper.Controllers;

import com.mateuszjanwojtyna.personaldeveloper.Entities.User;
import com.mateuszjanwojtyna.personaldeveloper.DTO.UserData;
import com.mateuszjanwojtyna.personaldeveloper.Services.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping({"/users"})
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new")
    public UserData create(@Valid @RequestBody UserData userData){
        return Optional.ofNullable(userData)
                .map(User::new)
                .map(userService::create)
                .map(UserData::new)
                .orElse(null);
    }

    @GetMapping(path = {"/user/{id}"})
    public UserData findOne(@PathVariable("id") int id){
        return Optional.ofNullable(userService.findById(id))
                .map(UserData::new)
                .orElse(null);
    }

    @PutMapping
    public User update(@RequestBody User user){
        return userService.update(user);
    }

    @DeleteMapping(path ={"/user/{id}"})
    public UserData delete(@PathVariable("id") int id) {
        return Optional.ofNullable(userService.delete(id))
                .map(UserData::new)
                .orElse(null);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/user")
    public List<UserData> findAll(){
        return userService
                .findAll()
                .stream()
                .map(UserData::new)
                .collect(Collectors.toList());
    }
}