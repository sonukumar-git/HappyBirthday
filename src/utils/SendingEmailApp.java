package utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendingEmailApp {
    final String sender="sender_email";
    final String sPassword="password";
    final String emailSMTPServer = "smtp.gmail.com";
    final String emailServerPort = "465";



    Properties props=new Properties();

    public SendingEmailApp(){
        setConfiguration();
    }


    public void setConfiguration(){
        //System.out.println("Configuring mail service..");
        props.put("mail.smtp.host",emailSMTPServer);
        props.put("mail.smtp.port",emailServerPort);
        props.put("mail.smtp.user",sender);
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.debug","true");
        props.put("mail.smtp.socketFactory.port",emailServerPort);
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback","false");


    }


    public void sendMail(String receiver,
                         String subject,
                         String body,
                         String content,
                         String contentType){
        try{

            Session session= Session.getDefaultInstance(props,null);
           // session.setDebug(true);

            MimeMessage message=new MimeMessage(session);
            message.setText(body);
            message.setSubject(subject);
            message.setFrom(new InternetAddress(sender));
            message.setContent(content,contentType);
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(receiver.toLowerCase()));

            message.saveChanges();

            Transport transport = session.getTransport("smtp");
            if(transport!=null) {
                transport.connect(emailSMTPServer, sender, sPassword);
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
                System.out.println("* Mail sent successfully *");
            }
            else{
                System.out.println("Connection Failed");
            }
        }catch(AuthenticationFailedException e) {
            System.out.println("AuthenticationFailedException - for authentication failures");
            e.printStackTrace();
        }
        catch (MessagingException e) {
            System.out.println("for other failures");
            e.printStackTrace();
        }

    }

 }
