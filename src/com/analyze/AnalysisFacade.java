package com.analyze;

/**
 * Created by chauh on 2016-03-31.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.util.Map;

import com.models.ScrappedModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author chauh
 */
public class AnalysisFacade {

    private static final AnalysisFacade INSTANCE = new AnalysisFacade();
    private static HashMap<String, ArrayList<String>> scrappedMap;

    static {
        System.out.println("Initiaized****************************");
    }

    private String inputFileName;
    private String outputFileName;
    private HashMap<String, ArrayList<String>> analysisModel;
//    public AnalysisFacade(String inputFileName) {
//        this.inputFileName = inputFileName;
//        this.outputFileName = inputFileName.substring(0, inputFileName.indexOf(".")) + ".xml";
//    }

    //    static {
//        System.out.println();
//        AnalysisFacade instance = new AnalysisFacade();
//    }
    private ScrappedModel scrappedModel;

    private AnalysisFacade() {
    }

    public static AnalysisFacade getInstance() {

        return INSTANCE;
    }

    public String analyseData(String fileLocation) {
        inputFileName = fileLocation;
        analysisModel = ParseXML.parseXML(new File(inputFileName));
        scrappedModel = new ScrappedModel();
        scrappedModel.setScrappedData(analysisModel);
        scrappedModel.getKeys();
        return "Done";
    }

    public ArrayList<String> getAvailableKeys() {
        return scrappedModel.getKeys();
    }

    public HashMap<String, ArrayList<String>> getScrappedModel() {
        return scrappedModel.getScrappedData();
    }
}
