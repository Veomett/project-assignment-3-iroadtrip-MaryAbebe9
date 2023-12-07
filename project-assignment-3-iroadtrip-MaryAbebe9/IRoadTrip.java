import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class IRoadTrip {
    private Graph countryGraph;
    private Map<String, String> fixedCountries;

    public IRoadTrip(String[] args) {
        countryGraph = new Graph();
        fixedCountries = createFixedCountries();

        try {
            loadBorders("borders.txt"); // Replace with the actual file path
    
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void loadBorders(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseAndStoreBorders(line);
            }
        }
    }

    private void parseAndStoreBorders(String line) {
        String[] parts = line.split(" = ");
        String country = parts[0].trim();
        String standardizedCountry = fixedCountries.getOrDefault(country, country);
    
        countryGraph.addCountry(standardizedCountry);
    
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            String[] borders = parts[1].split("; ");
            for (String border : borders) {
                String[] borderParts = border.trim().split("\\s+");
                String neighbor = borderParts[0].trim();
    
                // Try to extract and parse the distance if available
                int distance = 0;
                if (borderParts.length > 1) {
                    try {
                        String distanceStr = borderParts[borderParts.length - 1].replaceAll("[^0-9]", "");
                        if (!distanceStr.isEmpty()) {
                            distance = Integer.parseInt(distanceStr);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing distance in border data: " + border);
                        continue; // Skip this border entry if the distance cannot be parsed
                    }
                }
    
                String standardizedNeighbor = fixedCountries.getOrDefault(neighbor, neighbor);
                countryGraph.addCountry(standardizedNeighbor);
                if (distance > 0) {
                    countryGraph.addBorder(standardizedCountry, standardizedNeighbor, distance);
                }
            }
        }
    }
    
    

    private Map<String, String> createFixedCountries() {
        Map<String, String> fixedCountries = new HashMap<>();
        fixedCountries.put("German Federal Republic", "Germany");
        fixedCountries.put("Macedonia (Former Yugoslav Republic of)", "North Macedonia");
        fixedCountries.put("Bosnia-Herzegovina", "Bosnia and Herzegovina");
        fixedCountries.put("Bahamas", "The Bahamas");
        fixedCountries.put("Zambia", "Zambia");
        fixedCountries.put("US", "United States of America");
        fixedCountries.put("USA", "United States of America");
        fixedCountries.put("Burma", "Myanmar");
        fixedCountries.put("United States", "United States of America");
        fixedCountries.put("Greenland", "Greenland");
        fixedCountries.put("Congo, Democratic Republic of (Zaire)", "Democratic Republic of the Congo");
        fixedCountries.put("Congo, Democratic Republic of the", "Democratic Republic of the Congo");
        fixedCountries.put("Congo, Republic of the", "Republic of the Congo");
        fixedCountries.put("Gambia, The", "The Gambia");
        fixedCountries.put("Gambia", "The Gambia");
        fixedCountries.put("Macedonia", "North Macedonia");
        fixedCountries.put("Italy", "Italy");
        fixedCountries.put("East Timor", "Timor-Leste");
        fixedCountries.put("UK", "United Kingdom");
        fixedCountries.put("Korea, North", "North Korea");
        fixedCountries.put("Korea, People's Republic of", "North Korea");
        fixedCountries.put("Korea, South", "South Korea");
        return fixedCountries;
    }

    public int getDistance(String country1, String country2) {
        String standardizedCountry1 = fixedCountries.getOrDefault(country1, country1);
        String standardizedCountry2 = fixedCountries.getOrDefault(country2, country2);

        if (!countryGraph.hasCountry(standardizedCountry1) || !countryGraph.hasCountry(standardizedCountry2)) {
            return -1;
        }

        return countryGraph.getBorderLength(standardizedCountry1, standardizedCountry2);
    }

    public List<String> findPath(String country1, String country2) {
        String standardizedCountry1 = fixedCountries.getOrDefault(country1, country1);
        String standardizedCountry2 = fixedCountries.getOrDefault(country2, country2);

        return countryGraph.dijkstraPath(standardizedCountry1, standardizedCountry2);
    }

    public void acceptUserInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter the first country (or type 'EXIT' to quit): ");
            String country1 = scanner.nextLine().trim();

            if ("EXIT".equalsIgnoreCase(country1)) break;

            System.out.print("Enter the second country: ");
            String country2 = scanner.nextLine().trim();

            if ("EXIT".equalsIgnoreCase(country2)) break;

            int distance = getDistance(country1, country2);
            List<String> path = findPath(country1, country2);

            if (!path.isEmpty()) {
                System.out.println("Shortest path from " + country1 + " to " + country2 + ":");
                for (String step : path) {
                    System.out.println(step);
                }
            } else {
                System.out.println("No path found between " + country1 + " and " + country2);
            }

            if (distance >= 0) {
                System.out.println("Distance: " + distance + " km");
            } else {
                System.out.println("Distance information not available.");
            }
        }
        scanner.close();
    }


    public class Graph {
        private Map<String, Map<String, Integer>> adjacencyList;

        public Graph() {
            this.adjacencyList = new HashMap<>();
        }

        public void addCountry(String country) {
            adjacencyList.putIfAbsent(country, new HashMap<>());
        }

        public void addBorder(String country1, String country2, int distance) {
            adjacencyList.get(country1).put(country2, distance);
            adjacencyList.get(country2).put(country1, distance);
        }

        public boolean hasCountry(String country) {
            return adjacencyList.containsKey(country);
        }

        public int getBorderLength(String country1, String country2) {
            if (adjacencyList.containsKey(country1) && adjacencyList.get(country1).containsKey(country2)) {
                return adjacencyList.get(country1).get(country2);
            }
            return Integer.MAX_VALUE;
        }

        public List<String> dijkstraPath(String start, String end) {
            // Initialize Dijkstra's algorithm data structures
            Map<String, Integer> distances = new HashMap<>();
            Map<String, String> previous = new HashMap<>();
            PriorityQueue<Node> queue = new PriorityQueue<>();

            for (String vertex : adjacencyList.keySet()) {
                distances.put(vertex, vertex.equals(start) ? 0 : Integer.MAX_VALUE);
                queue.add(new Node(vertex, distances.get(vertex)));
                previous.put(vertex, null);
            }

            while (!queue.isEmpty()) {
                Node smallest = queue.poll();
                if (smallest.country.equals(end)) {
                    break;
                }

                if (distances.get(smallest.country) == Integer.MAX_VALUE) {
                    break;
                }

                for (Map.Entry<String, Integer> neighborEntry : adjacencyList.get(smallest.country).entrySet()) {
                    String neighbor = neighborEntry.getKey();
                    int alt = distances.get(smallest.country) + neighborEntry.getValue();
                    if (alt < distances.get(neighbor)) {
                        distances.put(neighbor, alt);
                        previous.put(neighbor, smallest.country);
                        queue.add(new Node(neighbor, alt));
                    }
                }
            }

            // Reconstruct the shortest path
            List<String> path = new ArrayList<>();
            for (String at = end; at != null; at = previous.get(at)) {
                path.add(at);
            }
            Collections.reverse(path);
            return path.isEmpty() || path.get(0).equals(start) ? path : new ArrayList<>();
        }

        private class Node implements Comparable<Node> {
            String country;
            int distance;

            Node(String country, int distance) {
                this.country = country;
                this.distance = distance;
            }

            @Override
            public int compareTo(Node other) {
                return Integer.compare(this.distance, other.distance);
            }
        }
    }

    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);
        a3.acceptUserInput();
    }

}





