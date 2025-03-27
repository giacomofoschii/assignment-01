package pcd.ass01;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoidsModel {
    
    private final List<Boid> boids;
    private double separationWeight; 
    private double alignmentWeight; 
    private double cohesionWeight; 
    private final double width;
    private final double height;
    private final double maxSpeed;
    private volatile boolean paused = false;
    private final double perceptionRadius;
    private final double avoidRadius;
    private final Random generator;

    public BoidsModel(double initialSeparationWeight,
                      double initialAlignmentWeight,
                      double initialCohesionWeight,
                      double width,
                      double height,
                      double maxSpeed,
                      double perceptionRadius,
                      double avoidRadius,
                      Random generator){
        separationWeight = initialSeparationWeight;
        alignmentWeight = initialAlignmentWeight;
        cohesionWeight = initialCohesionWeight;
        this.width = width;
        this.height = height;
        this.maxSpeed = maxSpeed;
        this.perceptionRadius = perceptionRadius;
        this.avoidRadius = avoidRadius;
        this.generator = generator;
        
    	boids = new ArrayList<>();
    }
    
    public synchronized boolean isPaused() {
        return paused;
    }

    public List<Boid> getBoids(){
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
 
    public double getHeight() {
    	return height;
    }

    public void setSeparationWeight(double value) {
    	this.separationWeight = value;
    }

    public void setAlignmentWeight(double value) {
    	this.alignmentWeight = value;
    }

    public void setCohesionWeight(double value) {
    	this.cohesionWeight = value;
    }

    public double getSeparationWeight() {
    	return separationWeight;
    }

    public double getCohesionWeight() {
    	return cohesionWeight;
    }

    public double getAlignmentWeight() {
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

    public void setBoidsNumber(int nBoids) {
        boids.clear();
        for (int i = 0; i < nBoids; i++) {
            P2d pos = new P2d(-width/2 + generator.nextDouble() * width, -height/2 + generator.nextDouble() * height);
            V2d vel = new V2d(generator.nextDouble() * maxSpeed/2 - maxSpeed/4, generator.nextDouble() * maxSpeed/2 - maxSpeed/4);
            boids.add(new Boid(pos, vel));
        }
    }

    public synchronized void setPaused(boolean paused) {
        this.paused = paused;
        if (!paused) {
            notifyAll();
        }
    }
    
}
