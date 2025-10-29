package uz.com.applicationmodule.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.com.applicationmodule.model.entity.Application;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    @Query(value = "select * from applications where status != 'DELETED'",nativeQuery = true)
    List<Application> findAllNoDeletedStatus();

    @Query(value = "select * from applications where user_id = :id and status != 'DELETED'",nativeQuery = true)
    List<Application> findByUserIdWithNoDeletedStatus(@Param("id") Integer id);
}
