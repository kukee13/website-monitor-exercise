package com.websitemonitor.domain;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class User {

    private final String userId;
    private final String name;
    private final String email;
    private final List<Subscription> subscriptions;

    public User(String name, String email) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.subscriptions = new ArrayList<>();
    }


    public Subscription subscribeTo(Website website, Duration frequency, CommunicationChannel channel) {
        Subscription sub = new Subscription(this, website, frequency, channel);
        this.subscriptions.add(sub);
        return sub;
    }

    void removeSubscription(Subscription sub) {
        this.subscriptions.remove(sub);
    }

    public List<Subscription> getSubscriptions() {
        return Collections.unmodifiableList(subscriptions);
    }

    public String getUserId() { return userId; }
    public String getName()   { return name; }
    public String getEmail()  { return email; }

    @Override
    public String toString() {
        return "User[" + name + " <" + email + ">]";
    }
}
