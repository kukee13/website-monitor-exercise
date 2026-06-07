package com.websitemonitor.system;

import com.websitemonitor.domain.CommunicationChannel;
import com.websitemonitor.domain.Subscription;
import com.websitemonitor.domain.User;
import com.websitemonitor.domain.Website;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class WebsiteMonitorSystem {

    private final Map<String, User> users;
    private final Map<String, Website> websites;

    public WebsiteMonitorSystem() {
        this.users = new HashMap<>();
        this.websites = new HashMap<>();
    }

    // Registration and lifecycle methods unchanged
    public User registerUser(String name, String email) {
        User user = new User(name, email);
        users.put(user.getUserId(), user);
        return user;
    }

    public Website addWebsite(URL url) {
        Website website = new Website(url);
        websites.put(website.getWebsiteId(), website);
        return website;
    }

    public Subscription subscribe(User user, Website website, Duration frequency, CommunicationChannel channel) {
        return user.subscribeTo(website, frequency, channel);
    }

    public void modifySubscription(Subscription sub, Duration newFrequency, CommunicationChannel newChannel) {
        sub.modify(newFrequency, newChannel);
    }

    public void cancelSubscription(Subscription sub) {
        sub.cancel();
    }

    // periodic check

    public void runPeriodicCheck() {
        for (Website website : websites.values()) {
            website.checkForUpdate();
        }
    }

    public int userCount()    { return users.size(); }
    public int websiteCount() { return websites.size(); }
}