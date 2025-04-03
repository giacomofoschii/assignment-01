package pcd.ass01.multithreading;

public class Administrator {
    private final int numThreads;
    private int waitingThreads;

    public Administrator(int numThreads) {
        this.numThreads = numThreads;
        this.waitingThreads = 0;
    }

    public synchronized void threadDone() {
        waitingThreads++;
        if (waitingThreads == numThreads) {
            notifyAll();
        }
    }

    public synchronized void waitThreads() {
        while (waitingThreads < numThreads) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void signalDone() {
        waitingThreads = 0;
        notifyAll();
    }
}