package com.example.football.api.controllers;

import com.example.football.infrastructure.security.CookieUtil;
import com.example.football.infrastructure.security.JwtUtil;
import com.example.football.models.JwtRequest;
import com.example.football.models.JwtResponse;
import com.example.football.models.User;
import com.example.football.services.AuthenticationService;
import com.example.football.services.Impl.UserServiceImpl;
import com.example.football.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/testUser")
public class UserController {
    private static final String jwtTokenCookieName = "JWT-TOKEN";
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserServiceImpl jwtUserDetailsService;

    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public User register(@RequestBody User user) {
        return userService.createUser(user);
    }


    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, HttpServletResponse httpServletResponse) throws Exception {
        authenticationService.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(userService.loginUser(userDetails, httpServletResponse)));
    }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.POST)

    public ResponseEntity<?> deleteAuthenticationToken(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) throws Exception {
        String jwt = CookieUtil.getValue(httpServletRequest, jwtTokenCookieName);
        if(null == jwt) {
            System.out.println("Chua login | khong the lay token trong cookie");
            // TODO return;
        }
        // kiem tra token duoc luu trong redis xem co hay khong
        // TODO
        // Neu dung thi tiep tuc
        String username = jwtUtil.getUsernameFromToken(jwt);
        System.out.println("username in cookie = " + username);

//        authenticationService.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//        String username = authenticationRequest.getUsername();

        httpServletRequest.setAttribute("username", username);
        return ResponseEntity.ok(new JwtResponse(userService.logoutUser(httpServletRequest, httpServletResponse)));
    }

    @RequestMapping(value = {"/user/getAll"}, method = RequestMethod.GET)
    public ResponseEntity<?> list() throws Exception {

        return new ResponseEntity<>(userService.listAllUser(), HttpStatus.OK);
    }

    @RequestMapping(value = {"/userById/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<User> get(@PathVariable Integer id) {
        try {
            User user = userService.getByIdUser(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(value = {"/user/{username}"}, method = RequestMethod.GET)
    public ResponseEntity<User> getByUsername(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = {"/userByUsername"}, method = RequestMethod.GET)
    public ResponseEntity<User> getUsername(HttpServletRequest httpServletRequest) {
        String jwt = CookieUtil.getValue(httpServletRequest, jwtTokenCookieName);
        if(null == jwt) {
            System.out.println("Chua login | khong the lay token trong cookie");
            // TODO return;
        }
        // kiem tra token duoc luu trong redis xem co hay khong
        // TODO
        // Neu dung thi tiep tuc
        String username = jwtUtil.getUsernameFromToken(jwt);
        System.out.println("username in cookie = " + username);
        try {
            User user = userService.findByUsername(username);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = {"/user/update/{id}"}, method = RequestMethod.POST)
    public ResponseEntity<?> updateById(@RequestBody User user, @PathVariable Integer id) {
        try {
            User existUser = userService.getByIdUser(id);
            try {
                userService.saveUser(user);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception internalError) {
                internalError.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = {"/user/updatePassWordByUsername"}, method = RequestMethod.POST)
    public ResponseEntity<?> updatePassWordByUsername(@RequestBody User user, HttpServletRequest httpServletRequest) {
        String jwt = CookieUtil.getValue(httpServletRequest, jwtTokenCookieName);
        if(null == jwt) {
            System.out.println("Chua login | khong the lay token trong cookie");
            // TODO return;
        }
        // kiem tra token duoc luu trong redis xem co hay khong
        // TODO
        // Neu dung thi tiep tuc
        String username = jwtUtil.getUsernameFromToken(jwt);
        System.out.println("username in cookie = " + username);
        try {
            userService.updatePassWordUser(user, username);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception internalError) {
            internalError.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = {"/user/updateProfileByUsername/{username}"}, method = RequestMethod.POST)
    public ResponseEntity<?> updateProfileByUsername(@RequestBody User user, @PathVariable String username) {
            try {
                userService.updateProfileUser(user, username);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception internalError) {
                internalError.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }


    @RequestMapping(value = {"/user/delete/{id}"}, method = RequestMethod.POST)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            User existUser = userService.getByIdUser(id);
            userService.deleteUser(existUser.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(value = {"/user/delete/{username}"}, method = RequestMethod.POST)
    public ResponseEntity<?> deleteByUsername(@RequestBody User user, @PathVariable String username) {
        try {
            User existUser = userService.findByUsername(username);
            userService.deleteUser(existUser.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
