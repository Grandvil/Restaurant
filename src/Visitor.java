import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Visitor implements Runnable {

    static int qnt = 1;
    protected List<Dish> table = new ArrayList<>();
    ReentrantLock vLock = new ReentrantLock();
    Condition isOnTable = vLock.newCondition();
    Thread t;
    String name;
    Restaurant restaurant;
    long operationTime = 5000;

    public Visitor(Restaurant restaurant) {
        name = "Посетитель " + qnt;
        t = new Thread(this, name);
        qnt++;
        this.restaurant = restaurant;
        t.start();
    }

    @Override
    public void run() {
        System.out.println(name + " в ресторане!");
        enter();
        try {
            Thread.sleep(operationTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        toOrder();
        toEat();
        try {
            Thread.sleep(operationTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        toLeave();
        t.interrupt();
    }

    void enter() {
        restaurant.visitors.add(this);
    }

    Dish toOrder() {
        restaurant.lock.lock();
        try {
            Dish dish = new Dish();
            restaurant.orders.put(this, dish);
            restaurant.visitorsMakeOrder.add(this);
            restaurant.isOrder.signalAll();
            return dish;
        } finally {
            if (restaurant.lock.isHeldByCurrentThread()) {
                restaurant.lock.unlock();
            }
        }
    }

    void toEat() {
        vLock.lock();
        try {
            while (table.isEmpty()) {
                isOnTable.await();
                System.out.println(name + " приступил к еде");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (vLock.isHeldByCurrentThread()) {
                vLock.unlock();
            }
        }
    }

    void toLeave() {
        restaurant.lock.lock();
        try {
            restaurant.visitors.remove(this);
            System.out.println(name + " вышел из ресторана");
            restaurant.isVisitors.signalAll();
        } finally {
            if (restaurant.lock.isHeldByCurrentThread()) {
                restaurant.lock.unlock();
            }
        }
    }
}