package com.jayqqaa12.jbase.util;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

/**
 * JDiy邮件发送工具类.
 * <br/>此类依赖于javax.mail包. 将发送E-mail的代码进行了封装简化.
 *
 * @author 子秋(ziquee)  http://www.jdiy.org
 */
@SuppressWarnings("unused")
public final class Eml {
    private MimeMessage message;
    private Multipart mp = new MimeMultipart();
    private Session ssn;
    private String server;
    private String uid;
    private String pwd;

    /**
     * 构造一个E-mail发送对象的实例.
     * 通过此实例完成电子邮件的发送.
     * @param server 指定发件服务器主机地址.
     * @param eml 指定发件人电子邮件地址
     * @param uid 指定发件人的(邮件服务器)登录帐号. 通常此帐号与邮件地址相同(视具体发件服务器而定)
     * @param pwd 指定发件人的(邮件服务器)登录密码.即发件人邮箱的登录密码.
     * @throws MessagingException 当指定的信息有误，无法连接到发件服务器时，将抛出此异常.
     */
    public Eml(String server, String eml, String uid, String pwd) throws MessagingException {
        this.server = server;
        this.uid = uid;
        this.pwd = pwd;
        Properties props = new Properties();
        props.put("mail.smtp.host", server);
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", "smtp");
        Auth auth = new Auth(uid, pwd);
        ssn = Session.getInstance(props, auth);
        message = new MimeMessage(ssn);
        InternetAddress fromAddress = new InternetAddress(eml);
        message.setFrom(fromAddress);
    }

    /**
     * 设置邮件的标题.
     *
     * @param subject 邮件的标题字符串. 必须是纯文本的字符串,建议不要超过64个字符.
     * @return 当前的Eml对象
     * @throws javax.mail.MessagingException 发生错误时抛出此异常
     */
    public Eml setSubject(String subject) throws MessagingException {
        message.setSubject(subject);
        return this;
    }

    /**
     * 设置邮件的正文内容.
     *
     * @param html 邮件的正文内容. 标准的html格式.
     * @return 当前的Eml对象
     * @throws javax.mail.MessagingException 发生错误时抛出此异常
     */
    public Eml setBody(String html) throws MessagingException {
        MimeBodyPart mbp = new MimeBodyPart();
        mbp.setContent(html, "text/html; charset=utf-8");
        mp.addBodyPart(mbp);
        return this;
    }

    /**
     * 添加邮件接收者的E-mail地址.
     * @param to 邮件接收者的E-mail地址.
     * @return 当前的Eml对象本身.
     * @see #addCc(String)
     * @see #addBcc(String)
     * @throws javax.mail.MessagingException 发生错误时抛出此异常
     */
    public Eml addTo(String to) throws MessagingException {
        InternetAddress toAddress = new InternetAddress(to);
        message.addRecipient(Message.RecipientType.TO, toAddress);
        return this;
    }

    /**
     * 添加抄送. 即将邮件同时抄送给此收件人.
     * @param cc 邮件地址
     * @return 当前的Eml对象本身.
     * @see #addBcc(String)
     * @see #addTo(String)
     * @throws MessagingException 发生错误时抛出此异常
     */
    public Eml addCc(String cc) throws MessagingException {
        InternetAddress toAddress = new InternetAddress(cc);
        message.addRecipient(Message.RecipientType.CC, toAddress);
        return this;
    }

    /**
     * 添加暗送. 即将邮件同时暗送给此收件人.
     * @param bcc 邮件地址
     * @return 当前的Eml对象本身.
     * @throws MessagingException 发生错误时抛出此异常
     * @see #addCc(String)
     * @see #addTo(String)
     */
    public Eml addBcc(String bcc) throws MessagingException {
        InternetAddress toAddress = new InternetAddress(bcc);
        message.addRecipient(Message.RecipientType.BCC, toAddress);
        return this;
    }

    /**
     * 为邮件添加附件.
     * @param filePath 附件的绝对路径.
     * @throws MessagingException 发生错误时抛出此异常
     * @see #addFile(java.io.File)
     */
    public Eml addFile(String filePath) throws MessagingException {
        MimeBodyPart mbp = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(filePath);
        mbp.setDataHandler(new DataHandler(fds));
        mbp.setFileName(fds.getName());
        mp.addBodyPart(mbp);
        return this;
    }

    /**
     * 为邮件添加附件.
     * @param file File文件对象.
     * @throws MessagingException 发生错误时抛出此异常
     * @see #addFile(String)
     */
    public Eml addFile(File file) throws MessagingException {
        return addFile(file.getAbsolutePath());
    }

    /**
     * 开始发送邮件.
     * 在调用此方法之前,请确保己经设置好收件人的地址,邮件主题，邮件正文等内容.
     * @throws javax.mail.MessagingException 当发送邮件时有错误时抛出此异常
     */
    @SuppressWarnings({"ConstantConditions"})
    public void send() throws MessagingException {
        message.setContent(mp);
        Transport transport = ssn.getTransport("smtp");
        transport.connect(server, uid, pwd);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    class Auth extends Authenticator {
        String username = null;
        String password = null;

        public Auth(String user, String pass) {
            username = user;
            password = pass;
        }
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }
}
