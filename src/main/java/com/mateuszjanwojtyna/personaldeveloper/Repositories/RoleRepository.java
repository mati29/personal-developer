package com.mateuszjanwojtyna.personaldeveloper.Repositories;

import com.mateuszjanwojtyna.personaldeveloper.Entities.Role;
import org.springframework.data.repository.Repository;

public interface RoleRepository extends Repository<Role, Integer> {

    Role findByRole(String role);

}
