import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;

public class MCC {
    public static void main(String[] args) {
        String file = "model.csv";
        int numClasses = 5;  // Classes range from 1 to 5
        int[][] confusionMatrix = new int[numClasses][numClasses];
        double CE = 0;
        int count = 0;

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] nextLine;
            reader.readNext(); // Skip header

            while ((nextLine = reader.readNext()) != null) {
                int y = Integer.parseInt(nextLine[0]) - 1;  // Actual class (adjusted for 0-indexing)
                double[] probabilities = new double[numClasses];

                for (int j = 0; j < numClasses; j++) {
                    probabilities[j] = Double.parseDouble(nextLine[j + 1]);
                }

                int predictedClass = 0;
                double maxProb = probabilities[0];

                for (int j = 1; j < numClasses; j++) {
                    if (probabilities[j] > maxProb) {
                        maxProb = probabilities[j];
                        predictedClass = j;
                    }
                }

                CE += Math.log(probabilities[y] + 1e-10);
                confusionMatrix[predictedClass][y]++;
                count++;
            }

            CE = -CE / count;

            System.out.println("Cross-Entropy Loss: " + CE);
            System.out.println("Confusion Matrix:");
            System.out.printf("       y=1    y=2    y=3    y=4    y=5\n");

            for (int i = 0; i < numClasses; i++) {
                System.out.printf(" y^=%d", (i + 1));
                for (int j = 0; j < numClasses; j++) {
                    System.out.printf("   %d", confusionMatrix[i][j]);
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + file);
        }
    }
}
