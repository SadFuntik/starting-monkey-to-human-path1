package RPIS71.Funtikov.wdad.data.managers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.Objects;

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

}
