package pcd.ass01.multithreading;

import pcd.ass01.BoidsController;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsSimulator;
import pcd.ass01.BoidsView;

public class MultithreadingController extends BoidsController {

    private MultithreadingSimulator simulator;

    public BoidsSimulator getSimulator() {
        return this.simulator;
    }

    @Override
    public void initialize() {
        model = new BoidsModel(
                SEPARATION_WEIGHT, ALIGNMENT_WEIGHT, COHESION_WEIGHT,
                ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
                MAX_SPEED,
                PERCEPTION_RADIUS,
                AVOID_RADIUS);
        view = new BoidsView(this, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.simulator = new MultithreadingSimulator(this);
        this.simulator.startThreads();
        this.simulator.runSimulation();
    }

}
