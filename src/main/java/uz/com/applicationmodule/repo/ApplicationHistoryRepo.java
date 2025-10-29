package uz.com.applicationmodule.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.com.applicationmodule.model.entity.Application;
import uz.com.applicationmodule.model.history.ApplicationHistory;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApplicationHistoryRepo extends JpaRepository<ApplicationHistory,Integer> {

    @Query(value = "select * from app_history where new_status = 'DELETED' and created_time between :start and :end",nativeQuery = true)
    List<ApplicationHistory> findDeletedBetweenTime(@Param("start") LocalDateTime start, @Param("end")LocalDateTime end);

    @Query(value = "select * from app_history where old_status is NULL and new_status = 'ACTIVE' and created_time between :start and :end",nativeQuery = true)
    List<ApplicationHistory> findCreatedBetweenTime(@Param("start") LocalDateTime start, @Param("end")LocalDateTime end);

    @Query(value = "select * from app_history where created_time between :start and :end",nativeQuery = true)
    List<ApplicationHistory> findAllByDay(LocalDateTime start, LocalDateTime end);

}
