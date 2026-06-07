package com.websitemonitor.system;

import com.websitemonitor.domain.CommunicationChannel;
import com.websitemonitor.domain.Subscription;
import com.websitemonitor.domain.User;
import com.websitemonitor.domain.Website;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Façade controller for the website monitor. Receives all system events
 * from the outside world (register, subscribe, modify, cancel, periodic
 * check) and delegates the actual work to the domain classes that own
 * the relevant data. By the Controller principle this class holds no
 * domain logic itself; it just holds the top-level registries of users
 * and websites and forwards calls.
 */
public class WebsiteMonitorSystem {

    private final Map<String, User> users;
    private final Map<String, Website> websites;

    public WebsiteMonitorSystem() {
        this.users = new HashMap<>();
        this.websites = new HashMap<>();
    }

    // ---- registration ----

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

    // ---- subscription lifecycle (thin delegation to User / Subscription) ----

    public Subscription subscribe(User user, Website website, Duration frequency, CommunicationChannel channel) {
        return user.subscribeTo(website, frequency, channel);
    }

    public void modifySubscription(Subscription sub, Duration newFrequency, CommunicationChannel newChannel) {
        sub.modify(newFrequency, newChannel);
    }

    public void cancelSubscription(Subscription sub) {
        sub.cancel();
    }

    // ---- periodic check ----

    /**
     * One full check cycle: ask every website whether it has changed; for
     * each one that has, find every due subscription pointing at it and
     * have that subscription notify its user. The system controller does
     * not check the URL itself — Website is the Information Expert for
     * that — and it does not build the Notification either — Subscription
     * is the Creator for that.
     */
    public void runPeriodicCheck() {
        for (Website website : websites.values()) {
            if (website.checkForUpdate()) {
                for (Subscription sub : subscriptionsFor(website)) {
                    if (sub.isDue()) sub.notifyOfUpdate();
                }
            }
        }
    }

    private List<Subscription> subscriptionsFor(Website website) {
        List<Subscription> result = new ArrayList<>();
        for (User user : users.values()) {
            for (Subscription sub : user.getSubscriptions()) {
                if (sub.getWebsite().equals(website)) result.add(sub);
            }
        }
        return result;
    }

    public int userCount()    { return users.size(); }
    public int websiteCount() { return websites.size(); }
}
