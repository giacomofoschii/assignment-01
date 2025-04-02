package pcd.ass01;

public abstract class BoidsSimulator {
    protected final int numThreads = Runtime.getRuntime().availableProcessors();
    protected volatile boolean running = true;
    protected volatile boolean paused = false;

    public abstract void runSimulation();

    public synchronized void pauseSimulation() {
        this.paused = true;
    };

    public synchronized void resumeSimulation() {
        this.paused = false;
        notifyAll();
    };

    public abstract void newSimulation();

    public abstract void stopSimulation();

    public abstract void updateView();

    public boolean isPaused() {
        return this.paused;
    };
}