//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tp.util;

import java.io.InputStream;

import org.jaxen.JaxenException;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class ConfigManager {
    static Document pathxmldoc = null;

    public ConfigManager() {
    }

    public static Document getPathXmlDoc() {
        SAXBuilder builder = new SAXBuilder();
        InputStream ins = ConfigManager.class.getResourceAsStream("/config.xml");

        try {
            pathxmldoc = builder.build(ins);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return pathxmldoc;
    }

    public static String getItemValue(String textlabel) {
        if (pathxmldoc == null) {
            pathxmldoc = getPathXmlDoc();
        }

        String pathtext;

        pathtext = "";
        JDOMXPath xpath = null;

        try {
            xpath = new JDOMXPath("configuration/item/" + textlabel);
        } catch (JaxenException var6) {
            var6.printStackTrace();
        }

        Object obj = null;

        try {
            obj = xpath.selectSingleNode(pathxmldoc);
        } catch (JaxenException var5) {
            var5.printStackTrace();
        }

        if (obj != null) {
            Element elm = (Element)obj;
            pathtext = elm.getText();
        }

        return pathtext;
    }

}
