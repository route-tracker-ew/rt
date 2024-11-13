package com.kpi.routetracker.distance.calculator.impl;

import com.kpi.routetracker.distance.calculator.elements.Edge;
import com.kpi.routetracker.distance.calculator.elements.Vertex;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TravelingSalesman {

    private static final int NUM_CITIES = 4;
    private static final int NUM_ANTS = 10;
    private static final int NUM_COLONIES = 3;  // Кількість колоній
    private static final int MAX_ITERATIONS = 10;
    private static final double ALPHA = 1;    // Вплив феромонів
    private static final double BETA = 5;     // Вплив видимості (відстані)
    private static final double EVAPORATION = 0.5;  // Швидкість випаровування феромонів
    private static final double Q = 500;  // Постійна для оновлення феромонів
    private static final double DIVERSITY_THRESHOLD = 0.1; // Порогове значення для різноманітності

    private double[][] distances;  // Матриця відстаней між містами
    private List<double[][]> pheromones; // Список феромонних матриць для кожної колонії
    private Random random = new Random();

    public TravelingSalesman(double[][] distances) {
        this.distances = distances;
        this.pheromones = new ArrayList<>();

        // Ініціалізація феромонів для кожної колонії
        for (int c = 0; c < NUM_COLONIES; c++) {
            double[][] colonyPheromones = new double[NUM_CITIES][NUM_CITIES];
            for (int i = 0; i < NUM_CITIES; i++) {
                for (int j = 0; j < NUM_CITIES; j++) {
                    colonyPheromones[i][j] = 1.0;
                }
            }
            pheromones.add(colonyPheromones);
        }
    }

    /**
     * Головний метод для запуску мурашиного алгоритму.
     * Повертає найкращий знайдений тур.
     */
    public int[] solve() {
        int[] bestTour = null;
        double bestTourLength = Double.MAX_VALUE;

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            for (int c = 0; c < NUM_COLONIES; c++) {  // Запуск для кожної колонії
                int[][] ants = new int[NUM_ANTS][NUM_CITIES];

                // Генерація маршрутів для кожної мурахи в колонії
                for (int k = 0; k < NUM_ANTS; k++) {
                    ants[k] = generateAntRoute(c);
                }

                // Оновлення феромонів на основі пройдених маршрутів
                for (int k = 0; k < NUM_ANTS; k++) {
                    double tourLength = calculateTourLength(ants[k]);
                    if (tourLength < bestTourLength) {
                        bestTourLength = tourLength;
                        bestTour = ants[k].clone();
                    }
                    updatePheromones(c, ants[k], tourLength);
                }

                // Випаровування феромонів
                evaporatePheromones(c);
            }

            // Контроль за різноманітністю рішень
            controlDiversity();

            // Виводимо найкращий маршрут після кожної ітерації
            if (iter == 0) {
                System.out.print("Найкращий маршрут після ітерації " + (iter + 1) + ": ");
                for (int city : bestTour) {
                    System.out.print(city + " ");
                }
                System.out.println("Довжина: " + bestTourLength + " км");
            }

        }

        System.out.println("Найкращий маршрут: " + bestTourLength + " км");
        return bestTour;
    }

    /**
     * Генерує маршрут для мурахи в конкретній колонії.
     * @param colonyIndex індекс колонії
     * @return згенерований тур
     */
    private int[] generateAntRoute(int colonyIndex) {
        int[] tour = new int[NUM_CITIES];
        boolean[] visited = new boolean[NUM_CITIES];
        tour[0] = 0; // Фіксована стартова точка - Вінниця
        visited[tour[0]] = true;

        for (int i = 1; i < NUM_CITIES - 1; i++) { // Додаємо міста, окрім Крефельда
            int nextCity = selectNextCity(colonyIndex, tour[i - 1], visited);
            tour[i] = nextCity;
            visited[nextCity] = true;
        }

        tour[NUM_CITIES - 1] = NUM_CITIES - 1; // Крефельд - фіксована кінцева точка
        return tour;
    }

    /**
     * Вибирає наступне місто для мурахи на основі ймовірності та феромонів.
     * Використовується комбінована функція ймовірностей для вибору.
     * @param colonyIndex індекс колонії
     * @param currentCity поточне місто
     * @param visited список відвіданих міст
     * @return наступне місто
     */
    private int selectNextCity(int colonyIndex, int currentCity, boolean[] visited) {
        double[] probabilities = new double[NUM_CITIES];
        double sum = 0.0;

        // Обчислення ймовірностей для вибору наступного міста
        for (int i = 0; i < NUM_CITIES; i++) {
            if (!visited[i] && i != 11) { // Не вибираємо Крефельд, якщо він вже в маршруті
                probabilities[i] = Math.pow(pheromones.get(colonyIndex)[currentCity][i], ALPHA) *
                        Math.pow(1.0 / distances[currentCity][i], BETA);
                sum += probabilities[i];
            }
        }

        double randomValue = random.nextDouble() * sum;
        double cumulativeProbability = 0.0;

        for (int i = 0; i < NUM_CITIES; i++) {
            if (!visited[i] && i != 11) { // Не вибираємо Крефельд
                cumulativeProbability += probabilities[i];
                if (cumulativeProbability >= randomValue) {
                    return i;
                }
            }
        }

        return -1;  // Не повинно сюди дійти
    }

    /**
     * Обчислює довжину маршруту.
     * @param tour маршрут мурахи
     * @return загальна довжина туру
     */
    private double calculateTourLength(int[] tour) {
        double length = 0.0;
        for (int i = 0; i < NUM_CITIES - 1; i++) {
            length += distances[tour[i]][tour[i + 1]];
        }
        // Додамо повернення з останнього міста до Крефельда
        length += distances[tour[NUM_CITIES - 1]][tour[0]];
        return length;
    }

    /**
     * Оновлює рівень феромонів для конкретної колонії на основі довжини туру.
     * @param colonyIndex індекс колонії
     * @param tour маршрут мурахи
     * @param tourLength довжина туру
     */
    private void updatePheromones(int colonyIndex, int[] tour, double tourLength) {
        for (int i = 0; i < NUM_CITIES - 1; i++) {
            pheromones.get(colonyIndex)[tour[i]][tour[i + 1]] += Q / tourLength;
            pheromones.get(colonyIndex)[tour[i + 1]][tour[i]] += Q / tourLength;
        }
        // Оновимо феромони для повернення до Крефельда
        pheromones.get(colonyIndex)[tour[NUM_CITIES - 1]][tour[0]] += Q / tourLength;
        pheromones.get(colonyIndex)[tour[0]][tour[NUM_CITIES - 1]] += Q / tourLength;
    }

    /**
     * Випаровує феромони для конкретної колонії.
     * @param colonyIndex індекс колонії
     */
    private void evaporatePheromones(int colonyIndex) {
        for (int i = 0; i < NUM_CITIES; i++) {
            for (int j = 0; j < NUM_CITIES; j++) {
                pheromones.get(colonyIndex)[i][j] *= (1.0 - EVAPORATION);
            }
        }
    }

    /**
     * Контролює різноманітність рішень, щоб уникнути передчасної конвергенції.
     */
    private void controlDiversity() {
        for (int i = 0; i < NUM_CITIES; i++) {
            for (int j = 0; j < NUM_CITIES; j++) {
                double avgPheromone = 0;
                for (int c = 0; c < NUM_COLONIES; c++) {
                    avgPheromone += pheromones.get(c)[i][j];
                }
                avgPheromone /= NUM_COLONIES;

                for (int c = 0; c < NUM_COLONIES; c++) {
                    if (Math.abs(pheromones.get(c)[i][j] - avgPheromone) < DIVERSITY_THRESHOLD) {
                        pheromones.get(c)[i][j] = avgPheromone;
                    }
                }
            }
        }
    }
}
