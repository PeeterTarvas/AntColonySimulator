/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Homework.colony;

import Homework.ants.Ant;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 *
 * @author peete
 */
public class Shelter {

    private ReadWriteLock antsListLock = new ReentrantReadWriteLock();
    private ArrayList<Ant> ants = new ArrayList<>();

    /**
     * Add ant to the shelter.
     */
    public void addAnt(Ant ant) {
        antsListLock.writeLock().lock();
        ants.add(ant);
        antsListLock.writeLock().unlock();
    }

    /**
     * Remove ant from the shelter.
     */
    public void removeAnt(Ant ant) {
        antsListLock.writeLock().lock();
        ants.remove(ant);
        antsListLock.writeLock().unlock();
    }

    public ArrayList<Ant> getAnts() {
        return ants;
    }

    public Integer nrOfChildAntsAtShelter() {
        int size = 0;
        antsListLock.readLock().lock();
        size += ants.size();
        antsListLock.readLock().unlock();
        return size;
    }
}
