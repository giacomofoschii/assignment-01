package pcd.ass01;

import pcd.ass01.multithreading.MultiThreadController;
import pcd.ass01.taskexecutor.TaskController;
import pcd.ass01.virtualthread.VirtualController;

public class BoidsSimulation {

    public static void main(String[] args) {
        String version = "multithreading";
        BoidsController boidsController;
        switch (version) {
            case "taskexecutor":
                boidsController = new TaskController();
                break;
            case "virtualthread":
                boidsController = new VirtualController();
                break;
            default:
                boidsController = new MultiThreadController();
                break;
        }
        boidsController.runSimulation();
    }
}
