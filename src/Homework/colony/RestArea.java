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

    ArrayList<Ant> ants = new ArrayList<>();

    private ReadWriteLock antsSetLock = new ReentrantReadWriteLock();

    public void addAnt(Ant ant) {
        antsSetLock.writeLock().lock();
        ants.add(ant);
        antsSetLock.writeLock().unlock();
    }

    public void removeAnt(Ant ant) {
        antsSetLock.writeLock().lock();
        ants.remove(ant);
        antsSetLock.writeLock().unlock();
    }

    public ArrayList<Ant> getAnts() {
        return ants;
    }

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
