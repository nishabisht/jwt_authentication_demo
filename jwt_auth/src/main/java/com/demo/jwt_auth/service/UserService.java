package com.demo.jwt_auth.service;

import com.demo.jwt_auth.model.Users;
import com.demo.jwt_auth.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    //step 3
   @Autowired
    private AuthenticationManager authManager;


    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);

    public Users saveUser(Users user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public List<Users> getAllUser() {
        return userRepo.findAll();
    }

    //step 4
    public String verify(Users user) {
        Authentication authentication=authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(user);
        }
       return "fail";
    }
}
