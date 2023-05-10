/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Homework.colony;

import Homework.ants.Ant;
import Homework.ants.WorkerAnt;
import Homework.utilities.MyLogger;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;
/**
 *
 * @author peete
 */
public class FoodStorageArea {

    private Semaphore foodStorageAreaLimit = new Semaphore(10);
    private Semaphore foodAtStorageArea = new Semaphore(0);
    private Lock foodLock = new ReentrantLock();
    private ReadWriteLock antsListLock = new ReentrantReadWriteLock();
    private ArrayList<Ant> ants = new ArrayList<>();
    private Logger logger = MyLogger.getMyLogger();
    public ArrayList<Ant> getAnts() {
        return ants;
    }

    /**
     * Add ant to the storage area.
     * Only 10 ants are allowed to be in the area, this is implemented by acquiring semaphores.
     */
    public void addAnt(Ant ant) {
        try {
            foodStorageAreaLimit.acquire();
        } catch (InterruptedException e) {

        }
        antsListLock.writeLock().lock();
        ants.add(ant);
        antsListLock.writeLock().unlock();
    }

    /**
     * Removes an ant from the storage area. Also releases one semaphore.
     */
    public void removeAnt(Ant ant) {
        antsListLock.writeLock().lock();
        ants.remove(ant);
        antsListLock.writeLock().unlock();
        foodStorageAreaLimit.release();
    }

    /**
     *  This method gets food that the ant has gathered and adds them to the storage.
     *  This is done by giving the semaphore permits from the ant.
     */
    public void placeFoodAtStorageArea(WorkerAnt workerAnt) {
        try {
            workerAnt.threadSleep(workerAnt.getRandomSleepTime(2000, 4000));
            foodAtStorageArea.release(workerAnt.giveFood());
        } catch (InterruptedException e) {

        }
        logger.info("Now food at storage: " + String.valueOf(foodAtStorageArea.availablePermits()));
    }

    /**
     * Get food from the storage area.
     * This is for the worker ants that move food between storage and eating area.
     */
    public void getFoodFromStorageArea(WorkerAnt workerAnt) throws InterruptedException {
        workerAnt.threadSleep(workerAnt.getRandomSleepTime(1000, 2000));
        try {
            workerAnt.threadSleep(workerAnt.getRandomSleepTime(1000, 2000));
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
