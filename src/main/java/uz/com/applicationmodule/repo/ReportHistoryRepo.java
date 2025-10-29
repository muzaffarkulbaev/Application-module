package uz.com.applicationmodule.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.com.applicationmodule.model.history.ReportHistory;

import java.util.Optional;

@Repository
public interface ReportHistoryRepo extends JpaRepository<ReportHistory,Integer> {

    @Query(value = "select * from report_history where date = :date", nativeQuery = true)
    Optional<ReportHistory> findByDate(@Param("date") String date);

}
