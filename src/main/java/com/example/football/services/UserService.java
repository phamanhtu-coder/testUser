package com.example.football.services;

import com.example.football.models.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    User createUser(User user);

    User findByUsername(String username);

    String loginUser(UserDetails userDetails, HttpServletResponse httpServletResponse) throws ServletException;

    String logoutUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    User getByIdUser(Integer id);

    User updateProfileUser(User user, String username);

    User updatePassWordUser(User user, String username);

    void saveUser(User user);

    void deleteUser(Integer id);

    List<User> listAllUser();

}
