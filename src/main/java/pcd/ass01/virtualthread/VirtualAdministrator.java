package pcd.ass01.virtualthread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class VirtualAdministrator {

    private int numThreads;
    private int waitingThreads;
    private final ReentrantLock lock;
    private final Condition condition;

    public VirtualAdministrator() {
        this.waitingThreads = 0;
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    public void setThreadNumber(int numThreads) {
        this.numThreads = numThreads;
    }

    public void threadDone() {
        lock.lock();
        try {
            waitingThreads++;
            if(waitingThreads == numThreads) {
                this.condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public void waitThreads() {
        lock.lock();
        try {
            while (waitingThreads < numThreads) {
                try {
                    this.condition.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void signalDone() {
        lock.lock();
        try {
            waitingThreads = 0;
            this.condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
