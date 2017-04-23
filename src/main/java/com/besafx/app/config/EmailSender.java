package com.besafx.app.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.concurrent.Future;

@Service
public class EmailSender {

    private static final String SMTP_HOST_NAME = "smtpout.secureserver.net";

    private static final int SMTP_HOST_PORT = 465;

    private static String SMTP_AUTH_USER = "admin@ararhni.com";

    private static String SMTP_AUTH_PWD = "besa2009";

    private final Logger log = LoggerFactory.getLogger(EmailSender.class);

    private Session mailSession;

    private Transport transport;

    private MimeMessage message;

    @PostConstruct
    public void init() {
        log.info("Preparing email service...");
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", SMTP_HOST_NAME);
        props.put("mail.smtps.auth", "true");
        mailSession = Session.getDefaultInstance(props);
        mailSession.setDebug(true);
        log.info("Preparing email service successfully");
    }

    @Async("threadPoolEmailSender")
    public void send(String title, String content, List<String> toEmailList) {
        try {
            log.info("Sleeping for 10 seconds");
            Thread.sleep(10000);
            log.info("Trying sending email to this destinations: " + toEmailList);
            transport = mailSession.getTransport();
            message = new MimeMessage(mailSession);
            message.setSubject(title, "UTF-8");
            message.setText(content, "UTF-8", "html");
            Address[] from = InternetAddress.parse("admin@ararhni.com");
            message.addFrom(from);
            toEmailList.stream().forEach(email -> {
                try {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                } catch (Exception ex) {
                    log.info(ex.getMessage());
                }
            });
            transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            log.info("Sending email successfully to this destinations: " + toEmailList);
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
    }

    @Async("threadPoolEmailSender")
    public void send(String title, String content, List<String> toEmailList, List<FileSystemResource> files) {
        try {
            log.info("Sleeping for 10 seconds");
            Thread.sleep(10000);
            log.info("Trying sending email to this destinations: " + toEmailList);
            transport = mailSession.getTransport();
            message = new MimeMessage(mailSession);
            Address[] from = InternetAddress.parse("admin@ararhni.com");
            message.addFrom(from);
            message.setSubject(title, "UTF-8");
            message.setText(content, "UTF-8", "html");
            toEmailList.stream().forEach(email -> {
                try {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                } catch (Exception ex) {
                    log.info(ex.getMessage());
                }
            });
            BodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            ListIterator<FileSystemResource> fileSystemResourceListIterator = files.listIterator();
            while (fileSystemResourceListIterator.hasNext()) {
                FileSystemResource fileSystemResource = fileSystemResourceListIterator.next();
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(fileSystemResource.getFile());
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fileSystemResource.getFilename());
                multipart.addBodyPart(messageBodyPart);
            }
            message.setContent(multipart);
            transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            log.info("Sending email successfully to this destinations: " + toEmailList);
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
    }

    @Async("threadPoolEmailSender")
    public void send(String title, String content, String email) {
        try {
            log.info("Sleeping for 10 seconds");
            Thread.sleep(10000);
            log.info("Trying sending email to this destinations: " + email);
            transport = mailSession.getTransport();
            message = new MimeMessage(mailSession);
            message.setSubject(title, "UTF-8");
            message.setText(content, "UTF-8", "html");
            Address[] from = InternetAddress.parse("admin@ararhni.com");
            message.addFrom(from);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            log.info("Sending email successfully to this destinations: " + email);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @Async("threadPoolEmailSender")
    public Future<Boolean> send(String title, String content, String email, List<FileSystemResource> files) {
        try {
            log.info("Sleeping for 10 seconds");
            Thread.sleep(10000);
            log.info("Trying sending email to this destinations: " + email);
            transport = mailSession.getTransport();
            message = new MimeMessage(mailSession);
            Address[] from = InternetAddress.parse("admin@ararhni.com");
            message.addFrom(from);
            message.setSubject(title, "UTF-8");
            message.setText(content, "UTF-8", "html");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            BodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            ListIterator<FileSystemResource> fileSystemResourceListIterator = files.listIterator();
            while (fileSystemResourceListIterator.hasNext()) {
                FileSystemResource fileSystemResource = fileSystemResourceListIterator.next();
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(fileSystemResource.getFile());
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fileSystemResource.getFilename());
                multipart.addBodyPart(messageBodyPart);
            }
            message.setContent(multipart);
            transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            log.info("Sending email successfully to this destinations: " + email);
            return new AsyncResult<>(true);
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return new AsyncResult<>(false);
        }
    }
}
