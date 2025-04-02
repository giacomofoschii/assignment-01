package pcd.ass01;

public abstract class BoidsController {
    protected final static double SEPARATION_WEIGHT = 1.0;
    protected final static double ALIGNMENT_WEIGHT = 1.0;
    protected final static double COHESION_WEIGHT = 1.0;

    protected final static int ENVIRONMENT_WIDTH = 1000;
    protected final static int ENVIRONMENT_HEIGHT = 1000;
    protected final static double MAX_SPEED = 4.0;
    protected final static double PERCEPTION_RADIUS = 50.0;
    protected final static double AVOID_RADIUS = 20.0;

    protected final static int SCREEN_WIDTH = 1000;
    protected final static int SCREEN_HEIGHT = 800;

    protected BoidsModel model;
    protected BoidsView view;

    public abstract BoidsSimulator getSimulator();

    public BoidsModel getModel() {
        return this.model;
    };

    public BoidsView getView() {
        return this.view;
    };

    public abstract void initialize();

}
