package com.websitemonitor;

import com.websitemonitor.channel.EmailChannel;
import com.websitemonitor.channel.PushChannel;
import com.websitemonitor.channel.SMSChannel;
import com.websitemonitor.domain.Subscription;
import com.websitemonitor.domain.User;
import com.websitemonitor.domain.Website;
import com.websitemonitor.system.WebsiteMonitorSystem;

import java.net.URI;
import java.time.Duration;

public class Main {

    public static void main(String[] args) throws Exception {
        WebsiteMonitorSystem system = new WebsiteMonitorSystem();

        //register users
        User alice = system.registerUser("Alice", "alice@example.com");
        User bob   = system.registerUser("Bob",   "bob@example.com");

        //add monitored websites
        Website hn  = system.addWebsite(URI.create("https://news.ycombinator.com").toURL());
        Website fra = system.addWebsite(URI.create("https://www.frankfurt-university.de").toURL());

        //set up channels
        EmailChannel email = new EmailChannel("smtp.example.com");
        SMSChannel   sms   = new SMSChannel("twilio-eu");
        PushChannel  push  = new PushChannel("test-api-key");

        //subscriptions
        Subscription s1 = system.subscribe(alice, hn,  Duration.ofMinutes(0), email);
        Subscription s2 = system.subscribe(alice, fra, Duration.ofMinutes(0), push);
        Subscription s3 = system.subscribe(bob,   hn,  Duration.ofMinutes(0), sms);

        System.out.println("System: " + system.userCount() + " users, "
                + system.websiteCount() + " websites tracked.");
        System.out.println("Alice's subscriptions: " + alice.getSubscriptions().size());
        System.out.println();

        //first cycle: every website is being seen for the first time,
        //so no notification fires (we have nothing to compare against)
        System.out.println("--- first periodic check (warm-up) ---");
        system.runPeriodicCheck();

        //second cycle: now lastContent is populated, the mock will produce
        //a different content string, so updates are detected and channels fire. ---
        System.out.println();
        System.out.println("--- second periodic check ---");
        system.runPeriodicCheck();

        //demonstrate cancel
        System.out.println();
        System.out.println("Cancelling Alice's HN subscription...");
        system.cancelSubscription(s1);
        System.out.println("Alice's subscriptions after cancel: " + alice.getSubscriptions().size());
    }
}
