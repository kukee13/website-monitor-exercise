package com.websitemonitor.channel;

import com.websitemonitor.domain.CommunicationChannel;
import com.websitemonitor.domain.Notification;

public class EmailChannel extends CommunicationChannel {

    private final String smtpServer;

    public EmailChannel(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    @Override
    public void send(Notification notification) {
        System.out.println("[EMAIL via " + smtpServer + "] to " + notification.getRecipient() + " :: " + notification.getContent());
    }

    public String getSmtpServer() {
        return smtpServer;
    }
}
