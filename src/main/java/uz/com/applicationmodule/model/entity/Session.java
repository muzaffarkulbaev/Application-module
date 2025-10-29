package uz.com.applicationmodule.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "sessions")
public class Session{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String token;
    private String ip;
    private String userAgent;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
