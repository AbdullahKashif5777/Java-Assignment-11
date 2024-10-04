package assignment.pkg12;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class JavaAssignment12 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            try {
                System.out.println("=== Product Management System ===");
                System.out.println("1. Add a new Product");
                System.out.println("2. View Product");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input, please enter a number between 1 and 5.");
                    scanner.next(); 
                    continue; 
                }
                
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        ProductOperations.createProduct(scanner);
                        break;
                    case 2:
                        ProductOperations.readProduct(scanner);
                        break;
                    case 3:
                        ProductOperations.updateProduct(scanner);
                        break;
                    case 4:
                        ProductOperations.deleteProduct(scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice, please select between 1 and 5.");
                }
            } catch (NoSuchElementException e) {
                System.out.println("Input stream has been closed unexpectedly.");
                break; 
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                scanner.next();
            }
        }
    }
}
