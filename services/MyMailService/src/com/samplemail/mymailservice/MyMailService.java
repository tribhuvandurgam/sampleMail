/*Copyright (c) 2016-2017 azym.com All Rights Reserved.
 This software is the confidential and proprietary information of azym.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with azym.com*/
package com.samplemail.mymailservice;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;


import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//import com.samplemail.mymailservice.model.*;

/**
 * This is a singleton class with all its public methods exposed as REST APIs via generated controller class.
 * To avoid exposing an API for a particular public method, annotate it with @HideFromClient.
 *
 * Method names will play a major role in defining the Http Method for the generated APIs. For example, a method name
 * that starts with delete/remove, will make the API exposed as Http Method "DELETE".
 *
 * Method Parameters of type primitives (including java.lang.String) will be exposed as Query Parameters &
 * Complex Types/Objects will become part of the Request body in the generated API.
 */
@ExposeToClient
public class MyMailService {

    private static final Logger logger = LoggerFactory.getLogger(MyMailService.class);

    @Autowired
    private SecurityService securityService;

    /**
     * This is sample java operation that accepts an input from the caller and responds with "Hello".
     *
     * SecurityService that is Autowired will provide access to the security context of the caller. It has methods like isAuthenticated(),
     * getUserName() and getUserId() etc which returns the information based on the caller context.
     *
     * Methods in this class can declare HttpServletRequest, HttpServletResponse as input parameters to access the
     * caller's request/response objects respectively. These parameters will be injected when request is made (during API invocation).
     */
    public String sendEmail(String toEmailAddress,  String emailSubject, String emailMessage) {
        try {
            // Use javamail api, set parameters from registration.properties file
            // set the session properties
            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");
            Session session = Session.getDefaultInstance(props, null);
 
            // Create email message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("praveen.noonsawath@wavemaker.com"));
            String[] recipientList = toEmailAddress.split(",");
            InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
            int counter = 0;
            for (String recipient: recipientList) {
                recipientAddress[counter] = new InternetAddress(recipient.trim());
                counter++;
            }
            message.setRecipients(Message.RecipientType.TO, recipientAddress);
            message.setSubject(emailSubject);
            // message.setContent(emailMessage, "text/html");
            message.setText(emailMessage,"UTF-8");
            // Send smtp message
            Transport tr = session.getTransport("smtp");
            tr.connect("smtp.gmail.com", 587, "praveen.noonsawath@wavemaker.com", "Pramati@123");
            message.saveChanges();
            tr.sendMessage(message, message.getAllRecipients());
            tr.close();
 
            return "Mail sent successfully.";
 
        } catch (MessagingException e) {
            return "Error in method sendEmailNotification: " + e;
        }}

}
