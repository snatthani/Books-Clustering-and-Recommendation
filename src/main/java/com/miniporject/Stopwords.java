package com.miniproject;

import java.io.*;
import java.util.*;

public final class Stopwords
{
    private static Stopwords _words = null;
    private Set<String> _wordList = null;
    private BufferedReader _file;

    public static Stopwords getStopWords() throws Exception
    {
        if (_words == null) {
            try {
                _words = new Stopwords();
            }
            catch (Exception e) {
                _words = null;
                throw e;
            }
        }
        return _words;
    }

    public Stopwords() throws Exception
    {
        _wordList = new HashSet<String>();

        try {

            String fullName = new String("/home/snatthani/MiniProject/src/main/java/com/miniporject/stopwords.txt");

            _file = new BufferedReader(new FileReader(fullName));
            if (_file == null) { 
                System.out.println("No stop words file " + fullName + " found. Assuming none defined");
            }
            else {
                String [] newData = _file.readLine().split(",");
                if (newData.length <= 0)  
                    System.out.println("Stop words file " + fullName + " corrupt; no data");
                else {
                    int index;
                    for (index = 0; index < newData.length; index++)
                        _wordList.add(newData[index]);
                }
                _file.close();
                _file = null;
            }
        }
        catch (Exception e) {
            _wordList.clear();

            _file.close();
            _file = null;
            throw e;
        }
    }

    public void finailize()
    {
        if (_file != null) {
            System.out.println("WARNING: File not properly closed");
            try {
                _file.close();
            }
            catch (IOException e) {}
            _file = null;
        }
    }

    public String toString()
    {
        if (_wordList == null)
            return "NONE";
        else
            return Arrays.toString(_wordList.toArray());
    }

    public boolean isStopWord(String word)
    {
        if (_wordList == null)
            return false;
        else
            return (_wordList.contains(word));
    }

    public static void main(String[] args) throws Exception
    {
        try {
            Stopwords test = getStopWords();
            System.out.println(test);
            System.out.println("Test is a stopword:" + test.isStopWord("test"));
            System.out.println("The is a stopword:" + test.isStopWord("the"));
        }
        catch (Exception e) {
            System.out.println("Exception " + e + " caught");
            throw e;
        }
    }
}
