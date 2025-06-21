package lab6.network.server;

public abstract class ServerModule implements Runnable{
    protected volatile boolean isRunning = true;

    public void shutdown(){
        this.isRunning = false;
        Thread.currentThread().interrupt();
    }
}
