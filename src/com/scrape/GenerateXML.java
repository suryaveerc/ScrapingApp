package com.scrape;

/**
 * Created by chauh on 2016-03-31.
 */


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

//        import Analyze.ParseXML;
//       import aAnalyze.Trigger;

public class GenerateXML {
    private static Document doc;

    public static void writeXML(HashMap<String, ArrayList<String>> total) {
        try {
            ArrayList<String> applicationName = total.get("AppName");
            ArrayList<String> applicationRating = total.get("AppRating");
            ArrayList<String> applicationFiveStars = total.get("AppFiveStars");
            ArrayList<String> applicationScore = total.get("AppScore");

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("Apps");
            doc.appendChild(rootElement);

            for (int i = 0; i < applicationName.size(); i++) {

                System.out.println("*********************" + i);
                Element app = doc.createElement("app");
                app.appendChild(doc.createTextNode(applicationName.get(i)));
                rootElement.appendChild(app);


                Element appName = doc.createElement("appName");
                appName.appendChild(doc.createTextNode(applicationName.get(i)));
                app.appendChild(appName);

                Element appRating = doc.createElement("appRating");
                appRating.appendChild(doc.createTextNode(applicationRating.get(i)));
                app.appendChild(appRating);

                Element appFiveStars = doc.createElement("appFiveStars");
                appFiveStars.appendChild(doc.createTextNode(applicationFiveStars.get(i)));
                app.appendChild(appFiveStars);

                Element appScore = doc.createElement("appScore");
                appScore.appendChild(doc.createTextNode(applicationScore.get(i)));
                app.appendChild(appScore);

            }


        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }

    }

    public static void saveXML(String location) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            System.out.println("Saving at location: " + location);
            StreamResult result = new StreamResult(new File(location));

            transformer.transform(source, result);
            System.out.println("File saved");
            //ParseXML.readXML("C:\\Concordia\\SDM\\Assignment1\\sample.xml");
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(GenerateXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(GenerateXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

