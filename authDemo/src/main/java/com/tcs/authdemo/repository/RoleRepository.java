package com.tcs.authdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.authdemo.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
