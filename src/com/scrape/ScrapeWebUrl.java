package com.scrape;

/**
 * Created by chauh on 2016-03-31.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

//import org.jsoup.select.Elements;

public class ScrapeWebUrl {

    public static void scrapeUrl(ArrayList<String> l) {
        Document doc;

        try {
            //System.out.println("url in Scrapeweburl is -->"+url );
            HashMap<String, ArrayList<String>> total = new HashMap<String, ArrayList<String>>();

            ArrayList<String> applicationName = new ArrayList<String>();
            ArrayList<String> applicationRating = new ArrayList<String>();
            ArrayList<String> applicationFiveStars = new ArrayList<String>();
            ArrayList<String> applicationScore = new ArrayList<String>();

            for (int i = 0; i < l.size(); i++) {
                String url = l.get(i);
                doc = Jsoup.connect(url).get();
                //Element div=doc.select("#offscreen-renderer").first();
                Element rating = doc.select("div.score").first();
                System.out.println("div to be printed : " + rating);
                System.out.println("final text is ---> " + rating.text());

                Element appName = doc.select("div.id-app-title").first();
                System.out.println("title for the app" + appName.text());

                Element fivestarrating = doc.select("div.rating-bar-container.five > span.bar-number").first();
            /*
			 * Element fourstarrating=doc.select("div.rating-bar-container.four > span.bar-number").first();
			Element threestarrating=doc.select("div.rating-bar-container.one > span.bar-number").first();
			Element twostarrating=doc.select("div.rating-bar-container.one > span.bar-number").first();
			Element onestarrating=doc.select("div.rating-bar-container.one > span.bar-number").first();
			*/
                Element score = doc.select("div.score").first();
                //Element updatedDate=doc.select("div.content").first();
                //Element installs=doc.select("[itemprop=numDownloads]").first();
                //Element review=doc.select("div.review-text").first();


                applicationName.add(appName.text());
                applicationRating.add(rating.text());
                applicationFiveStars.add(fivestarrating.text());
                applicationScore.add(score.text());

                total.put("AppName", applicationName);
                total.put("AppRating", applicationRating);
                total.put("AppFiveStars", applicationFiveStars);
                total.put("AppScore", applicationScore);


                //log.info("This is a sample statement added");

            }
            GenerateXML.writeXML(total);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}
