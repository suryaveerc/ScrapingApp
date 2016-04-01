/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analyze;

import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author suryaveer
 */
public class ParseXML {

    static int level = 1;


    public static HashMap<String, ArrayList<String>> parseXML(File file) {

        HashMap<String, ArrayList<String>> scrappedMap = null;
        //Map<String, Object> applicationKey = new HashMap<>();
        Document document = fetchXML(file);
        try {
            //scrappedMap = parseXMLToMap(clearEmptyNodes(document).getDocumentElement(), applicationKey, false);
            scrappedMap = parseXML(clearEmptyNodes(document).getDocumentElement());
            Log.d("Generated Map {}", scrappedMap.toString());
        } catch (XPathExpressionException ex) {
            Logger.getLogger(ParseXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return scrappedMap;
    }

    private static Document fetchXML(File file) {
        Document document = null;
        // String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Applications><app>Instagram<SEMrush_links>5520</SEMrush_links><Rating-Two>584,332</Rating-Two></app><app>Facebook<SEMrush_links>51520</SEMrush_links><Rating-Two>5184,332</Rating-Two></app></Applications>";
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
            document = docBuilder.parse(file);
            //Document document = docBuilder.parse( new InputSource( new StringReader( xmlString ) ) );


        } catch (ParserConfigurationException e) {
        } catch (SAXException | IOException ex) {
            Log.e("Exception parsing file", ex.toString());
        }
        return document;
    }

    private static Map<String, Object> parseXML(Node node, Map<String, Object> applicationKey, Boolean addToMap) {
        NodeList rootNodeList = node.getChildNodes();
        int len = rootNodeList.getLength();
        Log.d("Parent Node: ", node.getNodeName());
        Map<String, Object> applicationValuesMap = new HashMap<>();

        //Added check if this is called recursively do not add to map as the returned map will be added.
        if (addToMap) {
            applicationKey.put(node.getNodeName(), applicationValuesMap);
        } else {
            applicationValuesMap = applicationKey;
        }
        for (int i = 0; i < len; i++) {
            Node currentNode = rootNodeList.item(i);
            int currentNoteChildCount = currentNode.getChildNodes().getLength();
            //logger.debug("Child Node: {}, Children: {}", ((Element) currentNode).getTagName(), currentNoteChildCount);
            if (currentNoteChildCount == 1 && currentNode.getFirstChild().getNodeType() == 3)//checking for text node
            {
                applicationValuesMap.put(((Element) currentNode).getTagName(), ((Element) currentNode).getTextContent());
                //  logger.debug("Node: {}", ((Element) currentNode).getTagName());
                //  logger.debug("Value: {}", ((Element) currentNode).getTextContent());
            } else if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNoteChildCount > 0) {
                // logger.debug("Creating map for: {}", ((Element) currentNode).getTagName());
                Map<String, Object> subValues = new HashMap<>();

                applicationValuesMap.put(((Element) currentNode).getTagName(), parseXML(currentNode, subValues, false));
            }
        }
        return applicationKey;
    }

    private static HashMap<String, ArrayList<String>> parseXML(Node node) {
        HashMap<String, ArrayList<String>> scrappedData = new HashMap<>();
//        // Add names of applications in the list.
//        ArrayList<String> applicationNameList = new ArrayList<>();
        ArrayList<String> keysList = new ArrayList<>();
        Node levelOne = node.getFirstChild();
        NodeList rootNodeList = ((Element) node).getElementsByTagName(((Element) levelOne).getTagName());
        int totalApps = rootNodeList.getLength();
//        for (int i = 0; i < totalApps; i++) {
//            applicationNameList.add(rootNodeList.item(i).getFirstChild().getTextContent());
//        }
//        scrappedData.put("ApplicationName", applicationNameList);
//
        rootNodeList = levelOne.getChildNodes();
        int len = rootNodeList.getLength();
        //logger.debug("Parent Node: {}, Children: {}", rootNodeList.item(1).getNodeName(), len);

        //Added check if this is called recursively do not add to map as the returned map will be added.
        ArrayList<String> applicationValuesList = null;
        for (int i = 0; i < len; i++) {

            if (rootNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                applicationValuesList = new ArrayList<>();
                String dataPointName = ((Element) rootNodeList.item(i)).getTagName();
                NodeList childList = ((Element) node).getElementsByTagName(dataPointName);

                for (int j = 0; j < totalApps; j++) {
                    applicationValuesList.add(childList.item(j).getFirstChild().getTextContent());
                }
                keysList.add(dataPointName);
                scrappedData.put(dataPointName, applicationValuesList);
                //      logger.debug("Added Datapoint {} \n Value:{}",dataPointName,applicationValuesList);
            }

        }
        scrappedData.put("DataPoints", keysList);
        return scrappedData;
    }

    //Source http://j2stuff.blogspot.ca/2012/06/method-to-clear-empty-xml-nodes.html
    //Utility method to remove useless blank text nodes.
    public static Document clearEmptyNodes(Document doc) throws XPathExpressionException {
        XPathFactory xpathFactory = XPathFactory.newInstance();
        // XPath to find empty text nodes.
        XPathExpression xpathExp = xpathFactory.newXPath().compile(
                "//text()[normalize-space(.) = '']");
        NodeList emptyTextNodes = (NodeList) xpathExp.evaluate(doc, XPathConstants.NODESET);

        // Remove each empty text node from document.
        for (int i = 0; i < emptyTextNodes.getLength(); i++) {
            Node emptyTextNode = emptyTextNodes.item(i);
            emptyTextNode.getParentNode().removeChild(emptyTextNode);
        }
        return doc;
    }

    //Test function to print generated map.
}
