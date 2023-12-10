package util;



import storage.OrderStorage;
import storage.ProductStorage;
import storage.UserStorage;

import java.io.*;

public class StorageSerializeUtil {

    private static final String USER_FILE_PATH = "C:\\Users\\Admin\\IdeaProjects\\online-store\\src\\main\\java\\data\\userStorage.dat";
    private static final String PRODUCT_FILE_PATH = "C:\\Users\\Admin\\IdeaProjects\\online-store\\src\\main\\java\\data\\productStorage.dat";
    private static final String ORDER_FILE_PATH = "C:\\Users\\Admin\\IdeaProjects\\online-store\\src\\main\\java\\data\\orderStorage.dat";

    public static void serializeUserStorage(UserStorage userStorage) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(USER_FILE_PATH))) {
            outputStream.writeObject(userStorage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UserStorage deserializeUserStorage() {
        File file = new File(USER_FILE_PATH);
        if (!file.exists()) {
            return new UserStorage();
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(USER_FILE_PATH))) {
            Object o = inputStream.readObject();
            if (o instanceof UserStorage userStorage) {
                return userStorage;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new UserStorage();
    }

    public static void serializeProductStorage(ProductStorage productStorage) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(PRODUCT_FILE_PATH))) {
            outputStream.writeObject(productStorage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ProductStorage deserializeProductStorage() {
        File file = new File(PRODUCT_FILE_PATH);
        if (!file.exists()) {
            return new ProductStorage();
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PRODUCT_FILE_PATH))) {
            Object object = inputStream.readObject();
            if (object instanceof ProductStorage productStorage) {
                return productStorage;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ProductStorage();
    }

    public static void serializeOrderStorage(OrderStorage orderStorage) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(ORDER_FILE_PATH))) {
            outputStream.writeObject(orderStorage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static OrderStorage deserializeOrderStorage() {
        File file = new File(ORDER_FILE_PATH);
        if (!file.exists()) {
            return new OrderStorage();
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(ORDER_FILE_PATH))) {
            Object object = inputStream.readObject();
            if (object instanceof OrderStorage orderStorage) {
                return orderStorage;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new OrderStorage();


    }
}