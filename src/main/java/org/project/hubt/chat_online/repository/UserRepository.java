package org.project.hubt.chat_online.repository;

import org.project.hubt.chat_online.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  
  Optional<User> findByEmail(String email);
  
  @Query("SELECT u FROM User u WHERE " +
         "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
         "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
  List<User> searchByUsernameOrEmail(@Param("searchTerm") String searchTerm);
}