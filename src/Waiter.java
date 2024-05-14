import java.util.concurrent.TimeUnit;

public class Waiter implements Runnable {

    static int qnt = 1;
    Thread t;
    String name;
    Restaurant restaurant;
    long operationTime = 2000;
    Visitor visitor;
    Dish dish;

    public Waiter(Restaurant restaurant) {
        name = "Официант " + qnt;
        t = new Thread(this, name);
        qnt++;
        t.setPriority(Thread.MAX_PRIORITY);
        this.restaurant = restaurant;
        System.out.println(name + " на работе!");
        t.start();
    }

    @Override
    public void run() {
        while (restaurant.isMoreVisitors) {
            if (!takeOrder()) {
                continue;
            }
            try {
                Thread.sleep(operationTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(operationTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!takeReadyDish()) {
                continue;
            }

            try {
                Thread.sleep(operationTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            serveReadyDish();
            try {
                Thread.sleep(operationTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean takeOrder() {
        restaurant.lock.lock();
        try {
            while (restaurant.orders.isEmpty()) {
                restaurant.isOrder.await(10, TimeUnit.SECONDS);
                if (restaurant.orders.isEmpty()) {
                    return false;
                }
            }
            visitor = restaurant.visitorsMakeOrder.pop();
            System.out.println(this.name + " взял заказ");
            dish = restaurant.orders.remove(visitor);
            restaurant.readyOrders.put(visitor, dish);
            restaurant.visitorsReadyOrder.add(visitor);
            restaurant.isReadyOrder.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (restaurant.lock.isHeldByCurrentThread()) {
                restaurant.lock.unlock();
            }
        }
        return true;
    }

    boolean takeReadyDish() {
        restaurant.lock.lock();
        try {
            while (restaurant.readyDishes.isEmpty()) {
                restaurant.isReady.await(5, TimeUnit.SECONDS);
                if (restaurant.readyDishes.isEmpty()) {
                    return false;
                }
            }
            visitor = restaurant.visitorsReadyToServe.pop();
            dish = restaurant.readyDishes.remove(visitor);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (restaurant.lock.isHeldByCurrentThread()) {
                restaurant.lock.unlock();
            }
        }
        return true;
    }

    void serveReadyDish() {
        visitor.vLock.lock();
        try {
            visitor.table.add(dish);
            visitor.isOnTable.signalAll();
            System.out.println(this.name + " несет заказ");
        } finally {
            if (visitor.vLock.isHeldByCurrentThread()) {
                visitor.vLock.unlock();
            }
        }
    }
}