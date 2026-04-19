package org.example.roomreservation.repository;

import org.example.roomreservation.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
