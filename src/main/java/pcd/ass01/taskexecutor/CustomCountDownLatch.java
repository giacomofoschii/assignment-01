package pcd.ass01.taskexecutor;

public interface CustomCountDownLatch {
    void await() throws InterruptedException;

    void countDown();
}
