package com.alanueyn.projects.practiceroutefinder.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Route {

    private boolean isFitnessChanged = true;
    private double fitness = 0D;
    private ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();


    public Route(GeneticAlgorithm geneticAlgorithm) {
        geneticAlgorithm.getInitialRoute().forEach(x -> checkpoints.add(null));
    }

    public Route(ArrayList<Checkpoint> checkpoints) {
        this.checkpoints.addAll(checkpoints);
        shuffleRoute(this.checkpoints);
    }

    public ArrayList<Checkpoint> getCheckpoints() {
        isFitnessChanged = true;
        return checkpoints;
    }


    public double getFitness() {
        if (isFitnessChanged) {
            fitness = (1 / calculateTotalDistance()) * 10000;
            isFitnessChanged = false;
        }
        return fitness;
    }


    public double calculateTotalDistance() {
        int checkpointsSize = this.checkpoints.size();
        return (int) (this.checkpoints.stream().mapToDouble(x -> {
            int checkpointIndex = this.checkpoints.indexOf(x);
            double returnValue = 0;
            if (checkpointIndex < checkpointsSize - 1)
                returnValue = x.measureDistance(this.checkpoints.get(checkpointIndex + 1));
            return returnValue;
        }).sum() + this.checkpoints.get(0).measureDistance(this.checkpoints.get(checkpointsSize - 1)));
    }

    @Override
    public String toString() {
        return Arrays.toString(checkpoints.toArray());
    }


    public static void shuffleRoute(ArrayList<Checkpoint> checkpoints) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = checkpoints.size() - 2; i > 0; i--) {
            int index = rnd.nextInt(i) % (checkpoints.size() - 1) + 1;
            Checkpoint a = checkpoints.get(index);
            checkpoints.set(index, checkpoints.get(i));
            checkpoints.set(i, a);
        }
    }
}