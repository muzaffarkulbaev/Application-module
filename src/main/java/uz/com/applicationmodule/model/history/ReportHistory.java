package uz.com.applicationmodule.model.history;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "report_history")
public class ReportHistory {

    public ReportHistory(String date,Integer countOfActive,Integer countOfConsidering,Integer countOfInActive,Integer countOfDeleted){
        this.date = date;
        this.countOfActive = countOfActive;
        this.countOfConsidering = countOfConsidering;
        this.countOfInActive = countOfInActive;
        this.countOfDeleted = countOfDeleted;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String date;
    private Integer countOfActive;
    private Integer countOfConsidering;
    private Integer countOfInActive;
    private Integer countOfDeleted;

}
