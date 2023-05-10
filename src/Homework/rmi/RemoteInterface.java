/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Homework.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for realizing the Server classes methods.
 * @author peete
 */
public interface RemoteInterface extends Remote {

    Integer getNrOfAntsOutsideColony() throws RemoteException;

    Integer getNrOfAntsInsideColony() throws RemoteException;

    Integer getNrOfSoldierAntsDoingInstruction() throws RemoteException;

    Integer getNrOfSoldierAntsRepellingInvasion() throws RemoteException;

    Integer getNrOfBabyAntsAtEatingArea() throws RemoteException;

    Integer getNrOfBabyAntsAtShelter() throws RemoteException;

    void generateThreat() throws RemoteException;

}
