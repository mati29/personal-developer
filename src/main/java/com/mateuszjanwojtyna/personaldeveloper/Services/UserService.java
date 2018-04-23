package com.mateuszjanwojtyna.personaldeveloper.Services;

import com.mateuszjanwojtyna.personaldeveloper.Entities.User;

import java.util.List;

public interface UserService {
    User create(User user);
    User delete(int id);
    List findAll();
    User findById(int id);
    User update(User user);
    User findOne(String username);
    boolean usernameUnique(String username);
}
