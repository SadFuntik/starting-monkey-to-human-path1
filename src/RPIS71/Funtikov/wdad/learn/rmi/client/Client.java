package RPIS71.Funtikov.wdad.learn.rmi.client;

import RPIS71.Funtikov.wdad.data.managers.PreferencesManager;
import RPIS71.Funtikov.wdad.learn.rmi.server.XmlDataManager;
import RPIS71.Funtikov.wdad.learn.xml.NoSuchDayException;
import RPIS71.Funtikov.wdad.learn.xml.NoSuchOfficiantException;
import RPIS71.Funtikov.wdad.learn.xml.Officiant;
import RPIS71.Funtikov.wdad.learn.xml.Order;
import RPIS71.Funtikov.wdad.utils.PreferencesManagerConstants;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.List;

public class Client {

    public static void main(String[] args) throws RemoteException, NoSuchDayException, NoSuchOfficiantException, NotBoundException {
        PreferencesManager manager = PreferencesManager.INSTANCE;

        final Registry registry = LocateRegistry.getRegistry(manager.getProperty(PreferencesManagerConstants.REGISTRY_ADDRESS),
                Integer.parseInt(manager.getProperty(PreferencesManagerConstants.REGISTRY_PORT)));

        final XmlDataManager service = (XmlDataManager) registry.lookup(PreferencesManagerConstants.BINDING_NAME);

        LocalDate now = LocalDate.now();
        Officiant officiant = new Officiant("Yan", "Ivanov");
        Officiant newOfficiant = new Officiant("Billy", "Petrov");
        LocalDate date;
        int total;

        List<Order> items = service.getOrders(now);
        items.forEach(System.out::println);

        //Yan Ivanov
        total = service.earningsTotal(officiant, now);
        System.out.println(total);

        date = service.getLastOfficiantWorkDate(officiant);
        System.out.println(date);

        //Yan Ivano -> Billy Petrov
        service.changeOfficiantName(officiant, newOfficiant);

        //Billy Petrov
        total = service.earningsTotal(newOfficiant, now);
        System.out.println(total);

        date = service.getLastOfficiantWorkDate(newOfficiant);
        System.out.println(date);

        //Remove day
        service.removeDay(now);
    }
}
