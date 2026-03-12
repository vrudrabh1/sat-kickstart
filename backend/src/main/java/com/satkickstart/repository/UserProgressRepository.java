package com.satkickstart.repository;

import com.satkickstart.model.Section;
import com.satkickstart.model.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    Optional<UserProgress> findBySessionIdAndSection(String sessionId, Section section);

    List<UserProgress> findBySessionId(String sessionId);
}
