package RPIS71.Funtikov.wdad.learn.rmi;

import RPIS71.Funtikov.wdad.learn.rmi.XmlDataManager;
import RPIS71.Funtikov.wdad.learn.xml.*;

import java.time.LocalDate;
import java.util.List;

public class XmlDataManagerImpl implements XmlDataManager {

    private final XmlTask xmlTask = new XmlTask("src/RPIS71/Funtikov/wdad/learn/xml/poopnfork.xml");

    @Override
    public int earningsTotal(Officiant officiant, LocalDate date) throws NoSuchDayException {
        return xmlTask.earningsTotal(officiant.getSecondName(), date);
    }

    @Override
    public void removeDay(LocalDate date) throws NoSuchDayException {
        xmlTask.removeDay(date);
    }

    @Override
    public void changeOfficiantName(Officiant oldOfficiant, Officiant newOfficiant) {
        xmlTask.changeOfficiantName(oldOfficiant.getFirstName(), oldOfficiant.getSecondName(), newOfficiant.getFirstName(), newOfficiant.getSecondName());
    }

    @Override
    public List<Order> getOrders(LocalDate date) throws NoSuchDayException {
        return xmlTask.getOrders(date);
    }

    @Override
    public LocalDate getLastOfficiantWorkDate(Officiant officiant) throws NoSuchOfficiantException {
        return xmlTask.getLastOfficiantWorkDate(officiant.getFirstName(), officiant.getSecondName());
    }
}
