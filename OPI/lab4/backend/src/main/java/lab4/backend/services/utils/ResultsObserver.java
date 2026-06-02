package lab4.backend.services.utils;

import jakarta.ejb.Singleton;
import lab4.backend.utils.observer.ChangeListener;
import lab4.backend.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ResultsObserver implements Observer {
    private List<ChangeListener> listeners;

    {
        listeners = new ArrayList<>();
    }

    @Override
    public void registerChangeListener(ChangeListener changeListener) {
        listeners.add(changeListener);
    }

    @Override
    public void removeChangeListener(ChangeListener changeListener) {
        listeners.remove(changeListener);
    }

    @Override
    public void notifyListeners() {
        listeners.forEach(ChangeListener::onChange);
    }
}
