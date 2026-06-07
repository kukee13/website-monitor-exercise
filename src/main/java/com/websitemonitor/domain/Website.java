package com.websitemonitor.domain;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Website {

    private final String websiteId;
    private final URL url;
    private String lastContent;
    private LocalDateTime lastChecked;

    // Observer list
    private final List<Subscription> observers;

    public Website(URL url) {
        this.websiteId = UUID.randomUUID().toString();
        this.url = url;
        this.lastContent = "";
        this.lastChecked = null;
        this.observers = new ArrayList<>();
    }

    //Subject Interface Methods

    public void attach(Subscription observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void detach(Subscription observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Subscription observer : observers) {
            observer.update(this);
        }
    }

    // ---------------------------------

    public boolean checkForUpdate() {
        String current = fetchCurrentContent();
        boolean firstCheck = (lastContent == null || lastContent.isEmpty());
        boolean changed = !firstCheck && !current.equals(lastContent);

        this.lastContent = current;
        this.lastChecked = LocalDateTime.now();

        if (changed) {
            notifyObservers(); // Automatically notify when a change occurs
        }

        return changed;
    }

    public String fetchCurrentContent() {
        return "mock-content-" + System.nanoTime();
    }

    public URL getUrl()                      { return url; }
    public String getWebsiteId()             { return websiteId; }
    public LocalDateTime getLastChecked()    { return lastChecked; }

    @Override
    public String toString() {
        return "Website[" + url + "]";
    }
}