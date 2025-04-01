package pcd.ass01.taskexecutor;

public class BoidsController {
    final static double SEPARATION_WEIGHT = 1.0;
    final static double ALIGNMENT_WEIGHT = 1.0;
    final static double COHESION_WEIGHT = 1.0;

    final static int ENVIRONMENT_WIDTH = 1000;
    final static int ENVIRONMENT_HEIGHT = 1000;
    static final double MAX_SPEED = 4.0;
    static final double PERCEPTION_RADIUS = 50.0;
    static final double AVOID_RADIUS = 20.0;

    final static int SCREEN_WIDTH = 1000;
    final static int SCREEN_HEIGHT = 800;

    private BoidsSimulator simulator;
    private BoidsModel model;
    private BoidsView view;

    public void inizialize() {
        this.model = new BoidsModel(
                SEPARATION_WEIGHT, ALIGNMENT_WEIGHT, COHESION_WEIGHT,
                ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
                MAX_SPEED,
                PERCEPTION_RADIUS,
                AVOID_RADIUS);
        this.view = new BoidsView(this, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.simulator = new BoidsSimulator(this);
        this.simulator.runSimulation();
    }

    public void newSimulation() {
        new Thread(() -> this.simulator.runSimulation()).start();
    }

    public BoidsSimulator getSimulator() {
        return simulator;
    }

    public BoidsView getView() {
        return view;
    }

    public BoidsModel getModel() {
        return model;
    }
}
