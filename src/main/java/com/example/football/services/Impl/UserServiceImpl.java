package com.example.football.services.Impl;

import com.example.football.infrastructure.security.CookieUtil;
import com.example.football.infrastructure.security.JwtUtil;
import com.example.football.models.User;
import com.example.football.repositories.UserRepository;
import com.example.football.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final String jwtTokenCookieName = "JWT-TOKEN";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @Override
    public User createUser(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setCreated(new Date());
        return userRepository.save(user);
    }
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }



    @Override
    public String loginUser(UserDetails userDetails, HttpServletResponse httpServletResponse) throws ServletException {
        String status = userRepository.findStatusByUserName(userDetails.getUsername());
        System.out.println(status);
        if(status.equals("Deactive"))
        {
            throw new ServletException("Error Accout");
        }
        final String token = jwtUtil.generateToken(userDetails);
        CookieUtil.create(httpServletResponse, jwtTokenCookieName, token, false, -1, "localhost");
        return token;
    }

    @Override
    public String logoutUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        jwtUtil.invalidateRelatedTokens(httpServletRequest);
        CookieUtil.getValue(httpServletRequest, "JWT-TOKEN");
        CookieUtil.clear(httpServletResponse, jwtTokenCookieName);
        return "logout....";
    }

    @Override
    public User getByIdUser(Integer id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User updateProfileUser(User user, String username) {
        User existingUser = userRepository.findByUsername(username);
        existingUser.setFullname(user.getFullname());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        return userRepository.save(existingUser);
    }

    @Override
    public User updatePassWordUser(User user, String username) {
        User existingUser = userRepository.findByUsername(username);
        existingUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(existingUser);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> listAllUser() {

        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }
}