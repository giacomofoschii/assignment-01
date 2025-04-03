package pcd.ass01.virtualthread;

import java.util.concurrent.BrokenBarrierException;

public interface CustomCyclicBarrier {
    void await() throws InterruptedException, BrokenBarrierException;

    void reset();
}
