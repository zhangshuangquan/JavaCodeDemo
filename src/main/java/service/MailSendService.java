package service;

import model.OwnException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Properties;

/**
 * Created by zsq on 16/12/14.
 */
@Component
public class MailSendService {

    static Logger logger = Logger.getLogger("mailLogger");

    public void sendSimpleMail(String toUser, String subject, String content) throws OwnException {
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        /*
         * 可用的属性： mail.store.protocol / mail.transport.protocol / mail.host /
         * mail.user / mail.from
         */
        //String toUsers[] = {"zsq835963601com@163.com", "2465572192@qq.com"};

        String toUsers = toUser;
        String [] userList = toUsers.split(",");

        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.host", "smtp.mxhichina.com");
        // 发件人的账号
        props.put("mail.user", "835963601@qq.com");
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", "z@hu6633");


        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        mailSession.setDebug(true);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress form = null;
        try {

            form = new InternetAddress(props.getProperty("mail.user"), MimeUtility.encodeText("835963601@qq.com"));
            message.setFrom(form);

            if (userList.length > 1) {
                //设置多个收件人
                //String toList = getMailList(userList);
                InternetAddress[] toUserList = new InternetAddress().parse(toUsers);
                message.setRecipients(Message.RecipientType.TO, toUserList);
            } else {
                // 设置收件人
                InternetAddress to = new InternetAddress(toUsers);
                message.setRecipient(Message.RecipientType.TO, to);
            }

            /*// 设置抄送
            InternetAddress cc = new InternetAddress("luo_aaaaa@yeah.net");
            message.setRecipient(Message.RecipientType.CC, cc);

            // 设置密送，其他的收件人不能看到密送的邮件地址
            InternetAddress bcc = new InternetAddress("aaaaa@163.com");
            message.setRecipient(Message.RecipientType.CC, bcc);*/

            // 设置邮件标题
            message.setSubject(subject);

            // 设置邮件的内容体
            message.setContent(content, "text/html;charset=UTF-8");

            // 发送邮件
            Transport.send(message);
        } catch (Exception e) {
            logger.error(e);
            throw new OwnException(e.getMessage());
        }
    }

    private String getMailList(String[] mailArray) {
        StringBuffer toList = new StringBuffer();
        int length = mailArray.length;
        if (mailArray != null && length < 2) {
            toList.append(mailArray[0]);
        } else {
            for (int i = 0; i < length; i++) {
                toList.append(mailArray[i]);
                if (i != (length - 1)) {
                    toList.append(",");
                }

            }
        }
        return toList.toString();
    }
}
