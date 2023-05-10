/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Homework.colony;

import Homework.ants.Ant;
import Homework.ants.ChildAnt;
import Homework.ants.SoldierAnt;
import Homework.ants.WorkerAnt;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author peete
 */
public class Colony {

    private Semaphore enter = new Semaphore(1);
    private Semaphore exit = new Semaphore(2);
    private EatingArea eatingArea = new EatingArea();
    private FoodStorageArea foodStorageArea = new FoodStorageArea();
    private RestArea restArea = new RestArea();
    private InstructionArea instructionArea = new InstructionArea();
    private Shelter shelter = new Shelter();
    private HashMap<Class<? extends Ant>, ArrayList<Ant>> ants = new HashMap<>();
    private ArrayList<Ant> antArrayList = new ArrayList<>();
    private HashSet<Ant> antHashSet = new HashSet<>();
    private ReadWriteLock antsSetLock = new ReentrantReadWriteLock();
    private boolean colonyIsUnderAttack = false;
    private Outside outside;

    public Colony() {
        ants.put(ChildAnt.class, new ArrayList<>());
        ants.put(SoldierAnt.class, new ArrayList<>());
        ants.put(WorkerAnt.class, new ArrayList<>());
    }


    /**
     * This method is for adding an ant to this Colony when the ant enters.
     * Method adds ant to sets and lists: ants, antsHashSet, antArrayList
     * A lock is used in this method so that there won't be any concurrency errors.
     * @param ant that is added to the colony.
     */
    public void addAnt(Ant ant) {
        antsSetLock.writeLock().lock();
        ArrayList<Ant> antTypeAnts = ants.get(ant.getClass());
        antTypeAnts.add(ant);
        ants.put(ant.getClass(), antTypeAnts);
        if (!antHashSet.contains(ant)) {
            antHashSet.add(ant);
            antArrayList.add(ant);
        }
        antsSetLock.writeLock().unlock();
        outside.removeAnt(ant);
    }

    /**
     * This method removes the ant that was added in addAnt method.
     A lock is used in this method so that there won't be any concurrency errors.
     * @param ant that is added to the colony.
     */
    public void removeAnt(Ant ant) {
        antsSetLock.writeLock().lock();
        ArrayList<Ant> antTypeAnts = ants.get(ant.getClass());
        antTypeAnts.remove(ant);
        ants.put(ant.getClass(), antTypeAnts);
        antsSetLock.writeLock().unlock();
        outside.addAnt(ant);
    }

    /**
     * Method is used when an ant enters the colony.
     * The method uses semaphore which acts as a limiter so no more than
     * 1 ant could enter the colony at the same time.
     * @param ant that enters the colony.
     */
    public void enter(Ant ant) throws InterruptedException {
        try {
            enter.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ant.threadSleep(100);
        addAnt(ant);
        enter.release(1);
    }

    /**
     * Method is used when ant exits the colony.
     * Has a semaphore that has 2 permits so that 2 ants can exit the colony at the same time.
     * @param ant that exits colony
     */
    public void exit(Ant ant) throws InterruptedException {
        try {
            exit.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ant.threadSleep(100);
        removeAnt(ant);
        exit.release(1);
    }

    /**
     * Next following methods are navigation methods that the ant uses to enter and exit areas.
     * This method is used when ant enters the eating area.
     * @param ant that goes to the area
     * @return the area instance, so it can be used inside the ant object
     */
    public EatingArea goToEatingArea(Ant ant) {
        eatingArea.addAnt(ant);
        return eatingArea;
    }

    /**
     * This method is used when ant exits the area.
     * @param ant that goes to EatingArea
     */
    public void leaveEatingArea(Ant ant) {
        eatingArea.removeAnt(ant);
    }

    public FoodStorageArea goToFoodStorageArea(Ant ant) {
        foodStorageArea.addAnt(ant);
        return foodStorageArea;
    }

    public void leaveFoodStorageArea(Ant ant) {
        foodStorageArea.removeAnt(ant);
    }

    public RestArea goToRestArea(Ant ant) {
        restArea.addAnt(ant);
        return restArea;
    }

    public void leaveRestArea(Ant ant) {
        restArea.removeAnt(ant);
    }

    public InstructionArea goToInstructionArea(Ant ant) {
        instructionArea.addAnt(ant);
        return instructionArea;
    }

    public void leaveInstructionArea(Ant ant) {
        instructionArea.removeAnt(ant);
    }

    public void goTorShelter(Ant ant) {
        this.shelter.addAnt(ant);
    }

    public void leaveShelter(Ant ant) {
        this.shelter.removeAnt(ant);
    }

    /**
     * The navigation methods here.
     */
    public FoodStorageArea getFoodStorageArea() {
        return foodStorageArea;
    }

    public RestArea getRestArea() {
        return restArea;
    }

    public InstructionArea getInstructionArea() {
        return instructionArea;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public EatingArea getEatingArea() {
        return eatingArea;
    }

    public HashMap<Class<? extends Ant>, ArrayList<Ant>> getAnts() {
        return ants;
    }

    /**
     * The colony is under attack so the colonyIsUnderAttack is set true
     */
    public void setColonyIsUnderAttackTrue() {
        colonyIsUnderAttack = true;
    }

    /**
     * When the colony isn't under attack anymore colonyIsUnderAttack is set false
     */
    public void setColonyIsUnderAttackFalse() {
        colonyIsUnderAttack = false;
    }

    /**
     * This is a helper method that helps to remove SoldierAnt when colony is under attack.
     * returns a boolean so that ant get info if colony is under attack.
     */
    public boolean isUnderAttack() {
        eatingArea.removeSoldierAnts();
        instructionArea.removeSoldierAnts();
        restArea.removeSoldierAnts();
        return colonyIsUnderAttack;
    }

    /**
     * Method is for the UI so that food amount can be displayed.
     */
    public int getFoodAtStorage() {
        return foodStorageArea.getFoodAmount();
    }

    public int getFoodAtEatingArea() {
        return eatingArea.getFoodAtEatingArea();
    }

    /**
     * Because Outside is inited after Colony the Outside class has to be set.
     */
    public void setOutside(Outside outside) {
        this.outside = outside;
    }

    /**
     * @return all soldier ants inside the colony.
     */
    public List<Ant> getSoldierAnts() {
        ArrayList<Ant> soldierAnts;
        antsSetLock.readLock().lock();
        soldierAnts = ants.get(SoldierAnt.class);
        antsSetLock.readLock().unlock();
        return soldierAnts;
    }

    /**
     * @return number of ants inside the colony.
     */
    public Integer getNrOfAntsInsideColony() {
        int nrOfAnts = 0;
        antsSetLock.readLock().lock();
        nrOfAnts +=  ants.get(SoldierAnt.class).size();
        nrOfAnts +=  ants.get(ChildAnt.class).size();
        nrOfAnts +=  ants.get(WorkerAnt.class).size();
        antsSetLock.readLock().unlock();
        return nrOfAnts;
    }
    
}
