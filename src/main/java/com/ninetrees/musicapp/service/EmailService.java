package com.ninetrees.musicapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Classname:EmailService
 * Description:
 */
public interface EmailService {

    void sendEmail(String to);
}
