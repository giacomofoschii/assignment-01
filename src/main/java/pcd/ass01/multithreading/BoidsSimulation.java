package pcd.ass01.multithreading;

public class BoidsSimulation {

    public static void main(String[] args) {
        var boidsController = new MultithreadingController();
        boidsController.startThreads();
        boidsController.runSimulation();
    }
}