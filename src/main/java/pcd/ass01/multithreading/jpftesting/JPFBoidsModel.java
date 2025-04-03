package pcd.ass01.multithreading.jpftesting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class JPFBoidsModel {

    private final List<JPFBoid> boids;
    private final double separationWeight;
    private final double alignmentWeight;
    private final double cohesionWeight;
    private final double width;
    private final double height;
    private final double maxSpeed;
    private final double perceptionRadius;
    private final double avoidRadius;
    private final ReentrantLock lock;
    private final MockGenerator generator;

    public JPFBoidsModel(double initialSeparationWeight,
                      double initialAlignmentWeight,
                      double initialCohesionWeight,
                      double width,
                      double height,
                      double maxSpeed,
                      double perceptionRadius,
                      double avoidRadius,
                      MockGenerator generator){
        separationWeight = initialSeparationWeight;
        alignmentWeight = initialAlignmentWeight;
        cohesionWeight = initialCohesionWeight;
        this.width = width;
        this.height = height;
        this.maxSpeed = maxSpeed;
        this.perceptionRadius = perceptionRadius;
        this.avoidRadius = avoidRadius;
        this.generator = generator;
        this.lock = new ReentrantLock();

        boids = new ArrayList<>();
    }

    public synchronized List<JPFBoid> getBoids(){
        return boids;
    }

    public double getMinX() {
        return -width/2;
    }

    public double getMaxX() {
        return width/2;
    }

    public double getMinY() {
        return -height/2;
    }

    public double getMaxY() {
        return height/2;
    }

    public double getWidth() {
        return width;
    }

    public  double getHeight() {
        return height;
    }

    public synchronized double getSeparationWeight() {
        return separationWeight;
    }

    public synchronized double getCohesionWeight() {
        return cohesionWeight;
    }

    public synchronized double getAlignmentWeight() {
        return alignmentWeight;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getAvoidRadius() {
        return avoidRadius;
    }

    public double getPerceptionRadius() {
        return perceptionRadius;
    }

    public void setBoidsNumber(int boidsNumber) {
        if(!boids.isEmpty()) {
            boids.clear();
        }
        for (int i = 0; i < boidsNumber; i++) {
            P2d pos = new P2d(-width/2 + generator.nextDouble() * width, -height/2 + generator.nextDouble() * height);
            V2d vel = new V2d(generator.nextDouble() * maxSpeed/2 - maxSpeed/4, generator.nextDouble() * maxSpeed/2 - maxSpeed/4);
            boids.add(new JPFBoid(pos, vel, this.lock));
        }
    }
}