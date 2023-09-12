package org.diatliuk.bookstore.repository.role;

import java.util.Optional;
import org.diatliuk.bookstore.enums.RoleName;
import org.diatliuk.bookstore.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
