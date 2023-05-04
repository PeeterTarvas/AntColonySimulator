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

    public void addAnt(Ant ant) {
        antsListLock.writeLock().lock();
        ants.add(ant);
        antsListLock.writeLock().unlock();
    }

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
