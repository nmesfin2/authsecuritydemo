package com.tcs.authdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.authdemo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
