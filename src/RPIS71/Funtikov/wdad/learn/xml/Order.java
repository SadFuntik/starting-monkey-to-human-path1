package RPIS71.Funtikov.wdad.learn.xml;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

    private static final String DEFAULT_ORDER = "self catering";

    private Officiant officiant;
    private List<Item> items;

    public Order(Officiant officiant, List<Item> items) {
        this.officiant = officiant;
        this.items = items;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(officiant);
        builder.append("\nOrder: ");
        if (items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                builder.append("\n").append(i + 1).append(". ").append(items.get(i));
            }
        } else builder.append(DEFAULT_ORDER);
        builder.append("\n");
        return builder.toString();

    }
}
