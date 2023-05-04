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

    public void removeAnt(Ant ant) {
        antsSetLock.writeLock().lock();
        ArrayList<Ant> antTypeAnts = ants.get(ant.getClass());
        antTypeAnts.remove(ant);
        ants.put(ant.getClass(), antTypeAnts);
        antsSetLock.writeLock().unlock();
        outside.addAnt(ant);
    }

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


    public EatingArea goToEatingArea(Ant ant) {
        eatingArea.addAnt(ant);
        return eatingArea;
    }

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
    public HashMap<Class<? extends Ant>, ArrayList<Ant>> getAnts() {
        return ants;
    }


    public void setColonyIsUnderAttackTrue() {
        colonyIsUnderAttack = true;
    }

    public void setColonyIsUnderAttackFalse() {
        colonyIsUnderAttack = false;
    }

    public boolean isUnderAttack() {
        eatingArea.removeSoldierAnts();
        instructionArea.removeSoldierAnts();
        restArea.removeSoldierAnts();
        return colonyIsUnderAttack;
    }

    public int getFoodAtStorage() {
        return foodStorageArea.getFoodAmount();
    }

    public int getFoodAtEatingArea() {
        return eatingArea.getFoodAtEatingArea();
    }

    public void setOutside(Outside outside) {
        this.outside = outside;
    }

    public List<Ant> getSoldierAnts() {
        ArrayList<Ant> soldierAnts;
        antsSetLock.readLock().lock();
        soldierAnts = ants.get(SoldierAnt.class);
        antsSetLock.readLock().unlock();
        return soldierAnts;
    }

    public Integer getNrOfAntsInsideColony() {
        int nrOfAnts = 0;
        antsSetLock.readLock().lock();
        nrOfAnts +=  ants.get(SoldierAnt.class).size();
        nrOfAnts +=  ants.get(ChildAnt.class).size();
        nrOfAnts +=  ants.get(WorkerAnt.class).size();
        antsSetLock.readLock().unlock();
        return nrOfAnts;
    }

    public ArrayList<Ant> getAllAnts() {
        return antArrayList;
    }


}
