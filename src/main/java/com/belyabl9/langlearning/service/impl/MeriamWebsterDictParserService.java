package com.belyabl9.langlearning.service.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A parser for Meriam-Webster dictionary
 */
@Service
public class MeriamWebsterDictParserService {
    public static final String DICT_BROWSE_URL = "https://www.merriam-webster.com/dictionary";

    public Set<String> findUsageExamples(String word) {
        Document doc;
        try {
            doc = Jsoup.connect(DICT_BROWSE_URL + "/" + URLEncoder.encode(prepareWord(word), "UTF-8")).get();
        } catch (IOException e) {
            return Collections.emptySet();
        }
        Elements elements = doc.select(".example_sentences .in-sentences .ex-sent");
        Set<String> examples = new HashSet<>();
        for (Element element : elements) {
            String text = StringUtils.capitalize(element.text());
            examples.add(text);
        }
        return examples;
    }
    
    private String prepareWord(String word) {
        for (String skipPart : Arrays.asList("to ")) {
            word = word.replace(skipPart, "");
        }
        return word;
    }

}
