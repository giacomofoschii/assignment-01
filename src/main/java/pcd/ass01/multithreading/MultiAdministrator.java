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
        waitingThreads++;
        if (waitingThreads == numThreads) {
            notifyAll();
        }
    }

    @Override
    public synchronized void waitThreads() {
        while (waitingThreads < numThreads) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public synchronized void signalDone() {
        waitingThreads = 0;
        notifyAll();
    }
}