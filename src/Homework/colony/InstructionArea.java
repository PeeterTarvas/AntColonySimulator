package Homework.colony;

import Homework.ants.Ant;
import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 *
 * @author peete
 */
public class InstructionArea {

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

    /**
     * Remove all the soldier ants form the eating area.
     * This method is used when an invasive insect attacks the colony and is called from the Colony class.
     */
    public void removeSoldierAnts() {
        antsListLock.writeLock().lock();
        ants.clear();
        antsListLock.writeLock().unlock();
    }

    public Integer getNrOfAntsAtInstructionArea() {
        antsListLock.readLock().lock();
        Integer size = ants.size();
        antsListLock.readLock().unlock();
        return size;
    }
}
