package pcd.ass01.utils;

import java.util.concurrent.BrokenBarrierException;

public interface CustomCyclicBarrier {
    void await() throws InterruptedException, BrokenBarrierException;

    void reset();
}
