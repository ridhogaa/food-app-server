package org.ergea.master.repository.oauth2;

import org.ergea.master.entity.oauth2.Role;
import org.ergea.master.entity.oauth2.RolePath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<RolePath> {
    Role findOneByName(String name);

    List<Role> findByNameIn(String[] names);
}
