package com.miniproject;

import java.io.*;
import java.util.*;

public final class DocumentReader
{
    private Stopwords _stopwords;
    private PorterStemmer _stemmer;
    
    public DocumentReader() throws Exception
    {
        _stopwords = Stopwords.getStopWords();
        _stemmer = PorterStemmer.getStemmer();
    }

    public HashMap<String, WordCounter> getWordFrequencies(String id, CassandraConnector client) throws Exception
    {
        HashMap<String, WordCounter> results = new HashMap<String, WordCounter>();
        String buffer = client.getDescriptionByTitle("Books",id);
        String hyphenate = null;
        boolean haveHyphenate = false;
        haveHyphenate = buffer.matches(".*[A-Za-z]-$");
        buffer = buffer.replaceAll("[^A-Za-z ]", " ").trim().replaceAll(" +", " ");
        buffer = buffer.toLowerCase();

        if (!buffer.isEmpty()) {
            String [] procWords = buffer.split(" ");
            if (hyphenate != null)
                procWords[0] = hyphenate + procWords[0];
            hyphenate = null;

            int index;
            for (index = 0; index < procWords.length; index++) {
                if (haveHyphenate && (index >= procWords.length - 1)) {
                    hyphenate = procWords[index];
                    haveHyphenate = false;
                }
                else if (!_stopwords.isStopWord(procWords[index])) {
                    if (!procWords[index].equals("s")) {
                        String stem = _stemmer.getStem(procWords[index]);
                        WordCounter count = results.get(stem);
                        if (count == null) {
                            count = new WordCounter();
                            results.put(stem, count);
                        }
                        count.increment();
                    }
                }
            }
        }
        return results;
    }
}
