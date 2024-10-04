package assignment.pkg12;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ProductOperations {
    
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/product_info";
    private static final String USER = "root"; 
    private static final String PASSWORD = "";
    
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return connection;
    }

    public static void createProduct(Scanner scanner) {
        try (Connection connection = getConnection()) {
            System.out.print("Enter Product ID: ");
            int id = getIntInput(scanner);
            scanner.nextLine();
            System.out.print("Enter Product Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Product Category: ");
            String category = scanner.nextLine();
            System.out.print("Enter Product Price: ");
            double price = getDoubleInput(scanner);

            String insertQuery = "INSERT INTO product (id, name, category, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, category);
                preparedStatement.setDouble(4, price);
                int rowsInserted = preparedStatement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Product added successfully!");
                } else {
                    System.out.println("Failed to add product.");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }

    public static void readProduct(Scanner scanner) {
        try (Connection connection = getConnection()) {
            System.out.println("Search by (1) ID, (2) Name, (3) Category: ");
            int choice = getIntInput(scanner);
            String query = "";
            PreparedStatement preparedStatement = null;
            switch (choice) {
                case 1:
                    System.out.print("Enter Product ID: ");
                    int id = getIntInput(scanner);
                    query = "SELECT * FROM product WHERE id = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, id);
                    break;
                case 2:
                    System.out.print("Enter Product Name: ");
                    String name = scanner.nextLine();
                    query = "SELECT * FROM product WHERE name LIKE ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, "%" + name + "%");
                    break;
                case 3:
                    System.out.print("Enter Product Category: ");
                    String category = scanner.nextLine();
                    query = "SELECT * FROM product WHERE category = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, category);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                System.out.println("No products found.");
            } else {
                while (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("id"));
                    System.out.println("Name: " + resultSet.getString("name"));
                    System.out.println("Category: " + resultSet.getString("category"));
                    System.out.println("Price: " + resultSet.getDouble("price"));
                    System.out.println("------------------------");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }

    public static void updateProduct(Scanner scanner) {
        try (Connection connection = getConnection()) {
            System.out.print("Enter Product ID to update: ");
            int id = getIntInput(scanner);
            scanner.nextLine();
            System.out.print("Enter new Product Name (or leave empty to skip): ");
            String name = scanner.nextLine();
            System.out.print("Enter new Product Category (or leave empty to skip): ");
            String category = scanner.nextLine();
            System.out.print("Enter new Product Price (or -1 to skip): ");
            double price = getDoubleInput(scanner);

            String updateQuery = "UPDATE product SET name = COALESCE(?, name), category = COALESCE(?, category), price = COALESCE(?, price) WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, name.isEmpty() ? null : name);
                preparedStatement.setString(2, category.isEmpty() ? null : category);
                preparedStatement.setObject(3, price < 0 ? null : price);
                preparedStatement.setInt(4, id);

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Product updated successfully!");
                } else {
                    System.out.println("Product not found or update failed.");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }

    public static void deleteProduct(Scanner scanner) {
        try (Connection connection = getConnection()) {
            System.out.print("Enter Product ID to delete: ");
            int id = getIntInput(scanner);

            String deleteQuery = "DELETE FROM product WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, id);
                int rowsDeleted = preparedStatement.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Product deleted successfully!");
                } else {
                    System.out.println("Product not found or deletion failed.");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }

    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a valid integer.");
                scanner.next();
            }
        }
    }

    private static double getDoubleInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a valid number.");
                scanner.next();
            }
        }
    }
}
