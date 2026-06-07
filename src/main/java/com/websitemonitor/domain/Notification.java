package com.websitemonitor.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notification {

    private final String notificationId;
    private final String content;
    private final String recipient;
    private final LocalDateTime timestamp;

    public Notification(String content, String recipient) {
        this.notificationId = UUID.randomUUID().toString();
        this.content = content;
        this.recipient = recipient;
        this.timestamp = LocalDateTime.now();
    }

    public String getNotificationId() { return notificationId; }
    public String getContent()        { return content; }
    public String getRecipient()      { return recipient; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Notification[" + notificationId.substring(0, 8) + " to " + recipient + "]";
    }
}
