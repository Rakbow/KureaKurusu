package com.rakbow.kureakurusu.listener;

import com.rakbow.kureakurusu.service.GeneralService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Rakbow
 * @since 2023-11-05 5:33
 */
@Component
public class ApplicationListener {

    @Resource
    private GeneralService generalService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        // 在应用启动后执行的代码
        generalService.loadMetaData();
    }

}
