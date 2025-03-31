package pcd.ass01.taskexecutor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomCountDownLatch {
    private int waitingTasks ;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public CustomCountDownLatch(int waitingTasks ) {
        if (waitingTasks < 0) throw new IllegalArgumentException("Number of waiting tasks cannot be negative");
        this.waitingTasks  = waitingTasks ;
    }

    public void await() throws InterruptedException {
        lock.lock();
        try {
            while (waitingTasks > 0) {
                try{
                    condition.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void countDown() {
        lock.lock();
        try{
            if(waitingTasks > 0) {
                waitingTasks--;
                if (waitingTasks == 0) {
                    condition.signalAll();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        lock.lock();
        try {
            return waitingTasks;
        } finally {
            lock.unlock();
        }
    }


}
