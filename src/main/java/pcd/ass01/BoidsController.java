package pcd.ass01;

import static pcd.ass01.Constants.*;

/**
 * BoidsController is the controller class for the Boids simulation.
 */
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

    /**
     * Starts the simulation.
     */
    public abstract void runSimulation();

    /**
     * Starts a new simulation
     */
    public abstract void newSimulation();

    /**
     * Stops the current simulation.
     */
    public abstract void stopSimulation();

    /**
     * Resumes the simulation if it is paused.
     */
    public abstract void resumeSimulation();

    /**
     * Pauses the simulation.
     */
    public synchronized void pauseSimulation() {
        this.paused = true;
    }

    /**
     * Returns the current model linked to the controller.
     *
     * @return the current model
     */
    public BoidsModel getModel() {
        return this.model;
    }

    /**
     * Updates the view of the simulation,
     * calculating the time elapsed since the last update
     * and adjusting the framerate accordingly.
     */
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
                } catch (InterruptedException ignored) {
                }
                framerate = FRAMERATE;
            } else {
                framerate = (int) (1000 / dtElapsed);
            }
        }
    }
}