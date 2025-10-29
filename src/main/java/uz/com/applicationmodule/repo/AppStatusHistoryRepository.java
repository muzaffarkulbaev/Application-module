package uz.com.applicationmodule.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.com.applicationmodule.model.history.AppStatusHistory;

public interface AppStatusHistoryRepository extends JpaRepository<AppStatusHistory, Integer> {
}