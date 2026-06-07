package com.websitemonitor.domain;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

public class Website {

    private final String websiteId;
    private final URL url;
    private String lastContent;
    private LocalDateTime lastChecked;

    public Website(URL url) {
        this.websiteId = UUID.randomUUID().toString();
        this.url = url;
        this.lastContent = "";
        this.lastChecked = null;
    }

    public boolean checkForUpdate() {
        String current = fetchCurrentContent();
        boolean firstCheck = (lastContent == null || lastContent.isEmpty());
        boolean changed = !firstCheck && !current.equals(lastContent);
        this.lastContent = current;
        this.lastChecked = LocalDateTime.now();
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
