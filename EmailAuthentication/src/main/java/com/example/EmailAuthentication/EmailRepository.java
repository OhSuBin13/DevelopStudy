package com.example.EmailAuthentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<VerificationCode, Long> {
    public Optional<VerificationCode> findByEmailAndCode(String email, String code);
    public void deleteByExpiresTimeBefore(LocalDateTime time);
}
