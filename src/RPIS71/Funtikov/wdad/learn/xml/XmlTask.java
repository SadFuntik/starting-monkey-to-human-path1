package RPIS71.Funtikov.wdad.learn.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class XmlTask {

    private final String filePath;
    private final Document doc;

    public XmlTask(String filePath) {
        this.filePath = filePath;
        this.doc = parseDocument();
    }

    public int earningsTotal(String officiantSecondName, LocalDate date) throws NoSuchDayException {
        Element dateElement = getDate(date);
        NodeList orders = dateElement.getElementsByTagName("order");
        Element order, currentTotalCost, officiant;
        String thisSecondName;
        double totalCost = 0;
        for (int i = 0; i < orders.getLength(); i++) {
            order = (Element) orders.item(i);
            officiant = (Element) order.getElementsByTagName("officiant").item(0);
            if (officiant != null) {
                thisSecondName = officiant.getAttribute("secondname");
                if (officiantSecondName.equalsIgnoreCase(thisSecondName)) {
                    currentTotalCost = (Element) order.getLastChild();
                    if (currentTotalCost != null) {
                        totalCost += Double.parseDouble((currentTotalCost.getTextContent()));
                    } else {
                        totalCost += countTotalCost(order);
                    }
                }
            }
        }

        return (int) totalCost;
    }

    public void removeDay(LocalDate date) throws NoSuchDayException {
        Element dateElement = getDate(date);
        dateElement.getParentNode().removeChild(dateElement);
        changeDocument(doc);
    }

    public void changeOfficiantName(String oldFirstName, String oldSecondName, String newFirstName, String newSecondName) {
        NodeList officiants = doc.getElementsByTagName("officiant");
        Element officiantElement;
        for (int i = 0; i < officiants.getLength(); i++) {
            officiantElement = (Element) officiants.item(i);
            if (isThisOfficiant(officiantElement, oldFirstName, oldSecondName)) {
                officiantElement.setAttribute("firstname", newFirstName.toLowerCase());
                officiantElement.setAttribute("secondname", newSecondName.toLowerCase());
            }
        }
        changeDocument(doc);
    }

    public List<Order> getOrders(LocalDate date) throws NoSuchDayException {
        List<Order> orders = new ArrayList<>();
        Element dateElement = getDate(date);
        List<Item> items;
        Element currentElement;
        Officiant officiant;
        NodeList orderElements = dateElement.getChildNodes();
        for (int i = 0; i < orderElements.getLength(); i++) {
            currentElement = (Element) orderElements.item(i);
            officiant = getOfficiant(currentElement);
            items = getItems(currentElement);
            orders.add(new Order(officiant, items));
        }
        return orders;
    }

    public LocalDate getLastOfficiantWorkDate(String firstName, String secondName) throws NoSuchOfficiantException {
        NodeList officiants = doc.getElementsByTagName("officiant");
        Element officiantElement;
        Element dateElement;
        for (int i = officiants.getLength() - 1; i >= 0; i--) {
            officiantElement = (Element) officiants.item(i);
            if (isThisOfficiant(officiantElement, firstName, secondName)) {
                dateElement = (Element) officiantElement.getParentNode().getParentNode();
                return parseDate(dateElement);
            }
        }
        throw new NoSuchOfficiantException("Оффициант с таким именем и фамилией отсутствует");
    }

    private Document parseDocument() {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setIgnoringElementContentWhitespace(true);
            domFactory.setValidating(true);
            return domFactory.newDocumentBuilder().parse(new InputSource(filePath));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void changeDocument(Document doc) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "restaurant.dtd");
            transformer.transform(new DOMSource(doc), new StreamResult(filePath));
            System.out.println("XML file updated successfully");
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    private Element getDate(LocalDate searchingDate) throws NoSuchDayException {
        NodeList dates = doc.getElementsByTagName("date");
        Element dateElement;
        LocalDate thisDate;
        for (int i = 0; i < dates.getLength(); i++) {
            dateElement = (Element) dates.item(i);
            thisDate = parseDate(dateElement);
            if (searchingDate.isEqual(thisDate)) {
                return dateElement;
            }
        }
        throw new NoSuchDayException("Заданный день отсутствует.");
    }

    private Officiant getOfficiant(Element element) {
        Officiant officiant = new Officiant();
        Element officiantAttributes = (Element) element.getElementsByTagName("officiant").item(0);
        if (officiantAttributes != null) {
            officiant.setFirstName(officiantAttributes.getAttribute("firstname"));
            officiant.setSecondName(officiantAttributes.getAttribute("secondname"));
        }
        return officiant;
    }

    private List<Item> getItems(Element element) {
        List<Item> items = new ArrayList<>();
        NodeList itemsElements = element.getElementsByTagName("item");
        Element itemElement;
        for (int i = 0; i < itemsElements.getLength(); i++) {
            itemElement = (Element) itemsElements.item(i);
            if (itemElement != null) {
                items.add(new Item(itemElement.getAttribute("name"),
                        Integer.parseInt("0" + itemElement.getAttribute("cost"))));

            }
        }
        return items;
    }

    private boolean isThisOfficiant(Element officiant, String firstName, String secondName) {
        return firstName.equalsIgnoreCase(officiant.getAttribute("firstname")) &&
                secondName.equalsIgnoreCase(officiant.getAttribute("secondname"));
    }

    private LocalDate parseDate(Element dateTag) {
        return LocalDate.parse(String.format("%s-%s-%s",
                dateTag.getAttribute("year"),
                dateTag.getAttribute("month"),
                dateTag.getAttribute("day")));
    }

    private void addTotalCostTag(Element order, int totalCost) {
        Element totalCostElement = doc.createElement("totalcost");
        totalCostElement.appendChild(doc.createTextNode(String.valueOf(totalCost)));
        order.appendChild(totalCostElement);
        changeDocument(doc);
    }

    private int countTotalCost(Element order) {
        NodeList items = order.getElementsByTagName("item");
        int newTotalCost = 0;
        Element item;
        for (int j = 0; j < items.getLength(); j++) {
            item = (Element) items.item(j);
            newTotalCost += Integer.parseInt(item.getAttribute("cost"));
        }
        addTotalCostTag(order, newTotalCost);
        return newTotalCost;
    }

}