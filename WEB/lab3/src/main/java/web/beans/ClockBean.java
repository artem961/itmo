package web.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.Data;
import lombok.extern.java.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Named
@ApplicationScoped
@Data
@Log
public class ClockBean {
    private String currentTime;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    {
        updateCurrentTime();
    }

    public void updateCurrentTime() {
        currentTime = formatter.format(LocalDateTime.now()).toString();
    }
}
