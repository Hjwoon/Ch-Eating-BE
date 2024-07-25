package com.ch_eatimg.ch_eating.role;

import com.ch_eatimg.ch_eating.domain.RoleName;
import com.ch_eatimg.ch_eating.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}