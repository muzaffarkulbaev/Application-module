package uz.com.applicationmodule.service.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.com.applicationmodule.model.dto.requests.app.AppReportPeriodRequestDto;
import uz.com.applicationmodule.model.dto.responses.app.AppReportResponseDto;
import uz.com.applicationmodule.model.enums.AppStatus;
import uz.com.applicationmodule.model.history.ApplicationHistory;
import uz.com.applicationmodule.model.history.ReportHistory;
import uz.com.applicationmodule.repo.ApplicationHistoryRepo;
import uz.com.applicationmodule.repo.ReportHistoryRepo;
import uz.com.applicationmodule.service.abstractions.AppReportService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AppReportServiceImpl implements AppReportService {

    private final ApplicationHistoryRepo applicationHistoryRepo;
    private final ReportHistoryRepo reportHistoryRepo;

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final Comparator<ApplicationHistory> comparator = new Comparator<>() {
        @Override
        public int compare(ApplicationHistory o1, ApplicationHistory o2) {
            return -o1.getCreatedTime().compareTo(o2.getCreatedTime());
        }
    };

    @Override
    public AppReportResponseDto reportForDay(String dateString) {
        LocalDate date = LocalDate.parse(dateString,formatter);
        return reportByExactDate(date);
    }

    private AppReportResponseDto reportByExactDate(LocalDate date){

        System.out.println("date.toString() = " + date.toString());
        Optional<ReportHistory> optionalReportHistory = reportHistoryRepo.findByDate(date.toString());
        if (optionalReportHistory.isPresent()){
            ReportHistory reportHistory = optionalReportHistory.get();
            System.out.println("Present");
            return new AppReportResponseDto(
                    date.toString(),
                    reportHistory.getCountOfActive(),
                    reportHistory.getCountOfConsidering(),
                    reportHistory.getCountOfInActive(),
                    reportHistory.getCountOfDeleted()
            );
        }else System.out.println("Empty");

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23,59,59);

        List<ApplicationHistory> applicationHistoryList = applicationHistoryRepo.findAllByDay(start,end);

        for (ApplicationHistory applicationHistory : applicationHistoryList) {
            System.out.println("applicationHistory.getNewStatus() = " + applicationHistory.getNewStatus());
        }

        HashMap<Integer, TreeSet<ApplicationHistory>> hashMap = new HashMap<>();
        for (ApplicationHistory appHistory : applicationHistoryList) {
            Integer app_id = appHistory.getApplication().getId();
            if (hashMap.containsKey(app_id)) {
                hashMap.get(app_id).add(appHistory);
            }else {
                TreeSet<ApplicationHistory> treeSet = new TreeSet<>(comparator);
                treeSet.add(appHistory);
                hashMap.put(app_id, treeSet);
            }
        }

        int countOfActive = 0;
        int countOfInActive = 0;
        int countOfConsidering = 0;
        int countOfDeleted = 0;

        for (Map.Entry<Integer, TreeSet<ApplicationHistory>> setEntry : hashMap.entrySet()) {
            ApplicationHistory applicationHistory = setEntry.getValue().first();
            AppStatus oldStatus = applicationHistory.getOldStatus();
            AppStatus newStatus = applicationHistory.getNewStatus();

            if (oldStatus == null && newStatus == null) continue;

            if (newStatus == AppStatus.DELETED) countOfDeleted++;
            else if (newStatus == AppStatus.ACTIVE) countOfActive++;
            else if (newStatus == AppStatus.CONSIDERING) countOfConsidering++;
            else if (newStatus == AppStatus.INACTIVE) countOfInActive++;
        }


        System.out.println("LocalDate.now() = " + LocalDate.now());
        if (!date.equals(LocalDate.now())){
            System.out.println("Saving report");
            ReportHistory reportHistory = new ReportHistory(date.toString(), countOfActive, countOfConsidering, countOfInActive, countOfDeleted);
            reportHistoryRepo.save(reportHistory);
        }


        return new AppReportResponseDto(date.toString(),countOfActive,countOfConsidering,countOfInActive,countOfDeleted);
    }

    @Override
    public List<AppReportResponseDto> reportForPeriod(AppReportPeriodRequestDto appReportPeriodRequestDto) {

        LocalDate localDateStart = LocalDate.parse(appReportPeriodRequestDto.startPeriod(), formatter);
        LocalDate localDateEnd = LocalDate.parse(appReportPeriodRequestDto.endPeriod(), formatter);

        List<LocalDate> dates = localDateStart.datesUntil(localDateEnd.plusDays(1)).toList();

        return dates.stream().map(this::reportByExactDate).toList();
    }
}
