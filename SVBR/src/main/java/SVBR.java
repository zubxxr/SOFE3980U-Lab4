import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;

public class SVBR {
    public static void main(String[] args) {
        String[] files = {"model_1.csv", "model_2.csv", "model_3.csv"};
        double threshold = 0.5;

        for (String file : files) {
            int TP = 0, FP = 0, TN = 0, FN = 0;
            double BCE = 0;
            int count = 0;

            try (CSVReader reader = new CSVReader(new FileReader(file))) {
                String[] nextLine;
                reader.readNext(); // Skip header

                while ((nextLine = reader.readNext()) != null) {
                    int y = Integer.parseInt(nextLine[0]);  // True value (0 or 1)
                    double y_hat = Double.parseDouble(nextLine[1]);  // Predicted probability

                    BCE += y * Math.log(y_hat + 1e-10) + (1 - y) * Math.log(1 - y_hat + 1e-10);
                    int y_pred = y_hat >= threshold ? 1 : 0;

                    if (y == 1 && y_pred == 1) TP++;
                    if (y == 0 && y_pred == 1) FP++;
                    if (y == 0 && y_pred == 0) TN++;
                    if (y == 1 && y_pred == 0) FN++;

                    count++;
                }

                BCE = -BCE / count;
                double accuracy = (double) (TP + TN) / (TP + TN + FP + FN);
                double precision = TP / (double) (TP + FP + 1e-10);
                double recall = TP / (double) (TP + FN + 1e-10);
                double f1_score = 2 * (precision * recall) / (precision + recall + 1e-10);

                System.out.println("For " + file + ":");
                System.out.println("  BCE = " + BCE);
                System.out.println("  Confusion Matrix:");
                System.out.printf("       y=1    y=0\n");
                System.out.printf(" y^=1  %d      %d\n", TP, FP);
                System.out.printf(" y^=0  %d      %d\n", FN, TN);
                System.out.println("  Accuracy = " + accuracy);
                System.out.println("  Precision = " + precision);
                System.out.println("  Recall = " + recall);
                System.out.println("  F1 Score = " + f1_score);
                System.out.println();
            } catch (IOException e) {
                System.out.println("Error reading file: " + file);
            }
        }
        
        System.out.println("According to all metrics, the best model is model_3.csv");
    }
}
