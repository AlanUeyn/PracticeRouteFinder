package com.alanueyn.projects.practiceroutefinder.logic;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class GeneticAlgorithm {
    public static final double MUTATION_RATE = 0.25;
    public static final int TOURNAMENT_SELECTION_SIZE = 3;
    public static final int POPULATION_SIZE = 8;
    public static final int NUM_OF_ELITE_ROUTES = 1;
    public static final int NUM_OF_GENERATIONS = 100;
    private ArrayList<Checkpoint> initialRoute = null;

    public GeneticAlgorithm(ArrayList<Checkpoint> initialRoute) {
        this.initialRoute = initialRoute;
    }

    public ArrayList<Checkpoint> getInitialRoute() {
        return initialRoute;
    }

    public Population evolve(Population population) {
        return mutatePopulation(crossoverPopulation(population));
    }

    Population crossoverPopulation(Population population) {

        Population crossoverPopulation = new Population(population.getRoutes().size(), this);
        IntStream.range(0, NUM_OF_ELITE_ROUTES).forEach(x -> crossoverPopulation.getRoutes().set(x, population.getRoutes().get(x)));
        IntStream.range(NUM_OF_ELITE_ROUTES, crossoverPopulation.getRoutes().size()).forEach(x -> {
            Route route1 = selectTournamentPopulation(population).getRoutes().get(0);
            Route route2 = selectTournamentPopulation(population).getRoutes().get(0);
            crossoverPopulation.getRoutes().set(x, crossoverRoute(route1, route2));
        });
        return crossoverPopulation;
    }


    public Route crossoverRoute(Route route1, Route route2) {

        Route crossoverRoute = new Route(this);
        Route tempRoute1 = route1;
        Route tempRoute2 = route2;

        if (Math.random() < 0.5) {
            tempRoute1 = route2;
            tempRoute2 = route1;
        }

        for (int x = 0; x < crossoverRoute.getCheckpoints().size() / 2; ++x) {
            crossoverRoute.getCheckpoints().set(x, tempRoute1.getCheckpoints().get(x));
        }

        return fillNullInCrossoverRoute(crossoverRoute, tempRoute2);
    }

    private Route fillNullInCrossoverRoute(Route crossoverRoute, Route route) {
        crossoverRoute.getCheckpoints().set(crossoverRoute.getCheckpoints().size() - 1, route.getCheckpoints().get(route.getCheckpoints().size() - 1));
        route.getCheckpoints().stream().filter(x -> !crossoverRoute.getCheckpoints().contains(x)).forEach(checkpointX -> {
            for (int y = 0; y < route.getCheckpoints().size(); ++y) {
                if (crossoverRoute.getCheckpoints().get(y) == null) {
                    crossoverRoute.getCheckpoints().set(y, checkpointX);
                    break;
                }
            }
        });
        return crossoverRoute;
    }

    Route mutateRoute(Route route) {

        route.getCheckpoints().stream().filter(x -> Math.random() < MUTATION_RATE)
                .filter(x -> (route.getCheckpoints().indexOf(x) != 0))
                .filter(x -> (route.getCheckpoints().indexOf(x) != route.getCheckpoints().size() - 1))
                .forEach(checkpointX -> {
                    int y = (int) ((route.getCheckpoints().size() - 2) * Math.random()) % (route.getCheckpoints().size() - 1) + 1;
                    Checkpoint checkpointY = route.getCheckpoints().get(y);
                    route.getCheckpoints().set(route.getCheckpoints().indexOf(checkpointX), checkpointY);
                    route.getCheckpoints().set(y, checkpointX);
                });
        return route;

    }

    Population selectTournamentPopulation(Population population) {

        Population tournamentPopulation = new Population(TOURNAMENT_SELECTION_SIZE, this);
        IntStream.range(0, TOURNAMENT_SELECTION_SIZE).forEach(x -> tournamentPopulation.getRoutes().set(
                x, population.getRoutes().get((int) (Math.random() * population.getRoutes().size()))));
        tournamentPopulation.sortRoutesByFitness();
        return tournamentPopulation;
    }

    Population mutatePopulation(Population population) {

        population.getRoutes().stream().filter(x -> population.getRoutes().indexOf(x) >= NUM_OF_ELITE_ROUTES).forEach(x -> mutateRoute(x));
        return population;
    }
}