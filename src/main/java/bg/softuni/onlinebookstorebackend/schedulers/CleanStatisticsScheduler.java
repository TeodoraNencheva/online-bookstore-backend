package bg.softuni.onlinebookstorebackend.schedulers;

import bg.softuni.onlinebookstorebackend.service.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanStatisticsScheduler {
    private final OrderService orderService;

    public CleanStatisticsScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "59 59 23 * * *")
    public void cleanNewOrdersCount() {
        orderService.cleanNewOrdersCount();
    }
}
