package com.viglink.classification;


import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.types.Instance;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;

public class MerchantPublisherClassification {

    private String url;
    private String prediction;
    private Classifier model;

    public MerchantPublisherClassification(String input) throws FileNotFoundException, IOException, ClassNotFoundException {
        this.url = input;

        try {
            //Read page text
            Document html = Jsoup.connect(this.url).get();
            String pageText = html.text();

            //import model
            File modelFile = new File("src/main/resources/merchant_publisher_clf.bin");
            ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream(modelFile));
            this.model = (Classifier) ois.readObject();
            ois.close();

            //make prediction
            Classification pred = this.model.classify(this.model.getInstancePipe().instanceFrom(new Instance(pageText, "", "", "")));
            String predString = pred.getLabeling().getBestLabel().toString();
            String predPrettyPrint = predString.substring(predString.lastIndexOf('/')+1);

            this.prediction = predPrettyPrint;

        } catch (HttpStatusException e) {
            this.prediction = "Error: could not crawl page";
        }
    }

    public String getInput() {
        return this.url;
    }

    public String getPrediction() {
        return this.prediction;
    }

}