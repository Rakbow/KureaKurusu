package com.rakbow.kureakurusu.event;

import com.rakbow.kureakurusu.data.entity.Event;
import com.rakbow.kureakurusu.util.common.JsonUtil;
import jakarta.annotation.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Rakbow
 * @since 2022-09-13 21:26
 */
@Component
public class EventProducer {

//    @Resource
//    private KafkaTemplate kafkaTemplate;
//
//    // 处理事件
//    public void fireEvent(Event event) {
//        // 将事件发布到指定的主题
//        kafkaTemplate.send(event.getTopic(), JsonUtil.toJson(event));
//    }

}
