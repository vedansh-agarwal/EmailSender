package org.example;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the absolute path of the .txt file with email values below");
        String txtFileLoc = sc.next();
        sc.close();
        String from = "", password = "", to = "", subject = "", body = "";
        try {
            File myObj = new File(txtFileLoc);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                int divider = data.indexOf('=');
                String key = data.substring(0, divider);
                String value = data.substring(divider+1);
                if(key.equalsIgnoreCase("from"))
                    from = value;
                else if(key.equalsIgnoreCase("password"))
                    password = value;
                else if(key.equalsIgnoreCase("to"))
                    to = value;
                else if(key.equalsIgnoreCase("subject"))
                    subject = value;
                else if(key.equalsIgnoreCase("body"))
                    body = value;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("invalid input file location received. please input the absolute path of the file");
            e.printStackTrace();
            return;
        }

        if(from.isEmpty() || password.isEmpty() || to.isEmpty() || subject.isEmpty() || body.isEmpty()) {
            System.out.println("insufficient data in input file");
            return;
        }

        System.out.println("Preparing to send message...");
        sendEmail(from, password, to, subject, body);
    }

    private static void sendEmail(final String from, final String password, String to, String subject, String body) {

        String host="smtp.gmail.com";

        Properties properties = System.getProperties();
        System.out.println("PROPERTIES "+properties);

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");

        Session session=Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        session.setDebug(true);

        MimeMessage m = new MimeMessage(session);

        try {
            m.setFrom(from);

            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            m.setSubject(subject);

            m.setText(body);

            Transport.send(m);

            System.out.println("Email Sent Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
