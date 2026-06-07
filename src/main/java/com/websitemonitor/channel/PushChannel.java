package com.websitemonitor.channel;

import com.websitemonitor.domain.CommunicationChannel;
import com.websitemonitor.domain.Notification;

public class PushChannel extends CommunicationChannel {

    private final String apiKey;

    public PushChannel(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void send(Notification notification) {
        System.out.println(
            "[PUSH] to device " + notification.getRecipient()
            + " :: " + notification.getContent()
        );
    }

    public String getApiKey() { return apiKey; }
}
