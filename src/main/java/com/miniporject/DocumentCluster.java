package com.miniproject;

import java.util.*;
import java.security.InvalidParameterException;

public final class DocumentCluster
{
	private ArrayList<String> _files;
	private ArrayList<DocWordData> _wordMapping;
	private HashSet<String> _notClustered;

	public DocumentCluster(ArrayList<String> files,CassandraConnector client) throws Exception
	{
		CorpusData data = new CorpusData(files, client, 0.4);

		_wordMapping = data.getWordMapping();
		_files = data.getFiles();

		int minDocCount = _files.size() / 4;
		if (minDocCount < 1) 
			minDocCount = 1;

		CorpusByWord wordIndex = new CorpusByWord(_wordMapping);
		while (wordIndex.hasNext()) {
			if (wordIndex.nextWord().size() <= minDocCount)
				wordIndex.deleteProcessedWord();
		}

		_notClustered = new HashSet<String>();
		int index = 0;
		while (index < _wordMapping.size()) {
			if (_wordMapping.get(index).isEmpty()) {
				_wordMapping.remove(index);
				_notClustered.add(_files.get(index));
				_files.remove(index);
		    }
			else {
				_wordMapping.get(index).normalize();
				index++;
		    }
		}
	}

	public ArrayList<HashSet<String>> cluster(int clusterCount, boolean traceOutput) throws Exception
    {
		if (clusterCount > _files.size() / 2)
			clusterCount = _files.size() / 2;
		if (traceOutput) {
			System.out.println("Files:");
			System.out.println(_wordMapping);
			System.out.println("Clusters wanted: " + clusterCount);
		}

		ArrayList<HashSet<String>> result = new ArrayList<HashSet<String>>();
		if (clusterCount < 2)
			result.add(new HashSet<String>(_files));
		else {
			int[] clusterMapping = new int[_files.size()];
			ArrayList<DocWordData> currClusters = new ArrayList<DocWordData>();
			int index;
			for (index = 0; index < clusterCount; index++)
				currClusters.add(_wordMapping.get(index));
			int iteration = 0;
			boolean docMoved = true;
			while (docMoved && (iteration < 10)) {
	            docMoved = false;
	            for (index = 0; index < _wordMapping.size(); index++) {
	                int bestCluster = findClosestCluster(_wordMapping.get(index),
	                                                     currClusters);
	                if (bestCluster != clusterMapping[index]) {
	                    clusterMapping[index] = bestCluster;
	                    docMoved = true;
	                }
	            }

	            if (docMoved) {
	                for (index = 0; index < currClusters.size(); index++) {
	                    ArrayList<DocWordData> clusterDocs = new ArrayList<DocWordData>();
	                    int index2;
	                    for (index2 = 0; index2 < clusterMapping.length; index2++)
	                        if (clusterMapping[index2] == index)
	                            clusterDocs.add(_wordMapping.get(index2));
	                    if (!clusterDocs.isEmpty())
	                        currClusters.set(index, findCentroid(clusterDocs));
	                }
	            }
	            if (traceOutput) {
	                System.out.println("Current cluster assignments:");
	                System.out.println(Arrays.toString(clusterMapping));
	                System.out.println("Current cluster centroids:");
	                System.out.println(currClusters);
	            }
	            iteration++;
	        }
	    
	        int[] clusterPositions = new int[clusterCount];
	        Arrays.fill(clusterPositions, -1);
	        for (index = 0; index < clusterMapping.length; index++) {
	           
	            if (clusterPositions[clusterMapping[index]] == -1) {
	                clusterPositions[clusterMapping[index]] = result.size();
	                result.add(new HashSet<String>());
	            }
	            result.get(clusterPositions[clusterMapping[index]]).add(_files.get(index));
	        }
	    }
	    return result;
	}

    private int findClosestCluster(DocWordData testDoc, ArrayList<DocWordData> clusters) throws Exception
    {
        int index;
        int bestCluster = 0;
        double bestSimilarity = testDoc.findSimilarity(clusters.get(0));
        for (index = 1; index < clusters.size(); index++) {
            double newSimilarity = testDoc.findSimilarity(clusters.get(index));
            if (newSimilarity > bestSimilarity) {
                bestSimilarity = newSimilarity;
                bestCluster = index;
            } 
        } 
        return bestCluster;
    }
    
    
    private DocWordData findCentroid(ArrayList<DocWordData> documents) throws Exception
    {
    
        CorpusByWord index = new CorpusByWord(documents); 
        DocWordData result = new DocWordData();
        double docCount = (double)documents.size();
        while (index.hasNext()) {
            ArrayList<FrequencyByDoc> wordData = index.nextWord();
            int tempIndex;
            double sum = 0.0;
            for (tempIndex = 0; tempIndex < wordData.size(); tempIndex++)
                sum += wordData.get(tempIndex).getFrequency();
            result.addData(index.getProcessedWord(), sum / docCount);
        }
        return result;
    }

    public ArrayList<HashSet<String>> cluster(int clusterCount) throws Exception
    {
        return cluster(clusterCount, false);
    }

    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append("Files to cluster:\n");
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
        result.append("\n Not clusterable fles:");
        result.append(_notClustered.toString());
        return result.toString();
    }
}
