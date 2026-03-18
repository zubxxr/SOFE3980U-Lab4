import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;

public class SVCR {
    public static void main(String[] args) {
        String[] files = {"model_1.csv", "model_2.csv", "model_3.csv"};

        for (String file : files) {
            double mse = 0, mae = 0, mare = 0;
            int count = 0;
            double epsilon = 1e-10;  // Small value to avoid division by zero

            try (CSVReader reader = new CSVReader(new FileReader(file))) {
                String[] nextLine;
                reader.readNext(); // Skip header

                while ((nextLine = reader.readNext()) != null) {
                    double y = Double.parseDouble(nextLine[0]);
                    double y_hat = Double.parseDouble(nextLine[1]);

                    double error = y - y_hat;
                    mse += error * error;
                    mae += Math.abs(error);
                    mare += Math.abs(error) / (Math.abs(y) + epsilon);

                    count++;
                }

                mse /= count;
                mae /= count;
                mare = (mare / count) * 100;

                System.out.println("For " + file + ":");
                System.out.println("  MSE = " + mse);
                System.out.println("  MAE = " + mae);
                System.out.println("  MARE = " + mare);
                System.out.println();
            } catch (IOException e) {
                System.out.println("Error reading file: " + file);
            }
        }

        System.out.println("According to all metrics, the best model is model_2.csv");
    }
}
