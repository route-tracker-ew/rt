package com.kpi.routetracker.distance;

import com.kpi.routetracker.model.parcel.Parcel;

import java.util.*;

public class DeliveryService {

    private static final double INITIAL_PHEROMONE = 1.0;
    private static final double ALPHA = 1.0; // Вплив феромонів
    private static final double BETA = 5.0; // Вплив відстаней
    private static final double EVAPORATION_RATE = 0.5; // Швидкість випаровування феромонів
    private static final int NUM_ANTS = 10; // Кількість мурашок
    private static final int NUM_ITERATIONS = 100; // Кількість ітерацій

//    // Метод для планування маршруту доставки
//    public Route planDeliveryRoute(List<Parcel> parcels, String startCity) {
//        // Створення списку міст, через які потрібно проїхати
//        List<String> citiesToVisit = collectCities(parcels, startCity);
//
//        // Створення карти відстаней між містами (тут використовуємо випадкові відстані для прикладу)
//        Map<String, Map<String, Double>> distanceMatrix = createDistanceMatrix(citiesToVisit);
//
//        // Запуск мурашиного алгоритму для оптимізації маршруту
//        List<String> optimalRoute = optimizeRoute(citiesToVisit, distanceMatrix);
//
//        // Створення маршруту
//        Route route = new Route();
//        route.setStartCity(startCity);
//        for (String city : optimalRoute) {
//            route.addCity(city);
//        }
//        route.setEndCity(optimalRoute.get(optimalRoute.size() - 1));
//
//        return route;
//    }

    // Метод для збирання всіх міст (source та destination) з посилок
    private List<String> collectCities(List<Parcel> parcels, String startCity) {
        Set<String> cities = new HashSet<>();
        cities.add(startCity); // Додаємо стартове місто

        for (Parcel parcel : parcels) {
            cities.add(parcel.getSourceCity());
            cities.add(parcel.getDestinationCity());
        }

        return new ArrayList<>(cities);
    }

    // Створення матриці відстаней між містами (для прикладу використовуємо випадкові відстані)
    private Map<String, Map<String, Double>> createDistanceMatrix(List<String> citiesToVisit) {
        Map<String, Map<String, Double>> distanceMatrix = new HashMap<>();

        for (String city1 : citiesToVisit) {
            Map<String, Double> distancesFromCity = new HashMap<>();
            for (String city2 : citiesToVisit) {
                if (!city1.equals(city2)) {
                    // Генерація випадкової відстані між містами для прикладу
                    distancesFromCity.put(city2, Math.random() * 1000); // Відстань в км
                }
            }
            distanceMatrix.put(city1, distancesFromCity);
        }

        return distanceMatrix;
    }

    // Мурашиний алгоритм для оптимізації маршруту
    private List<String> optimizeRoute(List<String> citiesToVisit, Map<String, Map<String, Double>> distanceMatrix) {
        Map<String, Double> pheromones = initializePheromones(citiesToVisit);

        List<String> bestRoute = null;
        double bestRouteLength = Double.MAX_VALUE;

        for (int iter = 0; iter < NUM_ITERATIONS; iter++) {
            List<List<String>> allAntRoutes = new ArrayList<>();
            for (int ant = 0; ant < NUM_ANTS; ant++) {
                List<String> antRoute = constructRoute(citiesToVisit, pheromones, distanceMatrix);
                allAntRoutes.add(antRoute);

                double routeLength = calculateRouteLength(antRoute, distanceMatrix);
                if (routeLength < bestRouteLength) {
                    bestRouteLength = routeLength;
                    bestRoute = antRoute;
                }
            }

            updatePheromones(pheromones, allAntRoutes);
        }

        return bestRoute;
    }

    // Ініціалізація феромонів
    private Map<String, Double> initializePheromones(List<String> citiesToVisit) {
        Map<String, Double> pheromones = new HashMap<>();
        for (String city : citiesToVisit) {
            pheromones.put(city, INITIAL_PHEROMONE);
        }
        return pheromones;
    }

    // Створення маршруту для мурашки
    private List<String> constructRoute(List<String> citiesToVisit, Map<String, Double> pheromones, Map<String, Map<String, Double>> distanceMatrix) {
        List<String> route = new ArrayList<>();
        Set<String> visitedCities = new HashSet<>();
        String currentCity = citiesToVisit.get(0); // Починаємо з першого міста

        visitedCities.add(currentCity);
        route.add(currentCity);

        while (visitedCities.size() < citiesToVisit.size()) {
            String nextCity = selectNextCity(citiesToVisit, visitedCities, pheromones, distanceMatrix);
            visitedCities.add(nextCity);
            route.add(nextCity);
            currentCity = nextCity;
        }

        return route;
    }

    // Вибір наступного міста з урахуванням феромонів та відстаней
    private String selectNextCity(List<String> citiesToVisit, Set<String> visitedCities, Map<String, Double> pheromones, Map<String, Map<String, Double>> distanceMatrix) {
        double totalWeight = 0.0;
        Map<String, Double> cityWeights = new HashMap<>();

        for (String city : citiesToVisit) {
            if (!visitedCities.contains(city)) {
                double pheromone = pheromones.getOrDefault(city, INITIAL_PHEROMONE);
                double distance = distanceMatrix.get(visitedCities.iterator().next()).get(city);
                double weight = Math.pow(pheromone, ALPHA) * Math.pow(1.0 / distance, BETA); // Враховуємо відстань
                cityWeights.put(city, weight);
                totalWeight += weight;
            }
        }

        double randomWeight = Math.random() * totalWeight;
        double cumulativeWeight = 0.0;
        for (String city : cityWeights.keySet()) {
            cumulativeWeight += cityWeights.get(city);
            if (cumulativeWeight >= randomWeight) {
                return city;
            }
        }

        return null;
    }

    // Обчислення довжини маршруту (сумарна відстань)
    private double calculateRouteLength(List<String> route, Map<String, Map<String, Double>> distanceMatrix) {
        double length = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            length += distanceMatrix.get(route.get(i)).get(route.get(i + 1));
        }
        return length;
    }

    // Оновлення феромонів
    private void updatePheromones(Map<String, Double> pheromones, List<List<String>> allAntRoutes) {
        for (String city : pheromones.keySet()) {
            double pheromone = pheromones.get(city);
            pheromones.put(city, pheromone * (1 - EVAPORATION_RATE));
        }

        for (List<String> route : allAntRoutes) {
            double contribution = 1.0 / calculateRouteLength(route, createDistanceMatrix(route));

            for (String city : route) {
                double pheromone = pheromones.getOrDefault(city, 0.0);
                pheromones.put(city, pheromone + contribution);
            }
        }
    }
}
