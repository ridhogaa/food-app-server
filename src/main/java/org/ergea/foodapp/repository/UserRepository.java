package org.ergea.foodapp.repository;

import org.ergea.foodapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Boolean existsByEmailAddress(String email);

    Boolean existsByUsername(String username);

    @Transactional
    @Procedure("delete_user")
    void deleteQuerySP(@PathVariable("id") UUID id);

    @Procedure("update_user")
    void updateQuerySP(@Param("p_id") UUID id,
                       @Param("p_username") String username,
                       @Param("p_email_address") String emailAddress,
                       @Param("p_password") String password
    );

    @Procedure("insert_user")
    void createQuerySP(@Param("p_id") UUID id,
                       @Param("p_username") String username,
                       @Param("p_email_address") String emailAddress,
                       @Param("p_password") String password
    );

    @Query(value = "SELECT * FROM get_user_by_id(:id)", nativeQuery = true)
    Object findByIdQuerySP(@Param("id") UUID id);
}
