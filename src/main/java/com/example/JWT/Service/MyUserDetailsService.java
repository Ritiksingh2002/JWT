package com.example.JWT.Service;

import com.example.JWT.Model.UserPrincipal;
import com.example.JWT.Model.Users;
import com.example.JWT.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user= userRepo.findByUsername(username);
        if(user==null){
            System.out.println("user not ounf");
            throw  new UsernameNotFoundException("user not found");
        }
        return new UserPrincipal(user);
    }
}
