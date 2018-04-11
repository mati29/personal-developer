package com.mateuszjanwojtyna.personaldeveloper.Services.impl;

import com.mateuszjanwojtyna.personaldeveloper.Entities.Role;
import com.mateuszjanwojtyna.personaldeveloper.Entities.User;
import com.mateuszjanwojtyna.personaldeveloper.Models.UserPrincipal;
import com.mateuszjanwojtyna.personaldeveloper.Repositories.UserRepository;
import com.mateuszjanwojtyna.personaldeveloper.Services.RoleService;
import com.mateuszjanwojtyna.personaldeveloper.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService,UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private RoleService roleService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new UserPrincipal(user);//org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public User create(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(new ArrayList<Role>(Arrays.asList(roleService.findByRole("ROLE_ADMIN"))));//TODO
        return repository.save(user);
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
}
