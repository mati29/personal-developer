package com.mateuszjanwojtyna.personaldeveloper.Services.impl;

import com.mateuszjanwojtyna.personaldeveloper.Entities.User;
import com.mateuszjanwojtyna.personaldeveloper.Models.UserPrincipal;
import com.mateuszjanwojtyna.personaldeveloper.Repositories.UserRepository;
import com.mateuszjanwojtyna.personaldeveloper.Services.RoleService;
import com.mateuszjanwojtyna.personaldeveloper.Services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService,UserService {

    private UserRepository repository;
    private BCryptPasswordEncoder encoder;
    private RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder, RoleService roleService) {
        this.repository = userRepository;
        this.encoder = encoder;
        this.roleService = roleService;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new UserPrincipal(user);
    }

    @Override
    public User create(User user) {
        encodePassword(user);
        setStandardRole(user);
        return repository.save(user);
    }

    public User encodePassword(User user) {
        user.setPassword(
                encoder.encode(
                        user.getPassword()
                )
        );
        return user;
    }

    public User setStandardRole(User user) {
        user.setRoles(
                Collections.singletonList(
                        roleService.findByRole("ROLE_USER")
                )
        );
        return user;
    }

    @Override
    public User delete(int id) {
        User user = findById(id);
        if(user != null){
            repository.delete(user);
        }
        return user;
    }

    @Override
    public List findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(int id) {
        return repository.findById(id);
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User findOne(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public boolean usernameUnique(String username) {
        return !repository.existsByUsername(username);
    }
}
