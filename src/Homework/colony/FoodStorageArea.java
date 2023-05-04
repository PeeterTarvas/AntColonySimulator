package Homework.colony;

import Homework.ants.Ant;
import Homework.ants.WorkerAnt;
import Homework.utilities.MyLogger;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;
/**
 *
 * @author peete
 */
public class FoodStorageArea {

    private Semaphore foodStorageAreaLimit = new Semaphore(10);
    private Semaphore foodAtStorageArea = new Semaphore(0);
    private ReadWriteLock antsListLock = new ReentrantReadWriteLock();
    private ArrayList<Ant> ants = new ArrayList<>();
    private Logger logger = MyLogger.getMyLogger();

    public ArrayList<Ant> getAnts() {
        return ants;
    }

    public void addAnt(Ant ant) {
        try {
            foodStorageAreaLimit.acquire();
        } catch (InterruptedException e) {

        }
        antsListLock.writeLock().lock();
        ants.add(ant);
        antsListLock.writeLock().unlock();
    }

    public void removeAnt(Ant ant) {
        antsListLock.writeLock().lock();
        ants.remove(ant);
        antsListLock.writeLock().unlock();
        foodStorageAreaLimit.release();
    }

    public void placeFoodAtStorageArea(WorkerAnt workerAnt) throws InterruptedException {
        workerAnt.threadSleep(workerAnt.getRandomSleepTime(2000, 4000));
        foodAtStorageArea.release(workerAnt.giveFood());
        logger.info("Now food at storage: " + String.valueOf(foodAtStorageArea.availablePermits()));
    }

    public void getFoodFromStorageArea(WorkerAnt workerAnt) throws InterruptedException {
        workerAnt.threadSleep(workerAnt.getRandomSleepTime(1000, 2000));
        try {
            foodAtStorageArea.acquire(workerAnt.getFood(5));
        } catch (InterruptedException e) {
        }
        workerAnt.getFood(5);
        logger.info("Now food at storage: " + String.valueOf(foodAtStorageArea.availablePermits()));
    }

    public int getFoodAmount() {
        return foodAtStorageArea.availablePermits();
    }
}
