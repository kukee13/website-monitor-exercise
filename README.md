# Website Monitor

Java implementation of the *Website Monitor* system from
*Software Engineering Design — Exercise 3 (Frankfurt UAS, 2026, Prof. Dr. Lokaiczyk)*.
Built for **Exercise 4** of the same course.

The system lets a user subscribe to a website, periodically checks subscribed
websites for content changes, and dispatches notifications through a chosen
channel (email / SMS / push) when an update is detected. Notification delivery
is mocked — the channels just print to standard out so the demo runs without
any infrastructure.

## Project layout

```
src/main/java/com/websitemonitor/
├── Main.java                       — end-to-end demo
├── system/
│   └── WebsiteMonitorSystem.java   — façade controller
├── domain/
│   ├── User.java                   — user (composes subscriptions)
│   ├── Subscription.java           — links user / website / channel
│   ├── Website.java                — owns URL + last content, detects updates
│   ├── Notification.java           — value object
│   └── CommunicationChannel.java   — abstract delivery
└── channel/
    ├── EmailChannel.java           — mock SMTP
    ├── SMSChannel.java             — mock SMS gateway
    └── PushChannel.java            — mock push provider
```

Package dependencies are one-way:
`system → domain` and `channel → domain`. The `domain` package depends on
nothing else, which keeps the core entities reusable.

## Build & run

Plain `javac` / `java` — no Maven/Gradle required.

```bash
# from the project root
mkdir -p out
javac -d out $(find src/main/java -name "*.java")
java -cp out com.websitemonitor.Main
```

Expected output (channel order may vary by JVM):

```
System: 2 users, 2 websites tracked.
Alice's subscriptions: 2

--- first periodic check (warm-up) ---

--- second periodic check ---
[EMAIL via smtp.example.com] to alice@example.com :: Website https://news.ycombinator.com has been updated.
[SMS via twilio-eu] to bob@example.com :: Website https://news.ycombinator.com has been updated.
[PUSH] to device alice@example.com :: Website https://www.frankfurt-university.de has been updated.

Cancelling Alice's HN subscription...
Alice's subscriptions after cancel: 1
```

The first cycle is a warm-up: when a website is checked for the first time the
system has nothing to compare against, so no notification fires. On the second
cycle the mocked `Website.fetchCurrentContent()` returns a different string,
the change is detected, and each due subscription's channel fires.

## Design notes

- The system follows the **Information Expert** principle: `Website` owns
  `checkForUpdate()` because it owns the URL and the previous content;
  `CommunicationChannel` owns `send()` because it owns the transport.
- The **Creator** principle is applied: `User` creates its `Subscription`s
  (User composes and records them); `Subscription` creates the `Notification`
  on an update (it holds all the initialising data).
- `WebsiteMonitorSystem` acts as a **façade Controller**: it receives every
  external system event (`registerUser`, `subscribe`, `runPeriodicCheck`, …)
  and delegates the actual work; it holds no domain logic of its own.

See the accompanying `Exercise_4.docx` (or `Exercise_4.pdf`) for the metrics
analysis, package-structure justification, and notes on coupling reduction.
