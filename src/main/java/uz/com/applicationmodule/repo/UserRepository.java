package uz.com.applicationmodule.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.applicationmodule.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query(value = "select * from users where users.id in (select users_roles.user_id from users_roles where users_roles.roles_id=5)",nativeQuery = true)
    List<User> findAllUsers();

}
