package lab4.backend.utils;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClickInterval implements ClickIntervalMBean {
    private long totalIntervalsSum = 0;
    private int intervalsCount = 0;
    private Long lastClickTime = null;

    public synchronized void recordClick() {
        long currentTime = System.currentTimeMillis();

        if (lastClickTime != null) {
            long currentInterval = currentTime - lastClickTime;
            totalIntervalsSum += currentInterval;
            intervalsCount++;
        }

        lastClickTime = currentTime;
    }

    @Override
    public long getAverageInterval() {
        if (intervalsCount == 0) return 0;
        return totalIntervalsSum / intervalsCount;
    }
}
