package uz.com.applicationmodule.model.history;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import uz.com.applicationmodule.model.entity.Application;
import uz.com.applicationmodule.model.entity.User;
import uz.com.applicationmodule.model.enums.AppStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Immutable
@Table(name = "app_status_history")
public class AppStatusHistory {

    public AppStatusHistory() {
    }

    public AppStatusHistory(Application application,User user,AppStatus oldStatus,AppStatus newStatus,LocalDateTime time) {
        this.application = application;
        this.user = user;
        this.newStatus = newStatus;
        this.oldStatus = oldStatus;
        this.time = time;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private AppStatus oldStatus;
    @Enumerated(EnumType.STRING)
    private AppStatus newStatus;
    private LocalDateTime time;

}
