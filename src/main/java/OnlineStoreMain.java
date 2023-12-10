

import commands.Commands;
import exception.OutOfStockException;
import model.Order;
import model.Product;
import model.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument;
import storage.OrderStorage;
import storage.ProductStorage;
import storage.UserStorage;
import types.*;
import util.StorageSerializeUtil;
import util.UUIDUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class OnlineStoreMain implements Commands {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final ProductStorage PRODUCT_STORAGE = StorageSerializeUtil.deserializeProductStorage();
    private static final UserStorage USER_STORAGE = StorageSerializeUtil.deserializeUserStorage();
    private static final OrderStorage ORDER_STORAGE = StorageSerializeUtil.deserializeOrderStorage();
    public static User currentUser = null;

    public static void main(String[] args) {
        boolean isRun = true;
        while (isRun) {
            Commands.printLoginCommands();
            String command = SCANNER.nextLine();
            switch (command) {
                case EXIT: {
                    isRun = false;
                    break;
                }
                case LOGIN: {
                    loginUserAdmin();
                    break;
                }
                case REGISTER: {
                    registerUsers();
                    break;
                }
                default: {
                    System.out.println("WRONG COMMAND!!!");
                }
            }
        }

    }


    private static void userMenu() {

        boolean isRun = true;
        while (isRun) {
            Commands.printUserCommands();
            String command = SCANNER.nextLine();
            switch (command) {
                case LOGOUT: {
                    isRun = false;
                    currentUser = null;
                    break;
                }
                case PRINT_ALL_PRODUCTS: {
                    PRODUCT_STORAGE.printProducts();
                    break;
                }
                case BUY_PRODUCT: {
                    byProduct();
                    break;
                }
                case PRINT_MY_ORDERS: {
                    ORDER_STORAGE.printMyOrders(currentUser);
                    break;
                }
                case CANCEL_ORDER_BY_ID: {
                    cancelOrderById();
                    break;
                }
                default: {
                    System.out.println("WRONG COMMAND! TRY AGAIN!");
                    break;
                }
            }
        }
    }


    private static void adminMenu() {

        boolean isRun = true;
        while (isRun) {
            Commands.printAdminCommands();
            String commands = SCANNER.nextLine();
            switch (commands) {
                case LOGOUT: {
                    isRun = false;
                    break;
                }
                case ADD_PRODUCT: {
                    addProduct();
                    break;
                }
                case REMOVE_PRODUCT_BY_ID: {
                    removeProductByID();
                    break;
                }
                case PRINT_PRODUCTS: {
                    PRODUCT_STORAGE.printProducts();
                    break;
                }
                case PRINT_USERS: {
                    USER_STORAGE.printOnlyUsers();
                    break;
                }
                case PRINT_ORDERS: {
                    ORDER_STORAGE.printAllOrders();
                    break;
                }
                case CHANGE_ORDER_STATUS: {
                    changeOrderStatus();
                    break;
                }
                case EXPORT_ORDERS_TO_EXEL: {
                    exportOrdersToExel();
                }
                default: {
                    System.out.println("WRONG COMMAND! TRY AGAIN!");
                    break;
                }
            }
        }
    }

    private static void exportOrdersToExel() {
        System.out.println("please input path");
        String path = SCANNER.nextLine();
        File directory = new File(path);
        if (directory.exists() && directory.isDirectory()) {
            List<Order> orders = ORDER_STORAGE.getOrders();
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet ordersSheet = workbook.createSheet("orders");
                Row headRow = ordersSheet.createRow(0);
                Cell headIdCell = headRow.createCell(0);
                headIdCell.setCellValue("Order ID");

                Cell headUserNameCell = headRow.createCell(1);
                headUserNameCell.setCellValue("User Name");

                Cell headProductNameCell = headRow.createCell(2);
                headProductNameCell.setCellValue("Product name");

                Cell headOrderQtyCell = headRow.createCell(3);
                headOrderQtyCell.setCellValue("Qty");

                Cell headOrderPriceCell = headRow.createCell(4);
                headOrderPriceCell.setCellValue("Price");


                int rowIndex = 1;
                for (Order order : orders) {
                    Row row = ordersSheet.createRow(rowIndex++);

                    Cell idCell = row.createCell(0);
                    idCell.setCellValue(order.getId());

                    Cell userNameCell = row.createCell(1);
                    userNameCell.setCellValue(order.getUser().getName());

                    Cell productNameCell = row.createCell(2);
                    productNameCell.setCellValue(order.getProduct());

                    Cell productQtyCell = row.createCell(3);
                    productQtyCell.setCellValue(order.getQty());

                    Cell orderPriceCell = row.createCell(4);
                    orderPriceCell.setCellValue(order.getPrice());
                }
                workbook.write(new FileOutputStream(new File(directory, "report_" + System.currentTimeMillis() + ".xlsx")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Wrong path");
        }
    }

    private static void changeOrderStatus() {
        ORDER_STORAGE.printAllOrders();
        System.out.println("Please input order id you want to change");
        String orderId = SCANNER.nextLine();
        Order order = ORDER_STORAGE.getOrderByID(orderId);
        if (order == null) {
            System.out.println("Incorrect ID!!! Try again!");
            return;
        }
        System.out.println("Please input order status NEW, DELIVERED or CANCELED");
        String orderStatus = SCANNER.nextLine().toUpperCase();
        OrderStatus orderStatus1 = ORDER_STORAGE.getOrderStatusType(orderStatus);
        if (orderStatus1 == null) {
            System.out.println("You must input only NEW, DELIVERED or CANCELED");
            return;
        }
        if (orderStatus1 == OrderStatus.CANCELED) {
            order.setOrderStatus(orderStatus1);
            System.out.println("Order status changed as CANCELED");
            return;
        }
        if (orderStatus1 == OrderStatus.NEW) {
            order.setOrderStatus(orderStatus1);
            System.out.println("Order status changed as NEW");
            return;
        }
        if (orderStatus1 == OrderStatus.DELIVERED) {
            order.setOrderStatus(orderStatus1);
            String product = order.getProduct();
            Product product1 = PRODUCT_STORAGE.getProductById(product);
            int temp = product1.getStockQty() - order.getQty();
            product1.setStockQty(temp);
            StorageSerializeUtil.serializeOrderStorage(ORDER_STORAGE);

            System.out.println("Product DELIVERED");
        }

    }


    private static void cancelOrderById() {
        ORDER_STORAGE.printMyOrders(currentUser);
        System.out.println("Please input orderId  you want to CANCEL");
        String orderID = SCANNER.nextLine();
        Order order = ORDER_STORAGE.getOrderByID(orderID);
        if (order == null) {
            System.out.println("Incorrect order ID. Try again!!!");
            return;
        }
        order.setOrderStatus(OrderStatus.CANCELED);
        System.out.println("Order  is cancelled!");
        StorageSerializeUtil.serializeOrderStorage(ORDER_STORAGE);
    }


    private static void byProduct() {
        String id = UUIDUtil.generateUUID();
        PRODUCT_STORAGE.printProducts();
        System.out.println("Please input product id you want to BUY");
        String productId = SCANNER.nextLine();
        Product product = PRODUCT_STORAGE.getProductById(productId);
        if (product == null) {
            System.out.println("Product not found.Input correct product ID!!!");
            return;
        }
        try {
            System.out.println("Please input qty");
            int qty = Integer.parseInt(SCANNER.nextLine());
            if (qty <= 0) {
                System.out.println("Incorrect QTY!!!");
                return;
            }
            PRODUCT_STORAGE.getQty(productId, qty);
            System.out.println("Please input the payment method CARD, CASH OR PAYPAL");
            String type = SCANNER.nextLine().toUpperCase();
            PaymentMethod type1 = ORDER_STORAGE.getOrderPayType(type);
            if (type1 == null) {
                System.out.println("You must input only CARD, CASH OR PAYPAL!!!");
                return;
            }
            double price = PRODUCT_STORAGE.getPrice(productId);
            price = price * qty;

            System.out.println("do you want to buy this product in such " + qty +
                    " and at such a " + price + " ?");
            System.out.println("Input YES for confirm, input NO for CANCEL");
            String answerType = SCANNER.nextLine().toUpperCase();
            Answers answerType1 = ORDER_STORAGE.getAnswerType(answerType);
            if (answerType1 == null) {
                System.out.println("You must input only YES OR NO!!!");
                return;
            }
            if (answerType1 == Answers.YES) {
                Date date = new Date();
                Order order = new Order(id, currentUser, productId, date, price, OrderStatus.NEW, qty, type1);
                ORDER_STORAGE.add(order);
                System.out.println(order);
            }
            if (answerType1 == Answers.NO) {
                System.out.println("YOUR ORDER IS NOT REGISTERED!");
            }
        } catch (OutOfStockException e) {
            System.out.println(e.getMessage());

        } catch (NumberFormatException e) {
            System.out.println("Incorrect format for Qty!!!");
        }
    }

    private static void loginUserAdmin() {
        System.out.println("Please input your email");
        String email = SCANNER.nextLine();
        System.out.println("Please input your password");
        String password = SCANNER.nextLine();
        User user = USER_STORAGE.getUserEmailAndPassword(email, password);
        if (user == null) {
            System.out.println("Invalid email or password. Try again!");
            return;
        }
        if (user.getType() == UserType.ADMIN) {
            System.out.println("YOU LOGGED IN AS A ADMINISTRATOR");
            currentUser = user;
            adminMenu();
        }
        if (user.getType() == UserType.USER) {
            System.out.println("YOU LOGGED IN AS A USER");
            currentUser = user;
            userMenu();
        }
    }

    private static void removeProductByID() {
        PRODUCT_STORAGE.printProducts();
        System.out.println("Please input product ID for DELETE");
        String id = SCANNER.nextLine();
        PRODUCT_STORAGE.deleteProductByID(id);
        StorageSerializeUtil.serializeProductStorage(PRODUCT_STORAGE);
    }

    private static void addProduct() {
        System.out.println("Please input product id");
        String id = SCANNER.nextLine();
        Product productId = PRODUCT_STORAGE.getProductById(id);
        if (productId != null) {
            System.out.println("Product with this ID is already exists!");
            return;
        }
        System.out.println("Please input product name");
        String name = SCANNER.nextLine();
        System.out.println("Please input product description");
        String description = SCANNER.nextLine();

        try {
            System.out.println("Please input product price");
            double price = Double.parseDouble(SCANNER.nextLine());
            System.out.println("Please input product stockQty");
            int stockQty = Integer.parseInt(SCANNER.nextLine());
            System.out.println("Please input product type ELECTRONICS, CLOTHING OR BOOKS");
            String type = SCANNER.nextLine().toUpperCase();
            ProductType type1 = PRODUCT_STORAGE.getProductType(type);
            if (type1 == null) {
                System.out.println("You must input only ELECTRONICS, CLOTHING OR BOOKS!!!");
                return;
            }
            Product product = new Product(id, name, description, price, stockQty, type1);
            PRODUCT_STORAGE.add(product);
            System.out.println("Product created!");
        } catch (NumberFormatException e) {
            System.out.println("Incorrect format for Price/StockQty!!!");
        }
    }

    private static void registerUsers() {
        String id = UUIDUtil.generateUUID();
        User userId = USER_STORAGE.getUserById(id);
        {
            if (userId != null) {
                System.out.println("This ID is already registered");
                return;
            }
        }
        System.out.println("Please input email");
        String email = SCANNER.nextLine();
        User user1 = USER_STORAGE.getUserEmail(email);
        if (user1 != null) {
            System.out.println("User is already registered!");
            return;
        }
        System.out.println("Please create password");
        String password = SCANNER.nextLine();

        System.out.println("Please input User name");
        String name = SCANNER.nextLine();
        System.out.println("Please input type: ADMIN OR USER");
        String type = SCANNER.nextLine().toUpperCase();
        UserType type1 = USER_STORAGE.getUserAndAdminType(type);
        if (type1 == null) {
            System.out.println("Wrong type!!! Input only ADMIN or USER");
            return;
        }
        User user = new User(id, name, email, password, type1);
        USER_STORAGE.add(user);
        System.out.println("Your account created as " + type1);
    }
}
