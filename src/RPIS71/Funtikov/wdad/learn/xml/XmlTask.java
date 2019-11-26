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

public class XmlTask {

    private final String filePath;
    private final Document doc;

    XmlTask(String filePath) {
        this.filePath = filePath;
        this.doc = parseDocument();
    }

    int earningsTotal(String officiantSecondName, LocalDate date) {
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

    void removeDay(LocalDate date) {
        Element dateElement = getDate(date);
        dateElement.getParentNode().removeChild(dateElement);
        changeDocument(doc);
    }

    void changeOfficiantName(String oldFirstName, String oldSecondName, String newFirstName, String newSecondName) {
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

    private Element getDate(LocalDate searchingDate) {
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
        return null;
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