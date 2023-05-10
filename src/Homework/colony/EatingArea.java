package Homework.colony;

import Homework.ants.Ant;
import Homework.ants.ChildAnt;
import Homework.ants.SoldierAnt;
import Homework.ants.WorkerAnt;
import Homework.utilities.MyLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;
/**
 * Eating area class, one thing to know is that food amount is stored as semaphore permits.
 * @author peete
 */
public class EatingArea {

    private Logger logger = MyLogger.getMyLogger();
    private Lock foodLock = new ReentrantLock();
    private HashMap<Class<? extends Ant>, ArrayList<Ant>> ants = new HashMap<>();
    private ReadWriteLock antsSetLock = new ReentrantReadWriteLock();
    private Semaphore foodAtEatingArea = new Semaphore(0);

    public EatingArea() {
        ants.put(ChildAnt.class, new ArrayList<>());
        ants.put(SoldierAnt.class, new ArrayList<>());
        ants.put(WorkerAnt.class, new ArrayList<>());
    }

    /**
     * @return the number of baby ants int the eating area.
     */
    public Integer nrOfBabyAntsAtEatingArea() {
        int size = 0;
        antsSetLock.readLock().lock();
        size += ants.get(ChildAnt.class).size();
        antsSetLock.readLock().unlock();
        return size;

    }

    /**
     * Add ant to the eating areas HashMap, lock is used so there would not be any conflicts.
     * @param ant that is added to the eating area
     */
    public void addAnt(Ant ant) {
        antsSetLock.writeLock().lock();
        if (ants.containsKey(ant.getClass())) {
            ArrayList<Ant> antTypeAnts = ants.get(ant.getClass());
            antTypeAnts.add(ant);
            ants.put(ant.getClass(), antTypeAnts);
        } else {
            ArrayList<Ant> antsSet = new ArrayList<>();
            antsSet.add(ant);
            ants.put(ant.getClass(), antsSet);
        }
        antsSetLock.writeLock().unlock();
    }

    /**
     * Remove ant from the eating area, a lock is used here as well so there would not be any conflicts.
     * @param ant that is removed from the eating area.
     */
    public void removeAnt(Ant ant) {
        antsSetLock.writeLock().lock();
        if (ants.containsKey(ant.getClass())) {
            ArrayList<Ant> antTypeAnts = ants.get(ant.getClass());
            antTypeAnts.remove(ant);
            ants.put(ant.getClass(), antTypeAnts);
        }
        antsSetLock.writeLock().unlock();
    }

    /**
     * Method where the worker ant places food to the eating area that is taken from the shelter area.
     * @param workerAnt that places the food.
     */
    public void placeFoodAtEatingArea(WorkerAnt workerAnt) {
        try {
            workerAnt.threadSleep(workerAnt.getRandomSleepTime(1000,2000));
            foodAtEatingArea.release(workerAnt.giveFood());
        } catch (InterruptedException e) {}
        logger.info("Now food at EATING AREA: " + foodAtEatingArea.availablePermits());
    }

    /**
     * Return consumable food that the ant can eat.
     * @param ant that gets the food
     */
    public void getConsumableFood(Ant ant) {
        try {
            foodAtEatingArea.acquire(ant.consumeFood());
            logger.info("Now food at EATING AREA: " + foodAtEatingArea.availablePermits());
        } catch (InterruptedException e) {
        }
    }

    /**
     * Remove all the soldier ants form the eating area.
     * This method is used when an invasive insect attacks the colony and is called from the Colony class.
     */
    public void removeSoldierAnts() {
        antsSetLock.writeLock().lock();
        ants.get(SoldierAnt.class).clear();
        antsSetLock.writeLock().unlock();
    }

    public HashMap<Class<? extends Ant>, ArrayList<Ant>> getAnts() {
        return ants;
    }

    public int getFoodAtEatingArea() {
        return foodAtEatingArea.availablePermits();
    }
}
