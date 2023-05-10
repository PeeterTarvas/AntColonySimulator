/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Homework.colony;

import Homework.ants.Ant;
import Homework.ants.SoldierAnt;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 *
 * @author peete
 */
public class RestArea {

    private ArrayList<Ant> ants = new ArrayList<>();
    private ReadWriteLock antsSetLock = new ReentrantReadWriteLock();

    /**
     * Add ant to the rest area.
     */
    public void addAnt(Ant ant) {
        antsSetLock.writeLock().lock();
        ants.add(ant);
        antsSetLock.writeLock().unlock();
    }

    /**
     * Remove ant from the rest area.
     */
    public void removeAnt(Ant ant) {
        antsSetLock.writeLock().lock();
        ants.remove(ant);
        antsSetLock.writeLock().unlock();
    }

    public ArrayList<Ant> getAnts() {
        return ants;
    }

    /**
     * Remove all the soldier ants form the eating area.
     * This method is used when an invasive insect attacks the colony and is called from the Colony class.
     */
    public void removeSoldierAnts() {
        antsSetLock.writeLock().lock();
        ArrayList<Ant> antsNew = new ArrayList<>();
        for (Ant ant: ants) {
            if (ant.getClass() != SoldierAnt.class) {
                antsNew.add(ant);
            }
        }
        ants = antsNew;
        antsSetLock.writeLock().unlock();
    }
}
