import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Restaurant {

    public Map<Visitor, Dish> orders;
    public Map<Visitor, Dish> readyDishes;
    public Map<Visitor, Dish> readyOrders;
    public Deque<Visitor> visitors;
    public Deque<Visitor> visitorsMakeOrder;
    public Deque<Visitor> visitorsReadyOrder;
    public Deque<Visitor> visitorsReadyToServe;
    public boolean isMoreVisitors;

    public ReentrantLock lock;
    public Condition isOrder;
    public Condition isReadyOrder;
    public Condition isReady;
    public Condition isVisitors;

    public Restaurant() {
        orders = new HashMap<>();
        readyDishes = new HashMap<>();
        readyOrders = new HashMap<>();
        visitors = new ArrayDeque<>();
        visitorsReadyToServe = new ArrayDeque<>();
        visitorsReadyOrder = new ArrayDeque<>();
        visitorsMakeOrder = new ArrayDeque<>();
        lock = new ReentrantLock(true);
        isOrder = lock.newCondition();
        isReadyOrder = lock.newCondition();
        isReady = lock.newCondition();
        isVisitors = lock.newCondition();
        isMoreVisitors = true;
    }

    public boolean close() {
        if (!visitors.isEmpty()) {
            return false;
        } else {
            isMoreVisitors = false;
            return true;
        }
    }
}