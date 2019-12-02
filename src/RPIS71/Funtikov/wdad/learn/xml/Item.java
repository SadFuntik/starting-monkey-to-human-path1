package RPIS71.Funtikov.wdad.learn.xml;

import java.io.Serializable;

public class Item implements Serializable {

    private static final String DEFAULT_NAME = "Something";

    private String name;
    private int cost;

    public Item(String name, int cost) {
        this.name = name.isEmpty() ? DEFAULT_NAME : name;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return String.format("%s for %d$", name, cost);
    }
}
