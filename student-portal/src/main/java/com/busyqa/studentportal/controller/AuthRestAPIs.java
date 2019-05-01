package com.busyqa.studentportal.controller;


import com.busyqa.studentportal.message.request.LoginForm;
import com.busyqa.studentportal.message.request.SignUpForm;
import com.busyqa.studentportal.message.response.JwtResponse;
import com.busyqa.studentportal.message.response.ResponseMessage;
import com.busyqa.studentportal.model.*;
import com.busyqa.studentportal.repo.RoleRepository;
import com.busyqa.studentportal.repo.TeamRepository;
import com.busyqa.studentportal.repo.UserRepository;
import com.busyqa.studentportal.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(
                new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(
                    new ResponseMessage("Fail -> Username is already taken!"), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(
                    new ResponseMessage("Fail -> Email is already in use!"), HttpStatus.BAD_REQUEST);
        }


        // creating user account
        User user =
                new User(
                        signUpRequest.getName(),
                        signUpRequest.getUsername(),
                        signUpRequest.getEmail(),
                        encoder.encode(signUpRequest.getPassword()));
        if (signUpRequest.getRole().size() == signUpRequest.getTeam().size()){
            Set<String> strTeams = signUpRequest.getTeam();
            Set<Team> teams = new HashSet<>();
            strTeams.forEach(
                    team -> {
                        switch (team) {
                            case ("TEAM_SALES"):
                                Team salesTeam =
                                        teamRepository
                                                .findByName(TeamName.TEAM_SALES.name())
                                                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Team  not find."));
                                teams.add(salesTeam);
                                break;
                            case "TEAM_ACCOUNTS":
                                Team accountsteam =
                                        teamRepository
                                                .findByName(TeamName.TEAM_ACCOUNTS.name())
                                                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Team  not find."));
                                teams.add(accountsteam);
                                break;
                            case "TEAM_ADMIN":
                                Team adminteam =
                                        teamRepository
                                                .findByName(TeamName.TEAM_ADMIN.name())
                                                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Team  not find."));
                                teams.add(adminteam);
                                break;

                            default:
                                Team userTeam =
                                        teamRepository
                                                .findByName(TeamName.TEAM_UNASSIGNED.name())
                                                .orElseThrow(
                                                        () -> new RuntimeException("Fail! -> Cause: Team  not find."));
                                teams.add(userTeam);
                        }
                    });
            user.setTeams(teams);

            ///Role Mapping
            Set<String> strRoles = signUpRequest.getRole();
            Set<Role> roles = new HashSet<>();

            strRoles.forEach(
                    role -> {
                        switch (role) {
                            case "ROLE_ADMIN":
                                Role adminRole =
                                        roleRepository
                                                .findByName(RoleName.ROLE_ADMIN.name())
                                                .orElseThrow(
                                                        () -> new RuntimeException("Fail! -> Cause: User Role not find."));
                                roles.add(adminRole);
                                break;

                            case "ROLE_PM":
                                Role pmRole =
                                        roleRepository
                                                .findByName(RoleName.ROLE_PM.name())
                                                .orElseThrow(
                                                        () -> new RuntimeException("Fail! -> Cause: User Role not find."));
                                roles.add(pmRole);
                                break;
                            default:
                                Role userRole =
                                        roleRepository
                                                .findByName(RoleName.ROLE_USER.name())
                                                .orElseThrow(
                                                        () -> new RuntimeException("Fail! -> Cause: User Role not find."));
                                roles.add(userRole);
                        }
                    });
            user.setRoles(roles);
            userRepository.save(user);

        }

        else {
            return new ResponseEntity<>(
                    new ResponseMessage("Mismatch between roles and team assignment !"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }
}

