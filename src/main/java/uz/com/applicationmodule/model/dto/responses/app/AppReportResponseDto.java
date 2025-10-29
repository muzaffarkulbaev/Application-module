package uz.com.applicationmodule.model.dto.responses.app;

public record AppReportResponseDto(String date,Integer countOfActive, Integer countOfConsidering, Integer countOfInActive, Integer countOfDeleted) {
}
