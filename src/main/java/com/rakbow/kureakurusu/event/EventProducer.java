package com.rakbow.kureakurusu.event;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-09-13 21:26
 * @Description:
 */
// @Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    // 处理事件
    public void fireEvent(Event event) {
        // 将事件发布到指定的主题
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }

}
