package RPIS71.Funtikov.wdad.learn.rmi;

import RPIS71.Funtikov.wdad.learn.xml.NoSuchDayException;
import RPIS71.Funtikov.wdad.learn.xml.NoSuchOfficiantException;
import RPIS71.Funtikov.wdad.learn.xml.Officiant;
import RPIS71.Funtikov.wdad.learn.xml.Order;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface XmlDataManager extends Remote {

    int earningsTotal(Officiant officiant, LocalDate date) throws RemoteException, NoSuchDayException;

    void removeDay(LocalDate date) throws RemoteException, NoSuchDayException;

    void changeOfficiantName(Officiant oldOfficiant, Officiant newOfficiant) throws RemoteException;

    List<Order> getOrders(LocalDate date) throws RemoteException, NoSuchDayException;

    LocalDate getLastOfficiantWorkDate(Officiant officiant) throws RemoteException, NoSuchOfficiantException;
}
