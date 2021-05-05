package com.example.football.repositories;

import com.example.football.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM users u \n" +
            "WHERE u.username = :username", nativeQuery = true)
    User findByUsername(@Param("username") String username);

    @Query(value = "SELECT status FROM users u \n" +
            "WHERE u.username =:username", nativeQuery = true)
    String findStatusByUserName(@Param("username") String username);
}
