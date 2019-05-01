package com.busyqa.studentportal.security.services;

import com.busyqa.studentportal.model.User;
import com.busyqa.studentportal.repo.UserDao;
import com.busyqa.studentportal.repo.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));

        return UserPrinciple.build(user);
    }

    public User update(User userDto) {
        Optional<User> user = userRepository.findById(Long.valueOf(userDto.getId()));
        user.get().setEmail(userDto.getEmail());
        user.get().setTeams(userDto.getTeams());
        user.get().setRoles((userDto.getRoles()));
        user.get().setName(userDto.getName());
        user.get().setUsername(userDto.getUsername());
        if (!userDto.getStatus().equals(user.get().getStatus())){
            user.get().setStatus(userDto.getStatus());
            user.get().setStatusAsOfDay(LocalDateTime.now().toString());
        }

        if(user.get() != null) {
            BeanUtils.copyProperties(userDto, user, "password");
            userRepository.save(user.get());

        }
        return userDto;
    }

    public void delete(int id) {
        userRepository.deleteById(Long.valueOf(id));
    }

}