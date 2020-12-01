package com.nat.authdemo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nat.authdemo.model.ERole;
import com.nat.authdemo.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role>  findByName(ERole name);
}
