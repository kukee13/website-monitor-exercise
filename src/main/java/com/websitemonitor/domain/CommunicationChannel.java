package com.websitemonitor.domain;

public abstract class CommunicationChannel {
    public abstract void send(Notification notification);
}
