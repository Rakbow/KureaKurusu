package com.rakbow.kureakurusu.event;

import org.springframework.stereotype.Component;

/**
 * @author Rakbow
 * @since 2022-09-13 19:53
 */
@Component
public class EventConsumer{

//    private static final Logger log = LoggerFactory.getLogger(EventConsumer.class);
//
//    @Resource
//    private MessageService messageService;
//
//    @KafkaListener(topics = {CommonConstant.TOPIC_COMMENT, CommonConstant.TOPIC_LIKE, CommonConstant.TOPIC_FOLLOW})
//    public void handleCommentMessage(ConsumerRecord record) {
//        if (record == null || record.value() == null) {
//            log.error("消息的内容为空!");
//            return;
//        }
//
//        Event event = JsonUtil.to(record.value().toString(), Event.class);
//        if (event == null) {
//            log.error("消息格式错误!");
//            return;
//        }
//
//        // 发送站内通知
//        Message message = new Message();
//        message.setFromId(CommonConstant.SYSTEM_USER_ID);
//        message.setToId(event.getEntityUserId());
//        message.setConversationId(event.getTopic());
//        message.setCreateTime(new Date());
//
//        Map<String, Object> content = new HashMap<>();
//        content.put("userId", event.getUserId());
//        content.put("entityType", event.getEntityType());
//        content.put("entityId", event.getEntityId());
//
//        if (!event.getData().isEmpty()) {
//            content.putAll(event.getData());
//        }
//
//        message.setContent(JsonUtil.toJson(content));
//        messageService.addMessage(message);
//    }
}
