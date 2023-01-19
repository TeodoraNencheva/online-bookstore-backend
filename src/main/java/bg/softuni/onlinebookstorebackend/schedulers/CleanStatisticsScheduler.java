package bg.softuni.onlinebookstorebackend.schedulers;

import bg.softuni.onlinebookstorebackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CleanStatisticsScheduler {
    private final OrderService orderService;

    @Scheduled(cron = "59 59 23 * * *")
    public void cleanNewOrdersCount() {
        orderService.cleanNewOrdersCount();
    }
}
