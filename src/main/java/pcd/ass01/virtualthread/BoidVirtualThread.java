package pcd.ass01.virtualthread;

import pcd.ass01.Boid;
import pcd.ass01.utils.CustomCyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoidVirtualThread extends Thread{
    private final Boid boid;
    private final CustomCyclicBarrier barrier;
    private final VirtualAdministrator admin;
    private final VirtualThreadController controller;
    private volatile boolean stopped;
    private final ReentrantLock lock;
    private final Condition condition;

    public BoidVirtualThread(Boid boid, CustomCyclicBarrier barrier,
                             VirtualAdministrator admin, VirtualThreadController controller,
                             ReentrantLock lock, Condition condition) {
        this.boid = boid;
        this.barrier = barrier;
        this.admin = admin;
        this.controller = controller;
        this.stopped = false;
        this.lock = lock;
        this.condition = condition;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    @Override
    public void run() {
        while(!stopped){
            lock.lock();
            try {
                while(this.controller.isPaused()){
                    try{
                        this.condition.await();
                    } catch (InterruptedException e) {
                    }
                }
            } finally {
                lock.unlock();
            }

            this.boid.updateVelocity(this.controller.getModel());

            try{
                this.barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
            }

            this.boid.updatePos(this.controller.getModel());
            this.admin.threadDone();
        }
    }



}
