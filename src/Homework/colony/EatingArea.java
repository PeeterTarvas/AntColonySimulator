package Homework.colony;

import Homework.ants.Ant;
import Homework.ants.ChildAnt;
import Homework.ants.SoldierAnt;
import Homework.ants.WorkerAnt;
import Homework.utilities.MyLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;
/**
 *
 * @author peete
 */
public class EatingArea {

    private Logger logger = MyLogger.getMyLogger();
    private HashMap<Class<? extends Ant>, ArrayList<Ant>> ants = new HashMap<>();
    private ReadWriteLock antsSetLock = new ReentrantReadWriteLock();
    private Semaphore foodAtEatingArea = new Semaphore(0);

    public EatingArea() {
        ants.put(ChildAnt.class, new ArrayList<>());
        ants.put(SoldierAnt.class, new ArrayList<>());
        ants.put(WorkerAnt.class, new ArrayList<>());
    }

    public Integer nrOfBabyAntsAtEatingArea() {
        int size = 0;
        antsSetLock.readLock().lock();
        size += ants.get(ChildAnt.class).size();
        antsSetLock.readLock().unlock();
        return size;

    }

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

    public void removeAnt(Ant ant) {
        antsSetLock.writeLock().lock();
        if (ants.containsKey(ant.getClass())) {
            ArrayList<Ant> antTypeAnts = ants.get(ant.getClass());
            antTypeAnts.remove(ant);
            ants.put(ant.getClass(), antTypeAnts);
        }
        antsSetLock.writeLock().unlock();
    }

    public void placeFoodAtEatingArea(WorkerAnt workerAnt) throws InterruptedException {
        workerAnt.threadSleep(workerAnt.getRandomSleepTime(1000,2000));
        foodAtEatingArea.release(workerAnt.giveFood());
        logger.info("Now food at EATING AREA: " + foodAtEatingArea.availablePermits());
    }

    public void getConsumableFood(Ant ant) {
        try {
            foodAtEatingArea.acquire(ant.consumeFood());
            logger.info("Now food at EATING AREA: " + foodAtEatingArea.availablePermits());
        } catch (InterruptedException e) {
        }
    }

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
