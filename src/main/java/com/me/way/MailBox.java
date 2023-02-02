package com.me.way;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailBox {
    public static String setMaile(String toEmail,String code){
        //1_创建Java程序与163邮件服务器的连接对象
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.qq.com");//设置发送方邮箱服务器
        props.put("mail.smtp.auth", "true");//设置是否需要身份证
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                //发送方服务器账号设置
                //需要在163官方邮箱服务器，开启设置——>POP3/SMTP/IMAP服务能让其在本地客户端上收发邮件
                //QQ邮箱——>设置——>账号管理——>开启
                //开启后，需要验证密保，发送相关内容后会弹出密码
                return new PasswordAuthentication("744021881@qq.com", "meoogxvbhhfmbchh");
            }
        };
        Session session = Session.getInstance(props, auth);
        //2_创建一封邮件
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("744021881@qq.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("验证码");
            message.setContent("你的验证码为"+code+",5分钟内有效", "text/html;charset=UTF-8");
            //3_发送邮件
            Transport.send(message);
            return "true";
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return "false";
    }

    public static void main(String[] args) {
        System.out.println(setMaile("3020422116@qq.com", "111111"));
    }
}
