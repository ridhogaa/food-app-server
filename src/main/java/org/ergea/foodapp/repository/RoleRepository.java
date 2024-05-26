package org.ergea.foodapp.repository;

import org.ergea.foodapp.entity.oauth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Role findOneByName(String name);

    List<Role> findByNameIn(String[] names);
}