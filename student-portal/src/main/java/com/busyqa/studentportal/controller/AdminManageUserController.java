package com.busyqa.studentportal.controller;


import com.busyqa.studentportal.message.response.ApiResponse;
import com.busyqa.studentportal.repo.UserRepository;
import com.busyqa.studentportal.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/admin/user")
public class AdminManageUserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsService;


    @GetMapping("/{id}")
    public ApiResponse<User> getOne(@PathVariable int id){
        return new ApiResponse<>(HttpStatus.OK.value(), "User fetched successfully.",userRepository.findById(Long.valueOf(id)));
    }

    @PutMapping("/{id}")
    public ApiResponse<com.busyqa.studentportal.model.User> update(@RequestBody com.busyqa.studentportal.model.User userDto) {
        return new ApiResponse<>(HttpStatus.OK.value(), "User updated successfully.",userDetailsService.update(userDto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable int id) {
        userDetailsService.delete(id);
        return new ApiResponse<>(HttpStatus.OK.value(), "User fetched successfully.", null);
    }

}
