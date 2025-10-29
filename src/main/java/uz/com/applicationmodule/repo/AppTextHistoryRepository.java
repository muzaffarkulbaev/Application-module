package uz.com.applicationmodule.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.com.applicationmodule.model.history.AppTextHistory;

public interface AppTextHistoryRepository extends JpaRepository<AppTextHistory, Integer> {
}