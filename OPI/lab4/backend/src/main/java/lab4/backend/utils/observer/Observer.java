package lab4.backend.utils.observer;

public interface Observer {
    void registerChangeListener(ChangeListener changeListener);
    void removeChangeListener(ChangeListener changeListener);
    void notifyListeners();
}
