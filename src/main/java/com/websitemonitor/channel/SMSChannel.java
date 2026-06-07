package com.websitemonitor.channel;

import com.websitemonitor.domain.CommunicationChannel;
import com.websitemonitor.domain.Notification;

public class SMSChannel extends CommunicationChannel {

    private final String gateway;

    public SMSChannel(String gateway) {
        this.gateway = gateway;
    }

    @Override
    public void send(Notification notification) {
        System.out.println(
            "[SMS via " + gateway + "] to " + notification.getRecipient()
            + " :: " + notification.getContent()
        );
    }

    public String getGateway() {
        return gateway;
    }
}
