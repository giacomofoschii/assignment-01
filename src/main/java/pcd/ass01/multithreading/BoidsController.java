package pcd.ass01.multithreading;

public class BoidsController {

    BoidsSimulator boidsSimulator;

    public void setBoidsNumber(final int nBoids, final BoidsModel model) {
        model.setBoidsNumber(nBoids);
    }

    public void setBoidsSimulator(final BoidsSimulator boidsSimulator) {
        this.boidsSimulator = boidsSimulator;
    }

    public BoidsSimulator getSimulator() {
        return this.boidsSimulator;
    }

}
