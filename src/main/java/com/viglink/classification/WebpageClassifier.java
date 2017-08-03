package com.viglink.classification;


import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.types.Instance;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

public class WebpageClassifier {

    private String input;
    private Boolean isUrl;
    private String prediction;

    public WebpageClassifier(String input, Boolean isUrl, File serializedModel) throws FileNotFoundException, IOException, ClassNotFoundException {
        this.input = input;

        try {
            //load the classifier
            Classifier clf = loadModel(serializedModel);

            //if its a url, crawl then predict, if not just predict
            if (isUrl) {
                this.isUrl = true;
                String crawledContent = crawlUrl(this.input);
                this.prediction = predict(crawledContent, clf);
            } else {
                this.isUrl = false;
                this.prediction = predict(this.input, clf);
            }

        } catch (HttpStatusException he) {
            this.prediction = "Crawling Error: "+ he.getMessage();
        } catch (SocketTimeoutException se) {
            this.prediction = "Socket Timeout Exception: "+se.getMessage();
        } catch (MalformedURLException me) {
            this.prediction = "Malformed URL Exception: " + me.getMessage();
        }
    }

    public String crawlUrl(String url) throws IOException, MalformedURLException {
        //crawl the url
        Document htmlDoc = Jsoup.connect(url).get();
        //extract the page text
        return htmlDoc.text();
    }

    public String predict(String text, Classifier clf) {

        //make prediction
        Classification pred = clf.classify(clf.getInstancePipe().instanceFrom(new Instance(text, "", "", "")));
        String predString = pred.getLabeling().getBestLabel().toString();
        String predPrettyPrint = predString.substring(predString.lastIndexOf('/') + 1);

        return predPrettyPrint;
    }

    public Classifier loadModel(File serializedModel) throws IOException, ClassNotFoundException {

        ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(serializedModel));
        Classifier clf = (Classifier) ois.readObject();
        ois.close();

        return clf;
    }

    public String getInput() {
        return this.input;
    }

    public String getPrediction() {
        return this.prediction;
    }

    public Boolean getIsUrl() {
        return this.isUrl;
    }

}
