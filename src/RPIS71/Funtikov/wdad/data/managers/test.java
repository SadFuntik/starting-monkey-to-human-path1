package RPIS71.Funtikov.wdad.data.managers;

public class test {
    public static void main(String[] args) {
        PreferencesManager manager = PreferencesManager.INSTANCE;
        manager.addBindedObject("wow", "that's cool!");
        manager.addBindedObject("wew", "that's almost cool!");
        manager.addBindedObject("wuw", "that's not cool!");
        manager.removeBindedObject("wuw");
    }
}
