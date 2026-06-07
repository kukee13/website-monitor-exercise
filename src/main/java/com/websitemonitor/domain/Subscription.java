package com.websitemonitor.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class Subscription {

    private final String subId;
    private final User user;
    private final Website website;
    private Duration frequency;
    private CommunicationChannel channel;
    private final LocalDateTime createdAt;
    private LocalDateTime lastNotified;

    public Subscription(User user, Website website, Duration frequency, CommunicationChannel channel) {
        this.subId = UUID.randomUUID().toString();
        this.user = user;
        this.website = website;
        this.frequency = frequency;
        this.channel = channel;
        this.createdAt = LocalDateTime.now();
        this.lastNotified = null;

        // Attach this observer to the subject upon creation
        this.website.attach(this);
    }

    //Observer Interface Method
    public void update(Website updatedWebsite) {
        if (isDue()) {
            notifyOfUpdate();
        }
    }

    public boolean isDue() {
        if (lastNotified == null) return true;
        return Duration.between(lastNotified, LocalDateTime.now()).compareTo(frequency) >= 0;
    }

    public void modify(Duration newFrequency, CommunicationChannel newChannel) {
        this.frequency = newFrequency;
        this.channel = newChannel;
    }

    public void cancel() {
        user.removeSubscription(this);
        // Detach from the subject when cancelled
        website.detach(this);
    }

    public void notifyOfUpdate() {
        Notification notification = new Notification(
                "Website " + website.getUrl() + " has been updated.",
                user.getEmail()
        );
        channel.send(notification);
        this.lastNotified = LocalDateTime.now();
    }

    // Getters and toString remain unchanged...
    public String getSubId()             { return subId; }
    public User getUser()                { return user; }
    public Website getWebsite()          { return website; }
    public Duration getFrequency()       { return frequency; }
    public CommunicationChannel getChannel() { return channel; }
    public LocalDateTime getLastNotified()   { return lastNotified; }

    @Override
    public String toString() {
        return "Subscription[" + user.getName() + " -> " + website.getUrl() + "]";
    }
}