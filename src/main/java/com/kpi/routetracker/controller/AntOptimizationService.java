package com.kpi.routetracker.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AntOptimizationService {

    private static final int NUM_ANTS = 12;
    private static final int NUM_COLONIES = 3;  // Кількість колоній
    private static final int MAX_ITERATIONS = 100;
    private static final double ALPHA = 1;    // Вплив феромонів
    private static final double BETA = 5;     // Вплив видимості (відстані)
    private static final double EVAPORATION = 0.5;  // Швидкість випаровування феромонів
    private static final double Q = 500;  // Постійна для оновлення феромонів
    private static final double DIVERSITY_THRESHOLD = 0.1; // Порогове значення для різноманітності

    private double[][] distances;  // Матриця відстаней між містами
    private List<double[][]> pheromones; // Список феромонних матриць для кожної колонії
    private Random random = new Random();

    public AntOptimizationService(double[][] distances) {
        this.distances = distances;
        this.pheromones = new ArrayList<>();

        for (int c = 0; c < NUM_COLONIES; c++) {
            double[][] colonyPheromones = new double[distances.length][distances.length];
            for (int i = 0; i < distances.length; i++) {
                for (int j = 0; j < distances.length; j++) {
                    colonyPheromones[i][j] = 1.0;
                }
            }
            pheromones.add(colonyPheromones);
        }
    }

    public int[] solve(int startCity) {
        int[] bestTour = null;
        double bestTourLength = Double.MAX_VALUE;

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_COLONIES);
        List<Callable<int[]>> tasks = new ArrayList<>();

        for (int colonyIndex = 0; colonyIndex < NUM_COLONIES; colonyIndex++) {
            final int colony = colonyIndex;
            tasks.add(() -> runColony(colony, startCity));
        }

        try {
            List<Future<int[]>> results = executorService.invokeAll(tasks);
            executorService.shutdown();

            for (Future<int[]> result : results) {
                int[] tour = result.get();
                double tourLength = calculateTourLength(tour);
                if (tourLength < bestTourLength) {
                    bestTourLength = tourLength;
                    bestTour = tour;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("Найкращий маршрут: " + bestTourLength + " км");
        return bestTour;
    }

    private int[] runColony(int colonyIndex, int startCity) {
        int[] bestTour = null;
        double bestTourLength = Double.MAX_VALUE;

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            int[][] ants = new int[NUM_ANTS][distances.length];

            for (int k = 0; k < NUM_ANTS; k++) {
                ants[k] = generateAntRoute(colonyIndex, startCity);
            }

            for (int k = 0; k < NUM_ANTS; k++) {
                double tourLength = calculateTourLength(ants[k]);
                if (tourLength < bestTourLength) {
                    bestTourLength = tourLength;
                    bestTour = ants[k].clone();
                }
                updatePheromones(colonyIndex, ants[k], tourLength);
            }

            evaporatePheromones(colonyIndex);
        }

        return bestTour;
    }

    private int[] generateAntRoute(int colonyIndex, int startCity) {
        int[] tour = new int[distances.length];
        boolean[] visited = new boolean[distances.length];

        int currentCity = startCity;
        tour[0] = currentCity;
        visited[currentCity] = true;

        for (int i = 1; i < distances.length; i++) {
            currentCity = selectNextCity(colonyIndex, currentCity, visited);
            tour[i] = currentCity;
            visited[currentCity] = true;
        }

        return tour;
    }

    private double calculateTourLength(int[] tour) {
        double length = 0.0;
        for (int i = 0; i < distances.length - 1; i++) {
            length += distances[tour[i]][tour[i + 1]];
        }
        length += distances[tour[distances.length - 1]][tour[0]]; // повернення до стартового міста
        return length;
    }

    private void updatePheromones(int colonyIndex, int[] tour, double tourLength) {
        for (int i = 0; i < distances.length - 1; i++) {
            pheromones.get(colonyIndex)[tour[i]][tour[i + 1]] += Q / tourLength;
        }
    }

    private void evaporatePheromones(int colonyIndex) {
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances.length; j++) {
                pheromones.get(colonyIndex)[i][j] *= (1 - EVAPORATION);

                if (random.nextDouble() < DIVERSITY_THRESHOLD) {
                    double mutation = (random.nextDouble() - 0.5) * 0.1;
                    pheromones.get(colonyIndex)[i][j] += mutation;
                    pheromones.get(colonyIndex)[i][j] = Math.max(0.01, pheromones.get(colonyIndex)[i][j]);
                }
            }
        }
    }

    private int selectNextCity(int colonyIndex, int currentCity, boolean[] visited) {
        double[] probabilities = new double[distances.length];
        double sum = 0.0;

        for (int i = 0; i < distances.length; i++) {
            if (!visited[i]) {
                double pheromone = pheromones.get(colonyIndex)[currentCity][i];
                double visibility = 1.0 / distances[currentCity][i];
                probabilities[i] = Math.pow(pheromone, ALPHA) * Math.pow(visibility, BETA);
                sum += probabilities[i];
            }
        }

        double rand = random.nextDouble() * sum;
        double cumulative = 0.0;

        for (int i = 0; i < distances.length; i++) {
            if (!visited[i]) {
                cumulative += probabilities[i];
                if (cumulative >= rand) {
                    return i;
                }
            }
        }
        return -1;
    }
}
