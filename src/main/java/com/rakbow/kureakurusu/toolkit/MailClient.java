package com.rakbow.kureakurusu.toolkit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Rakbow
 * @since 2022-08-02 0:56
 */
@Slf4j
@Component
public class MailClient {

    // @Autowired
    // private JavaMailSender mailSender;
    //
    // @Value("${spring.mail.username}")
    // private String from;
    //
    // /**
    //  * 发送邮件
    //  * 参数：to：发送目标 subject：主题  content：内容
    //  * */
    // public void sendMail(String to, String subject, String content) {
    //     try {
    //         MimeMessage message = mailSender.createMimeMessage();
    //         MimeMessageHelper helper = new MimeMessageHelper(message);
    //         helper.setFrom(from);
    //         helper.setTo(to);
    //         helper.setSubject(subject);
    //         helper.setText(content, true);
    //         mailSender.send(helper.getMimeMessage());
    //     } catch (MessagingException e) {
    //         log.error("发送邮件失败: {}", e.getMessage());
    //     }
    // }

}
