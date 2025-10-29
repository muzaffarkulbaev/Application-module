package uz.com.applicationmodule.service.abstractions;

import uz.com.applicationmodule.model.dto.requests.app.AppReportPeriodRequestDto;
import uz.com.applicationmodule.model.dto.responses.app.AppReportResponseDto;

import java.util.List;

public interface AppReportService {

    AppReportResponseDto reportForDay(String date);
    List<AppReportResponseDto> reportForPeriod(AppReportPeriodRequestDto appReportPeriodRequestDto);

}
