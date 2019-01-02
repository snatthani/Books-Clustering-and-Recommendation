package com.miniproject;

import java.util.*;
import java.security.InvalidParameterException;
public final class CorpusData
{
    private ArrayList<String> _files;
    private ArrayList<DocWordData> _wordMapping;

    public CorpusData(ArrayList<String> files, CassandraConnector client,double minSignificantValue) throws Exception
    {

        _files = files;
        _wordMapping = new ArrayList<DocWordData>(_files.size());

        int index = 0;
        DocumentReader reader = new DocumentReader();
      /*  System.out.println(_files.get(0));
        System.out.println(_files.get(1));
        System.out.println(_files.get(2));
        System.out.println(_files.get(3));
        System.out.println(_files.get(4));*/
        while (index < _files.size()) {
            DocWordData results = null;            
            try {
                Set<Map.Entry<String, WordCounter>> wordCounts = reader.getWordFrequencies(_files.get(index),client).entrySet();
                if (!wordCounts.isEmpty()) {
                    ArrayList<WordFrequencyData> tempResults = new ArrayList<WordFrequencyData>();
                    int highestCount = 0;
                    Iterator<Map.Entry<String, WordCounter>> count = wordCounts.iterator();
                    while (count.hasNext()) {
                        int currCount = count.next().getValue().getCount();
                        if (currCount > highestCount)
                            highestCount = currCount;
                    }

                    count = wordCounts.iterator();
                    while (count.hasNext()) {
                        Map.Entry<String, WordCounter> rawValue = count.next();
                        double termFrequency = 0.5 + (((double)rawValue.getValue().getCount() * 0.5) / (double)highestCount);
                        tempResults.add(new WordFrequencyData(rawValue.getKey(), termFrequency));
                    }
                    Collections.sort(tempResults);
                    results = new DocWordData();
                    Iterator<WordFrequencyData> tempIndex = tempResults.iterator();
                    while (tempIndex.hasNext())
                        results.addData(tempIndex.next());
                } 
            }
            catch (Exception e) {
                System.out.println("File " + _files.get(index) + " ignored due to error: " + e);
                results = null;
            }

            if (results != null) {
                _wordMapping.add(results);
                index++;
            }
            else
                _files.remove(index);
        } 

        CorpusByWord wordIndex = new CorpusByWord(_wordMapping);
        while (wordIndex.hasNext()) {
            ArrayList<FrequencyByDoc> wordData = wordIndex.nextWord();
            double invDocFreq = Math.log(((double)_wordMapping.size())/((double)wordData.size()));
            double avgTFIDF = 0.0;
            Iterator<FrequencyByDoc> wordTF = wordData.iterator();
            while (wordTF.hasNext())
                avgTFIDF += wordTF.next().getFrequency();
            avgTFIDF /= (double)wordData.size();
            avgTFIDF *= invDocFreq;
            
            if (avgTFIDF < minSignificantValue)
                wordIndex.deleteProcessedWord();
            else
                wordIndex.scaleProcessedWord(invDocFreq);
        }
    }

    public ArrayList<String> getFiles()
    {
        return _files;
    }

    public ArrayList<DocWordData> getWordMapping()
    {
        return _wordMapping;
    }

    public String toString()
    {
        StringBuilder result = new StringBuilder();
        int index;
        for (index = 0; index < _files.size(); index++) {
            try {
                result.append(_files.get(index));
            }
            catch (Exception e) {
                result.append("ERROR, FILE UNDEFINED");
            }
            result.append(": ");
            try {
                result.append(_wordMapping.get(index));
            }
            catch (Exception e) {
                result.append("ERROR, UNDEFINED");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
