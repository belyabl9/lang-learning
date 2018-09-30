package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.service.SynonymService;
import lombok.NonNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides synonyms based on www.thesaurus.com
 */
@Service
public class ThesarusSynonymService implements SynonymService {
    private static final String THESARUS_BROWSE_URL = "http://www.thesaurus.com/browse";
    
    @Override
    public Set<String> findAll(@NonNull String word) {
        Document doc;
        try {
            doc = Jsoup.connect(THESARUS_BROWSE_URL + "/" + URLEncoder.encode(prepareWord(word), "UTF-8")).get();
        } catch (IOException e) {
            return Collections.emptySet();
        }
        Elements elements = doc.select(".synonyms-container h2:contains(Synonyms)").get(0)
                .parent()
                .parent()
                .select("li a");
        Set<String> synonyms = new HashSet<>();
        for (Element element : elements) {
            synonyms.add(element.text());
        }
        return synonyms;
    }
    
    @NotNull
    private String prepareWord(@NonNull String word) {
        for (String skipPart : Arrays.asList("to ")) {
            word = word.replace(skipPart, "");
        }
        return word;
    }

}
