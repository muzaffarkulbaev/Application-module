package uz.com.applicationmodule.model.history;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.com.applicationmodule.model.entity.Application;
import uz.com.applicationmodule.model.entity.User;
import uz.com.applicationmodule.model.enums.AppStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "app_history")
public class ApplicationHistory {

    public ApplicationHistory(Application application){
        this.application = application;
        this.createdTime = application.getCreatedTime();
        this.oldText = null;
        this.newText = application.getText();
        this.oldStatus = null;
        this.newStatus = application.getStatus();
        this.changedBy = null;
    }

    public ApplicationHistory(Application application,String oldText, String newText, User changedBy){
        this.application = application;
        this.createdTime = LocalDateTime.now();
        this.oldText = oldText;
        this.newText = newText;
        this.newStatus = null;
        this.oldStatus = null;
        this.changedBy = changedBy;
    }

    public ApplicationHistory(Application application,AppStatus oldStatus, AppStatus newStatus, User changedBy){
        this.application = application;
        this.createdTime = LocalDateTime.now();
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.oldText = null;
        this.newText = null;
        this.changedBy = changedBy;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Application application;

    private LocalDateTime createdTime;
    private String oldText;
    private String newText;

    @Enumerated(value = EnumType.STRING)
    private AppStatus oldStatus;

    @Enumerated(value = EnumType.STRING)
    private AppStatus newStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private User changedBy;

}
