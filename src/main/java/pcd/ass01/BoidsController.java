package pcd.ass01;

import static pcd.ass01.Constants.*;

public abstract class BoidsController {
    protected BoidsModel model;
    protected BoidsView view;
    protected final int numThreads = Runtime.getRuntime().availableProcessors() + 1;
    protected volatile boolean running = true;
    protected volatile boolean paused = false;

    private int framerate;

    public BoidsController() {
        model = new BoidsModel(
                SEPARATION_WEIGHT, ALIGNMENT_WEIGHT, COHESION_WEIGHT,
                ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
                MAX_SPEED,
                PERCEPTION_RADIUS,
                AVOID_RADIUS);
        view = new BoidsView(this, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public abstract void runSimulation();

    public abstract void newSimulation();

    public abstract void stopSimulation();

    public abstract void resumeSimulation();

    public synchronized void pauseSimulation() {
        this.paused = true;
    }

    public BoidsModel getModel() {
        return this.model;
    }

    public void updateView() {
        var t0 = System.currentTimeMillis();

        if (this.view != null) {
            this.view.update(framerate);
            var t1 = System.currentTimeMillis();
            var dtElapsed = t1 - t0;
            var frameRatePeriod = 1000 / FRAMERATE;

            if (dtElapsed < frameRatePeriod) {
                try {
                    Thread.sleep(frameRatePeriod - dtElapsed);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                framerate = FRAMERATE;
            } else {
                framerate = (int) (1000 / dtElapsed);
            }
        }
    }
}