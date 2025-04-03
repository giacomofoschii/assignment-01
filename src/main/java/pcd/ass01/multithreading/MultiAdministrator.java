package pcd.ass01.multithreading;

import pcd.ass01.utils.Administrator;

public class MultiAdministrator implements Administrator {
    private final int numThreads;
    private int waitingThreads;

    public MultiAdministrator(int numThreads) {
        this.numThreads = numThreads;
        this.waitingThreads = 0;
    }

    @Override
    public synchronized void threadDone() {
        try {
            waitingThreads++;
            if (waitingThreads == numThreads) {
                notifyAll();
            }

            while(waitingThreads != 0){
                    wait();
            }
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public synchronized void waitThreads() {
        try {
            while (waitingThreads < numThreads) {
                wait();
            }
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public synchronized void signalDone() {
        waitingThreads = 0;
        notifyAll();
    }
}