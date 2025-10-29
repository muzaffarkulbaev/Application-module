package uz.com.applicationmodule.model.history;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import uz.com.applicationmodule.model.entity.Application;
import uz.com.applicationmodule.model.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Immutable
@Table(name = "app_text_history")
public class AppTextHistory {

    public AppTextHistory() {
    }

    public AppTextHistory(Application application, User user, String oldText, String newText, LocalDateTime time) {
        this.application = application;
        this.user = user;
        this.newText = newText;
        this.oldText = oldText;
        this.time = time;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String oldText;
    private String newText;
    private LocalDateTime time;
}
