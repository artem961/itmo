package web.beans;

import lombok.Data;
import lombok.extern.java.Log;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import java.time.LocalDateTime;

@ManagedBean
@ViewScoped
@Data
public class ClockBean {
    private LocalDateTime currentTime;

    {
        currentTime = LocalDateTime.now();
    }

    public void updateCurrentTime() {
        currentTime = LocalDateTime.now();
    }
}
