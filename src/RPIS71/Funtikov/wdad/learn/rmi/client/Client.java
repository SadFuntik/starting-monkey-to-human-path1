package RPIS71.Funtikov.wdad.learn.rmi.client;

import RPIS71.Funtikov.wdad.data.managers.PreferencesManager;
import RPIS71.Funtikov.wdad.learn.rmi.XmlDataManager;
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

        Officiant officiant = new Officiant("Yan", "Ivanov");
        Officiant newOfficiant = new Officiant("Billy", "Petrov");
        LocalDate date;

        date = service.getLastOfficiantWorkDate(officiant);
        System.out.println(date);

        //Yan Ivanov -> Billy Petrov
        service.changeOfficiantName(officiant, newOfficiant);

        date = service.getLastOfficiantWorkDate(newOfficiant);
        System.out.println(date);

    }
}
