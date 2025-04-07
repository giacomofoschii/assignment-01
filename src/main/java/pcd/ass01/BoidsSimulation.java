package pcd.ass01;

import pcd.ass01.multithreading.MultiThreadController;
import pcd.ass01.taskexecutor.TaskController;
import pcd.ass01.virtualthread.VirtualController;

import javax.swing.*;

import static pcd.ass01.Constants.*;

public class BoidsSimulation {

    public static void main(String[] args) {
        String[] options = {"Multi-Threading", "Task-Based (Java Executor Framework)", "Virtual-Threading"};
        String version = (String) JOptionPane.showInputDialog(null, "Select concurrency version:",
                "Concurrency Version", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (version == null) {
            System.exit(0);
        }
        
        BoidsController boidsController = switch (version) {
            case MULTI_VERSION -> new MultiThreadController();
            case TASK_VERSION -> new TaskController();
            case VIRTUAL_VERSION -> new VirtualController();
            default -> throw new IllegalStateException("Unexpected value: " + version);
        };
        boidsController.runSimulation();
    }
}
