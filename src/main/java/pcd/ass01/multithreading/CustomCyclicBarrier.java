package pcd.ass01.multithreading;

import java.util.concurrent.BrokenBarrierException;

public class CustomCyclicBarrier {


    private final int waitingThreadNum;
    private int arrivedThreadNum;
    private boolean broken = false;

    public CustomCyclicBarrier(int waitingThreadNum) {
        this.waitingThreadNum = waitingThreadNum;
        this.arrivedThreadNum = 0;
    }

    public synchronized void await() throws InterruptedException, BrokenBarrierException {
        if (broken) {
            throw new BrokenBarrierException();
        }

        arrivedThreadNum++;

        if (arrivedThreadNum < waitingThreadNum) {
            try {
                wait();
            } catch (InterruptedException e) {
                broken = true;
                notifyAll();
                throw e;
            }
            if (broken) {
                throw new BrokenBarrierException();
            }
        } else {
            arrivedThreadNum = 0;
            notifyAll();
        }
    }
}
