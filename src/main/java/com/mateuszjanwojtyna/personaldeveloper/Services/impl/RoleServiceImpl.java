package com.mateuszjanwojtyna.personaldeveloper.Services.impl;

import com.mateuszjanwojtyna.personaldeveloper.Entities.Role;
import com.mateuszjanwojtyna.personaldeveloper.Repositories.RoleRepository;
import com.mateuszjanwojtyna.personaldeveloper.Services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "roleService")
public class RoleServiceImpl implements RoleService{

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByRole(String role) {
        return roleRepository.findByRole(role);
    }

}
