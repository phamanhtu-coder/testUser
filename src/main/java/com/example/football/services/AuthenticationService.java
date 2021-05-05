package com.example.football.services;

public interface AuthenticationService {
    void authenticate(String email, String password) throws Exception;
}
