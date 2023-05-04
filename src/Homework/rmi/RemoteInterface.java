/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Homework.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author peete
 */
public interface RemoteInterface extends Remote {

    Integer nrOfAntsOutsideColony() throws RemoteException;

    Integer nrOfAntsInsideColony() throws RemoteException;

    Integer nrOfSoldierAntsDoingInstruction() throws RemoteException;

    Integer nrOfSoldierAntsRepellingInvasion() throws RemoteException;

    Integer nrOfBabyAntsAtEatingArea() throws RemoteException;

    Integer nrOfBabyAntsAtShelter() throws RemoteException;

    void generateThreat() throws RemoteException;

}
