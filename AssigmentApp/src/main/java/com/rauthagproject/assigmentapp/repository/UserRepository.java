package com.rauthagproject.assigmentapp.repository;

import com.rauthagproject.assigmentapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//CRUD for User
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
