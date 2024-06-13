package com.skyhorsemanpower.payment.quartz;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {

    private Scheduler scheduler;

    @PostConstruct
    private void jobProgress() throws SchedulerException {
        cronScheduler();
    }

    private void cronScheduler() throws SchedulerException {
        JobDetail job = JobBuilder
            .newJob(EventPreviewJob.class)
            .withIdentity("eventPreview", "alarmGroup")
            .withDescription("행사 시작 미리 알림 이벤트 produce job")
            .build();

        CronTrigger cronTrigger = TriggerBuilder
            .newTrigger()
            .withIdentity("eventPreview", "alarmGroup")         // Trigger 이름, 그룹 지정
            .withDescription("행사 시작 미리 알림 이벤트 produce trigger")     // Trigger 설명
            .startNow()
            .withSchedule(CronScheduleBuilder.cronSchedule("0/3 * * * * ?")).build();

        scheduler = new StdSchedulerFactory().getScheduler();
        EventPreviewJobListener eventPreviewJobListener = new EventPreviewJobListener();
        scheduler.getListenerManager().addJobListener(eventPreviewJobListener);
        scheduler.start();
        scheduler.scheduleJob(job, cronTrigger);
    }
}
