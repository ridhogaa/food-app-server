package org.ergea.foodapp.repository;

import org.ergea.foodapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Boolean existsByEmailAddress(String email);

    Boolean existsByUsername(String username);

    User findByUsername(String username);
}
