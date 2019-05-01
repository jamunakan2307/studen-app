package com.busyqa.studentportal.controller;

import com.busyqa.studentportal.message.response.ApiResponse;
import com.busyqa.studentportal.model.Team;
import com.busyqa.studentportal.model.TeamName;
import com.busyqa.studentportal.repo.TeamRepository;
import com.busyqa.studentportal.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/pm/users")
public class ManagerUserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @GetMapping("/{username}")
    public ApiResponse<User> getTeam(@PathVariable String username) {
        ApiResponse<User> userApiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Users Not found", null);
        Optional<com.busyqa.studentportal.model.User> adminUser = userRepository.findByUsername(username);
        Set<Team> adminTeam = adminUser.get().getTeams();
        List<com.busyqa.studentportal.model.User> userList = new ArrayList<>();

        for (Team team : adminTeam) {
            if (team.getName().equals(TeamName.TEAM_ADMIN.name())) {
                userList = userRepository.findAll();
                break;
            } else {
                userList = userRepository.findByTeams(team);
            }
        }
        return new ApiResponse<>(HttpStatus.OK.value(), "User fetched successfully.", userList);
    }
}
