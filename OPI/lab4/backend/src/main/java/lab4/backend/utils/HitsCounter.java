package lab4.backend.utils;

import jakarta.enterprise.context.ApplicationScoped;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

@ApplicationScoped
public class HitsCounter extends NotificationBroadcasterSupport implements HitsCounterMBean {
    private int total = 0;
    private int hits = 0;
    private int missCount = 0;
    private long notificationSequence = 1;

    public void addPoint(boolean isHit) {
        total++;
        if (isHit) {
            hits++;
            missCount = 0;
        } else {
            missCount++;
            if (missCount == 2) {
                Notification notification = new Notification(
                        "TwoMissesInARow",
                        "PointsCounterMBean",
                        notificationSequence++,
                        "Пользователь промахнулся дважды подряд!"
                );
                sendNotification(notification);
                missCount = 0;
            }
        }
    }

    @Override
    public int getTotalPoints() {
        return total;
    }

    @Override
    public int getHitPoints() {
        return hits;
    }
}