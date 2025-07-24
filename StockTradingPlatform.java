import java.util.*;

class Stock {
    String symbol;
    String name;
    double price;

    Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }
}

class Transaction {
    String symbol;
    int quantity;
    double price;
    String type; // "BUY" or "SELL"

    Transaction(String symbol, int quantity, double price, String type) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }
}

class User {
    String username;
    double balance;
    Map<String, Integer> portfolio = new HashMap<>();
    List<Transaction> history = new ArrayList<>();

    User(String username, double balance) {
        this.username = username;
        this.balance = balance;
    }

    public void buyStock(Stock stock, int quantity) {
        double cost = stock.price * quantity;
        if (cost > balance) {
            System.out.println("❌ Not enough balance to buy.");
        } else {
            balance -= cost;
            portfolio.put(stock.symbol, portfolio.getOrDefault(stock.symbol, 0) + quantity);
            history.add(new Transaction(stock.symbol, quantity, stock.price, "BUY"));
            System.out.println("✅ Bought " + quantity + " shares of " + stock.symbol);
        }
    }

    public void sellStock(Stock stock, int quantity) {
        int owned = portfolio.getOrDefault(stock.symbol, 0);
        if (quantity > owned) {
            System.out.println("❌ Not enough shares to sell.");
        } else {
            balance += stock.price * quantity;
            portfolio.put(stock.symbol, owned - quantity);
            history.add(new Transaction(stock.symbol, quantity, stock.price, "SELL"));
            System.out.println("✅ Sold " + quantity + " shares of " + stock.symbol);
        }
    }

    public void showPortfolio(Map<String, Stock> market) {
        System.out.println("\n📊 Portfolio Summary:");
        double totalValue = balance;

        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            String symbol = entry.getKey();
            int qty = entry.getValue();
            double value = market.get(symbol).price * qty;
            System.out.printf(" - %s: %d shares × %.2f = %.2f\n", symbol, qty, market.get(symbol).price, value);
            totalValue += value;
        }

        System.out.printf("💰 Cash Balance: %.2f\n", balance);
        System.out.printf("📈 Total Portfolio Value: %.2f\n", totalValue);
    }

    public void showTransactionHistory() {
        System.out.println("\n📄 Transaction History:");
        for (Transaction t : history) {
            System.out.printf(" - %s %d of %s @ %.2f\n", t.type, t.quantity, t.symbol, t.price);
        }
    }
}

public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Sample market stocks
        Map<String, Stock> market = new HashMap<>();
        market.put("AAPL", new Stock("AAPL", "Apple Inc.", 180.50));
        market.put("GOOG", new Stock("GOOG", "Alphabet Inc.", 2700.00));
        market.put("TSLA", new Stock("TSLA", "Tesla Inc.", 900.00));
        market.put("AMZN", new Stock("AMZN", "Amazon Inc.", 3200.00));

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        User user = new User(username, 10000.0); // Starting with $10,000

        int choice;
        do {
            System.out.println("\n📈 Welcome to the Stock Trading Platform");
            System.out.println("1. View Market Stocks");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transaction History");
            System.out.println("6. Exit");
            System.out.print("Choose an option (1-6): ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n📰 Market Data:");
                    for (Stock stock : market.values()) {
                        System.out.printf(" - %s (%s): $%.2f\n", stock.symbol, stock.name, stock.price);
                    }
                    break;

                case 2:
                    System.out.print("Enter stock symbol to buy: ");
                    String buySymbol = scanner.next().toUpperCase();
                    if (!market.containsKey(buySymbol)) {
                        System.out.println("❌ Invalid symbol!");
                        break;
                    }
                    System.out.print("Enter quantity: ");
                    int buyQty = scanner.nextInt();
                    user.buyStock(market.get(buySymbol), buyQty);
                    break;

                case 3:
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSymbol = scanner.next().toUpperCase();
                    if (!market.containsKey(sellSymbol)) {
                        System.out.println("❌ Invalid symbol!");
                        break;
                    }
                    System.out.print("Enter quantity: ");
                    int sellQty = scanner.nextInt();
                    user.sellStock(market.get(sellSymbol), sellQty);
                    break;

                case 4:
                    user.showPortfolio(market);
                    break;

                case 5:
                    user.showTransactionHistory();
                    break;

                case 6:
                    System.out.println("👋 Exiting... Thank you!");
                    break;

                default:
                    System.out.println("❌ Invalid option.");
            }

        } while (choice != 6);
    }
}
