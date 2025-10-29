package uz.com.applicationmodule.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.com.applicationmodule.model.entity.Session;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {

//    @Query("select s from Session s where s.token=:token")
    Optional<Session> findByToken(String token);

//    @Query("delete from Session where user.id = :id")
    void deleteAllByUserId(Integer id);
}