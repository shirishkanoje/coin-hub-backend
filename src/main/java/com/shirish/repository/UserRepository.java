package com.shirish.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shirish.modal.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
