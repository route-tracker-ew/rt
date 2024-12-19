package org.example;

import java.util.Random;

public class AntAlgorithm {

    private static final int NUM_CITIES = 5;  // Кількість міст (в цьому прикладі 5 міст)
    private static final int NUM_ANTS = 10;    // Кількість мурах
    private static final int MAX_ITERATIONS = 10;  // Кількість ітерацій
    private static final double ALPHA = 1.0;    // Вплив феромонів
    private static final double BETA = 5.0;     // Вплив видимості (відстані)
    private static final double EVAPORATION = 0.5;  // Швидкість випаровування феромонів
    private static final double Q = 500.0;  // Постійна для оновлення феромонів

    private double[][] distances;  // Матриця відстаней між містами
    private double[][] pheromones; // Феромонна матриця
    private Random random = new Random();

    // Список реальних відстаней між містами
    // Відстані між містами у км
    private static final double[][] REAL_DISTANCES = {
            {0, 270, 1400, 1300, 800},  // Київ
            {270, 0, 1300, 1200, 750},  // Вінниця
            {1400, 1300, 0, 150, 1000}, // Крефельд
            {1300, 1200, 150, 0, 600},  // Берлін
            {800, 750, 1000, 600, 0}    // Варшава
    };

    // Масив назв міст
    private static final String[] CITY_NAMES = {
            "Київ", "Вінниця", "Крефельд", "Берлін", "Варшава"
    };

    public AntAlgorithm(double[][] distances) {
        this.distances = distances;
        this.pheromones = new double[NUM_CITIES][NUM_CITIES];

        // Ініціалізація феромонів
        for (int i = 0; i < NUM_CITIES; i++) {
            for (int j = 0; j < NUM_CITIES; j++) {
                pheromones[i][j] = 1.0;  // Початковий рівень феромонів
            }
        }
    }

    /**
     * Головний метод для запуску мурашиного алгоритму.
     * Повертає найкращий знайдений тур.
     * @param startCity індекс стартового міста
     */
    public String[] solve(int startCity) {
        String[] bestTour = null;
        double bestTourLength = Double.MAX_VALUE;

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            int[][] ants = new int[NUM_ANTS][NUM_CITIES];

            // Генерація маршрутів для кожної мурахи
            for (int k = 0; k < NUM_ANTS; k++) {
                ants[k] = generateAntRoute(startCity);
            }

            // Оновлення феромонів на основі пройдених маршрутів
            for (int k = 0; k < NUM_ANTS; k++) {
                double tourLength = calculateTourLength(ants[k]);
                if (tourLength < bestTourLength) {
                    bestTourLength = tourLength;
                    bestTour = convertTourToCityNames(ants[k]);
                }
                updatePheromones(ants[k], tourLength);
            }

            // Випаровування феромонів
            evaporatePheromones();

            // Виводимо найкращий маршрут після кожної ітерації
            System.out.print("Найкращий маршрут після ітерації " + (iter + 1) + ": ");
            for (String city : bestTour) {
                System.out.print(city + " ");
            }
            System.out.println("Довжина: " + bestTourLength + " км");
        }

        System.out.println("Найкращий маршрут: " + bestTourLength + " км");
        return bestTour;
    }

    /**
     * Генерує маршрут для мурахи.
     * @param startCity індекс стартового міста
     * @return згенерований тур
     */
    private int[] generateAntRoute(int startCity) {
        int[] tour = new int[NUM_CITIES];
        boolean[] visited = new boolean[NUM_CITIES];
        tour[0] = startCity; // Стартова точка (динамічно змінюється)
        visited[tour[0]] = true;

        for (int i = 1; i < NUM_CITIES; i++) {
            int nextCity = selectNextCity(tour[i - 1], visited);
            tour[i] = nextCity;
            visited[nextCity] = true;
        }

        return tour;
    }

    /**
     * Вибирає наступне місто для мурахи на основі ймовірності та феромонів.
     * @param currentCity поточне місто
     * @param visited список відвіданих міст
     * @return наступне місто
     */
    private int selectNextCity(int currentCity, boolean[] visited) {
        double[] probabilities = new double[NUM_CITIES];
        double sum = 0.0;

        // Обчислення ймовірностей для вибору наступного міста
        for (int i = 0; i < NUM_CITIES; i++) {
            if (!visited[i]) {
                probabilities[i] = Math.pow(pheromones[currentCity][i], ALPHA) *
                        Math.pow(1.0 / distances[currentCity][i], BETA);
                sum += probabilities[i];
            }
        }

        double randomValue = random.nextDouble() * sum;
        double cumulativeProbability = 0.0;

        for (int i = 0; i < NUM_CITIES; i++) {
            if (!visited[i]) {
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
        return length;
    }

    /**
     * Оновлює рівень феромонів на основі довжини туру.
     * @param tour маршрут мурахи
     * @param tourLength довжина туру
     */
    private void updatePheromones(int[] tour, double tourLength) {
        for (int i = 0; i < NUM_CITIES - 1; i++) {
            pheromones[tour[i]][tour[i + 1]] += Q / tourLength;
            pheromones[tour[i + 1]][tour[i]] += Q / tourLength;
        }
    }

    /**
     * Випаровує феромони.
     */
    private void evaporatePheromones() {
        for (int i = 0; i < NUM_CITIES; i++) {
            for (int j = 0; j < NUM_CITIES; j++) {
                pheromones[i][j] *= (1.0 - EVAPORATION);
            }
        }
    }

    /**
     * Перетворює маршрут з індексів міст у назви міст.
     * @param tour маршрут з індексів
     * @return маршрут з назвами міст
     */
    private String[] convertTourToCityNames(int[] tour) {
        String[] cityTour = new String[NUM_CITIES];
        for (int i = 0; i < NUM_CITIES; i++) {
            cityTour[i] = CITY_NAMES[tour[i]];
        }
        return cityTour;
    }

    public static void main(String[] args) {
        // Використовуємо реальні відстані для тесту
        double[][] distances = REAL_DISTANCES;

        AntAlgorithm antAlgorithm = new AntAlgorithm(distances);

        // Динамічно задаємо стартове місто
        int startCity = 1;  // Наприклад, починаємо з Вінниці (індекс 1)
        antAlgorithm.solve(startCity);
    }
}
