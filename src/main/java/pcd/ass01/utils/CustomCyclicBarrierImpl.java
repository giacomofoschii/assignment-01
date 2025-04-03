package pcd.ass01.utils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomCyclicBarrierImpl implements CustomCyclicBarrier {

    private final int waitingThreadNum;
    private int arrivedThreadNum;

    public CustomCyclicBarrierImpl(int waitingThreadNum) {
        this.waitingThreadNum = waitingThreadNum;
        this.arrivedThreadNum = 0;
    }

    @Override
    public synchronized void await() throws InterruptedException, BrokenBarrierException {
        try {
            arrivedThreadNum++;

            if (arrivedThreadNum < waitingThreadNum) {
                wait();
            } else {
                arrivedThreadNum = 0;
                notifyAll();
            }
        } catch (InterruptedException ignored) {}
    }

    @Override
    public synchronized void reset() {
        arrivedThreadNum = 0;
        notifyAll();
    }
}
