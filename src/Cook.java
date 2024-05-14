import java.util.concurrent.TimeUnit;

public class Cook implements Runnable {
    Thread t;
    String name;
    Restaurant restaurant;
    long operationTime = 5000;
    Visitor visitor;
    Dish dish;

    public Cook(Restaurant restaurant) {
        name = "Повар ";
        t = new Thread(this, name);
        this.restaurant = restaurant;
        t.setPriority(Thread.MIN_PRIORITY);
        System.out.println(name + "на работе!");
        t.start();
    }

    @Override
    public void run() {
        while (restaurant.isMoreVisitors) {
            if (!startCooking()) {
                continue;
            }
            try {
                Thread.sleep(operationTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finishCooking();
        }
    }

    boolean startCooking() {
        restaurant.lock.lock();
        try {
            while (restaurant.readyOrders.isEmpty()) {
                restaurant.isReadyOrder.await(10, TimeUnit.SECONDS);
                if (restaurant.readyOrders.isEmpty()) {
                    return false;
                }
            }
            visitor = restaurant.visitorsReadyOrder.pop();
            System.out.println(name + " готовит блюдо");
            dish = restaurant.readyOrders.remove(visitor);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (restaurant.lock.isHeldByCurrentThread()) {
                restaurant.lock.unlock();
            }
        }
        return true;
    }

    void finishCooking() {
        restaurant.lock.lock();
        try {
            restaurant.readyDishes.put(visitor, dish);
            restaurant.visitorsReadyToServe.add(visitor);
            System.out.println(name + " закончил готовить");
            restaurant.isReady.signalAll();
        } finally {
            if (restaurant.lock.isHeldByCurrentThread()) {
                restaurant.lock.unlock();
            }
        }
    }
}