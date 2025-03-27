package pcd.ass01;

public class Administrator {
    private final int numThreads;
    private int waitingThreads;

    public Administrator(int numThreads) {
        this.numThreads = numThreads;
        this.waitingThreads = 0;
    }

    public synchronized void threadDone() {
        try {
            waitingThreads++;
            /*if (waitingThreads < numThreads) {
                wait();
            } else {
                notifyAll();
            }*/

            if (waitingThreads == numThreads) {
               notifyAll();
               }

            while (waitingThreads < numThreads) {
                wait();
            }

        } catch (InterruptedException e) {
        }
    }

    public synchronized void waitThreads() {
        try {
            while (waitingThreads < numThreads) {
                wait();
            }
        } catch (InterruptedException e) {
        }
    }

    public synchronized void signalDone() {
        waitingThreads = 0;
        notifyAll();
    }
}
