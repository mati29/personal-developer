package com.mateuszjanwojtyna.personaldeveloper.Repositories;

import com.mateuszjanwojtyna.personaldeveloper.Entities.User;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface UserRepository extends Repository<User, Integer> {

    void delete(User user);

    List findAll();

    User findById(int id);

    User save(User user);

    User findByUsername(String username);
}
