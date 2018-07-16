package com.alanueyn.projects.practiceroutefinder.logic;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Population {

    private ArrayList<Route> routes = new ArrayList<Route>(GeneticAlgorithm.POPULATION_SIZE);
    public Population(int populationSize, GeneticAlgorithm geneticAlgorithm) {
        IntStream.range(0,populationSize).forEach(x -> routes.add(new Route(geneticAlgorithm.getInitialRoute())));
    }

    public Population(int populationSize, ArrayList<Checkpoint> checkpoints) {
        IntStream.range(0,populationSize).forEach(x -> routes.add(new Route(checkpoints)));
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void sortRoutesByFitness() {
        routes.sort((route1,route2) -> {
            int flag = 0;
            if (route1.getFitness() > route2.getFitness()) flag = -1;
            else if (route1.getFitness() < route2.getFitness()) flag = 1;
            return flag;
        });
    }
}