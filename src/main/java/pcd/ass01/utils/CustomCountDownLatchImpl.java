package pcd.ass01.utils;

public class CustomCountDownLatchImpl implements CustomCountDownLatch {
    private int waitingTasks;

    public CustomCountDownLatchImpl(int waitingTasks ) {
        if (waitingTasks < 0) throw new IllegalArgumentException("Number of waiting tasks cannot be negative");
        this.waitingTasks  = waitingTasks ;
    }

    @Override
    public synchronized void await() {
        while (waitingTasks > 0) {
            try{
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public synchronized void countDown() {
        if(waitingTasks > 0) {
            waitingTasks--;
            if (waitingTasks == 0) {
                notifyAll();
            }
        }
    }
}
