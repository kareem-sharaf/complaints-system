package com.example.complaints.dao;

import com.example.complaints.entity.Role;
import com.example.complaints.entity.RoleName;

import java.util.Optional;

public interface RoleDao {
    Optional<Role> findByName(RoleName name);
    Role save(Role role);
}

