package com.example.Pfeproject.repository;

import com.example.Pfeproject.entity.User;
import com.example.Pfeproject.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    

    Optional<User> findFirstByEmail(String email);

    User findByUserRole(UserRole admin);

    User findFirstByUserRole(UserRole admin);
}
