package com.busyqa.studentportal.message.request;

import com.busyqa.studentportal.model.StatusName;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;


public class SignUpForm {
    @NotBlank
    @Size(min = 6, max = 40)
    private String name;

    @NotBlank
    @Size(min =  6, max = 40)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    private Set<String> teams;

    private Set<String> role;;

    private String status;

    private String statusAsOfDay;


    @NotBlank
    @Size(min = 8, max = 40)
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String>role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getTeam() {
        return teams;
    }

    public void setTeam(Set<String> team) {
        this.teams = team;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status.equals(StatusName.NO.name())){
            this.status = status;
        }

        else
        {
            this.status = StatusName.YES.name();
        }

    }

    public String getStatusAsOfDay() {
        return statusAsOfDay;
    }

    public void setStatusAsOfDay(String statusAsOfDay) {
        this.statusAsOfDay = LocalDateTime.now().toString();
    }
}