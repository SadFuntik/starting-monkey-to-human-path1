package RPIS71.Funtikov.wdad.learn.rmi.server;

import RPIS71.Funtikov.wdad.data.managers.PreferencesManager;
import RPIS71.Funtikov.wdad.learn.rmi.XmlDataManager;
import RPIS71.Funtikov.wdad.learn.rmi.XmlDataManagerImpl;
import RPIS71.Funtikov.wdad.utils.PreferencesManagerConstants;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {


    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        PreferencesManager manager = PreferencesManager.INSTANCE;
        final String registryProperty = manager.getProperty(PreferencesManagerConstants.CREATE_REGISTRY);
        if (registryProperty.equalsIgnoreCase("yes")) {

            final int port = Integer.parseInt(manager.getProperty(PreferencesManagerConstants.REGISTRY_PORT));
            final Registry registry = LocateRegistry.createRegistry(port);
            final XmlDataManagerImpl service = new XmlDataManagerImpl();
            final Remote stub = UnicastRemoteObject.exportObject(service, port);

            registry.bind(PreferencesManagerConstants.BINDING_NAME, stub);
            manager.addBindedObject(PreferencesManagerConstants.BINDING_NAME, XmlDataManager.class.getName());
        } else {
            throw new RemoteException("Registry is already created!");
        }

    }
}
