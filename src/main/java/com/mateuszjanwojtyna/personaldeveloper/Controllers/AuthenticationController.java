package com.mateuszjanwojtyna.personaldeveloper.Controllers;

import com.mateuszjanwojtyna.personaldeveloper.Configurations.JwtTokenUtil;
import com.mateuszjanwojtyna.personaldeveloper.Models.AuthToken;
import com.mateuszjanwojtyna.personaldeveloper.DTO.LoginUser;
import com.mateuszjanwojtyna.personaldeveloper.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/token")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @RequestMapping(value = "/generate-token", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody LoginUser loginUser) throws AuthenticationException {
        userSetAuthentication(loginUser);
        return Optional.ofNullable(loginUser)
                .map(LoginUser::getUsername)
                .map(userService::findOne)
                .map(jwtTokenUtil::generateToken)
                .map(AuthToken::new)
                .map(ResponseEntity::ok)
                .orElse(null);
    }

    public void userSetAuthentication(LoginUser loginUser) {
        Optional.ofNullable(loginUser)
                .map(user -> new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()))
                .map(authenticationManager::authenticate)
                .ifPresent(SecurityContextHolder.getContext()::setAuthentication);
    }

}