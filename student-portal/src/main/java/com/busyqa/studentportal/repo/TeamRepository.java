package com.busyqa.studentportal.repo;


import com.busyqa.studentportal.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String teamName);
    Optional<Team> findById(int id);

    List<Team> findAll();
}
