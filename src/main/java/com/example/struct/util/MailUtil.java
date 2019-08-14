package com.example.struct.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

@Component
public class MailUtil {
  @Resource
  private JavaMailSender mailSender;
  @Value("${spring.mail.username}")
  private String username;

  public void sendEmail(MailEntity mailEntity) {
    try {
      final MimeMessage mimeMessage = mailSender.createMimeMessage();
      final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
      message.setFrom(username);
      if (StringUtils.isNotEmpty(mailEntity.to)) {
        message.setTo(mailEntity.to);
      }
      if (ArrayUtils.isNotEmpty(mailEntity.tos)) {
        message.setTo(mailEntity.tos);
      }
      if (StringUtils.isNotEmpty(mailEntity.cc)) {
        message.setCc(mailEntity.cc);
      }
      if (ArrayUtils.isNotEmpty(mailEntity.ccs)) {
        message.setCc(mailEntity.ccs);
      }
      if (StringUtils.isNotEmpty(mailEntity.subject)) {
        message.setSubject(mailEntity.subject);
      }
      if (StringUtils.isNotEmpty(mailEntity.text)) {
        message.setText(mailEntity.text);
      }
      if (StringUtils.isNotEmpty(mailEntity.relatPath)) {
        message.addAttachment("文件测试",new ClassPathResource(mailEntity.relatPath).getFile());
      }
      this.mailSender.send(mimeMessage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static class MailEntity {
    private String to;
    private String[] tos;
    private String cc;
    private String[] ccs;
    private String subject;
    private String text;
    private String relatPath;
    
    public static MailEntity createMailEntity(String to, String[] tos, String cc, String[] ccs, String subject,
        String text, String relatPath) {
      MailEntity mailEntity = new MailEntity();
      mailEntity.to = to;
      mailEntity.tos = tos;
      mailEntity.cc = cc;
      mailEntity.ccs = ccs;
      mailEntity.subject = subject;
      mailEntity.text = text;
      mailEntity.relatPath = relatPath;
      return mailEntity;
    }
  }
}
