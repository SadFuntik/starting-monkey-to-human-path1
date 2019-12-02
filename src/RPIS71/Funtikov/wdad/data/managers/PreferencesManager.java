package RPIS71.Funtikov.wdad.data.managers;

import RPIS71.Funtikov.wdad.utils.PreferencesManagerConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Properties;

public class PreferencesManager {

    private static final String FILE_PATH = "src/RPIS71/Funtikov/wdad/appconfig.xml";
    public static final PreferencesManager INSTANCE = new PreferencesManager();

    private static Document document;


    private void changeDocument() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "appconfig.dtd");
            transformer.transform(new DOMSource(document), new StreamResult(FILE_PATH));
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    public void setProperty(String key, String value) {
        Node propertyNode = getNode(key);
        if (propertyNode != null) {
            propertyNode.setTextContent(value);
            changeDocument();
        }
    }

    public String getProperty(String key) {
        return Objects.requireNonNull(getNode(key)).getTextContent();
    }

    public void setProperties(Properties prop) {
        prop.forEach((key, value) -> setProperty(key.toString(), value.toString()));
    }

    public Properties getProperties() {
        Properties properties = new Properties();
        Field[] fields = PreferencesManagerConstants.class.getFields();
        for (Field field : fields) {
            try {
                String fieldValue = String.valueOf(field.get(String.class));
                properties.setProperty(fieldValue, getProperty(fieldValue));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    public void addBindedObject(String name, String className) {
        Node serverElement = document.getElementsByTagName("server").item(0);
        Element bindedObjectElement = document.createElement("bindedobject");
        bindedObjectElement.setAttribute("name", name);
        bindedObjectElement.setAttribute("class", className);
        serverElement.appendChild(bindedObjectElement);
        changeDocument();
    }

    public void removeBindedObject(String name) {
        NodeList bindedObjects = document.getElementsByTagName("bindedobject");
        Element bindedObjectElement;
        for (int i = 0; i < bindedObjects.getLength(); i++) {
            bindedObjectElement = (Element) bindedObjects.item(i);
            if (name.equalsIgnoreCase(bindedObjectElement.getAttribute("name"))) {
                bindedObjectElement.getParentNode().removeChild(bindedObjectElement);
                break;
            }
        }
        changeDocument();
    }


    @Deprecated
    public String getCreateRegistry() {
        return Objects.requireNonNull(getTag("createregistry")).getTextContent();
    }

    @Deprecated
    public void setCreateRegistry(String newValue) {
        Objects.requireNonNull(getTag("createregistry")).setTextContent(newValue);
        changeDocument();
    }

    @Deprecated
    public String getRegistryAddress() {
        return Objects.requireNonNull(getTag("registryaddress")).getTextContent();
    }

    @Deprecated
    public void setRegistryAddress(String newValue) {
        Objects.requireNonNull(getTag("registryaddress")).setTextContent(newValue);
        changeDocument();
    }

    @Deprecated
    public String getRegistryPort() {
        return Objects.requireNonNull(getTag("registryport")).getTextContent();
    }

    @Deprecated
    public void setRegistryPort(String newValue) {
        Objects.requireNonNull(getTag("registryport")).setTextContent(newValue);
        changeDocument();
    }

    @Deprecated
    public String getPolicyPath() {
        return Objects.requireNonNull(getTag("policypath")).getTextContent();
    }

    @Deprecated
    public void setPolicyPath(String newValue) {
        Objects.requireNonNull(getTag("policypath")).setTextContent(newValue);
        changeDocument();
    }

    @Deprecated
    public String getUseCodeBaseOnly() {
        return Objects.requireNonNull(getTag("usecodebaseonly")).getTextContent();
    }

    @Deprecated
    public void setUseCodeBaseOnly(String newValue) {
        Objects.requireNonNull(getTag("usecodebaseonly")).setTextContent(newValue);
        changeDocument();
    }

    @Deprecated
    public String getClassProvider() {
        return Objects.requireNonNull(getTag("classprovider")).getTextContent();
    }

    @Deprecated
    public void setClassProvider(String newValue) {
        Objects.requireNonNull(getTag("classprovider")).setTextContent(newValue);
        changeDocument();
    }

    @Deprecated
    private Element getTag(String tagName) {
        return (Element) document.getElementsByTagName(tagName).item(0);
    }

    @Nullable
    private Node getNode(@NotNull String key) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression = key.replaceAll("\\.", "/");
            return (Node) xpath.evaluate(expression, document, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
