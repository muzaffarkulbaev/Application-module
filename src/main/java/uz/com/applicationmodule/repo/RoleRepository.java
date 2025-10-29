package uz.com.applicationmodule.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.com.applicationmodule.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String roleUser);
}