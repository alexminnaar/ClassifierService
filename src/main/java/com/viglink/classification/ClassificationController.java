package com.viglink.classification;

import java.io.File;
import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassificationController {

    @RequestMapping("/forum-classification-url")
    public WebpageClassifier forumClassifierUrl(@RequestParam(value = "url") String name) throws IOException, ClassNotFoundException {
        return new WebpageClassifier(name, true, new File("src/main/resources/forum_notforum_clf.bin"));
    }

    @RequestMapping("/forum-classification-text")
    public WebpageClassifier forumClassifierText(@RequestParam(value = "text") String name) throws IOException, ClassNotFoundException {
        return new WebpageClassifier(name, false, new File("src/main/resources/forum_notforum_clf.bin"));
    }

    @RequestMapping("/merchant-publisher-classification-url")
    public WebpageClassifier merchantPublisherClassifierUrl(@RequestParam(value = "url") String name) throws IOException, ClassNotFoundException {
        return new WebpageClassifier(name, true, new File("src/main/resources/merchant_publisher_clf.bin"));
    }

    @RequestMapping("/merchant-publisher-classification-text")
    public WebpageClassifier merchantPublisherClassifierText(@RequestParam(value = "text") String name) throws IOException, ClassNotFoundException {
        return new WebpageClassifier(name, false, new File("src/main/resources/merchant_publisher_clf.bin"));
    }
}