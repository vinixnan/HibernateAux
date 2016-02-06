/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Vinicius
 */
public class GeraXml {
    protected Document readFromFile(String url) {
        File fXmlFile = new File(url);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setCoalescing(true);
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.parse(fXmlFile);
        } catch (Exception ex) {
        }
        return null;
    }

    

    

    protected static String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag);
        if (nlList.getLength() > 0) {
            nlList = nlList.item(0).getChildNodes();
            if (nlList.getLength() > 0) {
                Node nValue = (Node) nlList.item(0);
                return nValue.getNodeValue();
            }
        }
        return new String();
    }
}
