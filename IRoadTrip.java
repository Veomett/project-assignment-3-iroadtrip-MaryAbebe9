import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IRoadTrip {

    private Map<String, Map<String, Integer>> bordersMap;

    // This function is the constructor of the class
    // It must read all the files and prepare to execute the implementation to respond to requests
    // It must halt on any failure here
    public IRoadTrip(String[] args) {
        bordersMap = new HashMap<>();
        // Read the borders file
        try (BufferedReader reader = new BufferedReader(new FileReader("borders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseAndStoreBorders(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Exit the program if there's an issue with file reading
            System.exit(1);
        }
        
        // Additional initialization can go here
    }
    
    private void parseAndStoreBorders(String line) {
        // Split the line at the "=" character to separate the country from its borders
        String[] parts = line.split(" = ");
        String country = parts[0].trim(); // The country name is the first part before "="
        Map<String, Integer> borderCountries = new HashMap<>();
    
        // Only proceed if there are borders listed (second part is not empty after "=")
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            // Get the borders string which contains neighboring countries and border lengths
            String borders = parts[1];
            // Find all occurrences of " km" to extract border information
            int lastIndex = 0;
            while (lastIndex != -1) {
                lastIndex = borders.indexOf(" km", lastIndex);
                if (lastIndex != -1) {
                    int start = borders.lastIndexOf(";", lastIndex) + 1; // Find the start of the country name
                    if (start < 0) start = 0; // If no ";" found, start is the beginning of the string
                    // Extract the substring containing the border country and length
                    String border = borders.substring(start, lastIndex).trim();
                    // Find the last space character, which separates the country name from the border length
                    int lastSpaceIndex = border.lastIndexOf(" ");
                    // Extract the border country name and border length
                    String borderCountry = border.substring(0, lastSpaceIndex);
                    int borderLength = Integer.parseInt(border.substring(lastSpaceIndex + 1));
                    // Put the border information in the map
                    borderCountries.put(borderCountry, borderLength);
                    // Move past the current " km" to find the next
                    lastIndex += " km".length();
                }
            }
        }
    
        // Put the country and its borders in the main map
        bordersMap.put(country, borderCountries);
    }
       // The parsing logic we discussed goes here (as provided in the previous message)
    }

    // This function provides the shortest path distance between the capitals of the two countries passed as arguments
    // per table 3 - if a country doesn't exist or if countries do not share a land border we return (-1)
    public int getDistance(String country1, String country2) {
        // Replace with your code
        return -1;
    }

    // This function determines and returns the shortest path between the two countries passed as the argument
    // starts with the capital of country 1 and ends in capital of country 2
    // the path therefore starts in country 1 and ends in country 2
    // if either of the countries does not exist or there is no path between the countries
    // - return an empty list (a list instance of size 0, not null list)
    public List<String> findPath(String country1, String country2) {
        // Replace with your code
        return null;
    }

    // Function - allows for user interaction on the console.
    // This will receive and validate the two countries the user enters.
    // After validation - we print out the path if there is one
    public void acceptUserInput() {
        // Replace with your code
        System.out.println("IRoadTrip - skeleton");
    }

    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);
        a3.acceptUserInput();
    }
}



