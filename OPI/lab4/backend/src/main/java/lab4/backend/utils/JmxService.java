package lab4.backend.utils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@Singleton
@Startup // Запускается автоматически при деплое
public class JmxService {

    @Inject
    private HitsCounter hitsCounter; // Внедряем тот же объект, что юзает API

    @Inject
    private ClickInterval clickInterval;

    private ObjectName counterName;
    private ObjectName intervalName;

    @PostConstruct
    public void register() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            counterName = new ObjectName("PointsManagement:type=HitsCounter");
            intervalName = new ObjectName("PointsManagement:type=ClickInterval");

            mbs.registerMBean(hitsCounter, counterName);
            mbs.registerMBean(clickInterval, intervalName);
            System.out.println("--- JMX MBeans registered successfully ---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void unregister() {
        try {
            ManagementFactory.getPlatformMBeanServer().unregisterMBean(counterName);
            ManagementFactory.getPlatformMBeanServer().unregisterMBean(intervalName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}