package uz.com.applicationmodule.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.com.applicationmodule.model.base.BaseEntity;
import uz.com.applicationmodule.model.enums.AppStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "applications")
public class Application extends BaseEntity {

    public Application(String text,AppStatus status,User user){
        this.createdTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.text = text;
        this.status = status;
        this.user = user;
    }

    private String text;
    @Enumerated(value = EnumType.STRING)
    private AppStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
