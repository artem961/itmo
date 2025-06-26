package lab6.ui.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
public class UserSettings {
    private Locale locale;

    public UserSettings(){
        this.locale = new Locale("en");
    }
}
