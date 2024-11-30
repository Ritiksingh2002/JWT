package com.example.JWT.Service;

import com.example.JWT.Model.Role;
import com.example.JWT.Model.UserRegistrationRequest;
import com.example.JWT.Model.Users;
import com.example.JWT.Repo.RoleRepo;
import com.example.JWT.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
   private  JWTService jwtService;
    @Autowired
    private RoleRepo roleRepo;

    private BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(12);

    public Users register(UserRegistrationRequest request){
        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));

        // Assign roles to the user
        Set<Role> roles = new HashSet<>();
        for (String roleName : request.getRoles()) {
            Role role = roleRepo.findByName(roleName);
            if (role == null) {
                // If role doesn't exist, create it
                role = new Role();
                role.setName(roleName);
                roleRepo.save(role);
            }
            roles.add(role);
        }

        // Set roles to the user
        user.setRoles(roles);

        // Save the user
        return userRepo.save(user);
    }




    public String verify(Users user) {
        System.out.println("role is +"+ user.getRoles());

        Authentication  authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        if(authentication.isAuthenticated()){
           Users authenticatedUser=userRepo.findByUsername(user.getUsername());
            return jwtService.generateToken(authenticatedUser);
        }
        return "fail";
    }
}
