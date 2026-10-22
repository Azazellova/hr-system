package ru.hr.backend.hr.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** Observer: слушатель кадровых событий. Сейчас уведомление пишется в журнал; позже можно подключить email/очередь. */
@Component
public class PersonnelEventObserver {
    private static final Logger log = LoggerFactory.getLogger(PersonnelEventObserver.class);

    @EventListener
    public void onPersonnelEvent(PersonnelEvent event) {
        log.info("Кадровое событие: employeeId={}, action={}, status={}, reason={}, at={}",
                event.getEmployeeId(), event.getAction(), event.getStatus(), event.getReason(), event.getOccurredAt());
    }
}
