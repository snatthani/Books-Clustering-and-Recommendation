package com.miniproject;

import java.util.*;
import java.security.InvalidParameterException;
import java.lang.IndexOutOfBoundsException;
public final class DocWordData
{
    private LinkedList<WordFrequencyData> _data;

    public DocWordData()
    {
        _data = new LinkedList<WordFrequencyData>();
    }

    public void addData(String word, double data) throws Exception
    {
        addData(new WordFrequencyData(word, data));
    }

    public void addData(WordFrequencyData newData)
    {
        if (_data.isEmpty())
            _data.add(newData);
        else if (_data.getLast().compareTo(newData) < 0)
            _data.addLast(newData);
        else {
            int index = 0;
            while ((index < _data.size()) &&
                   (_data.get(index).compareTo(newData) < 0))
                index++;
            if (index >= _data.size())
                _data.addLast(newData);
            else if (_data.get(index).compareTo(newData) == 0)
                _data.set(index, newData);
            else
                _data.add(index, newData);
        }
    }

    public double getData(String entry) throws Exception
    {
        int index = _data.indexOf(new WordFrequencyData(entry, 0.0));
        if (index < 0)
            return 0.0;
        else
            return _data.get(index).getData();
    }

    public double getData(int index) throws Exception
    {
        if ((index < 0) || (index >= _data.size()))
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds (" + 0 + " - " + (_data.size() - 1) + ")");
        return _data.get(index).getData();
    }

    public String getWordForIndex(int index) throws Exception
    {
        if ((index < 0) || (index >= _data.size()))
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds (" + 0 + " - " + (_data.size() - 1) + ")");
        return _data.get(index).getWord();
    }
    
    public void scaleData(int index, double scaleAmt) throws Exception
    {
        if ((index < 0) || (index >= _data.size()))
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds (" + 0 + " - " + (_data.size() - 1) + ")");
        WordFrequencyData oldData = _data.get(index);
        _data.set(index, new WordFrequencyData(oldData.getWord(),
                                               oldData.getData() * scaleAmt));
    }

    public void removeData(int index) throws Exception
    {
        if ((index < 0) || (index >= _data.size()))
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds (" + 0 + " - " + (_data.size() - 1) + ")");
        _data.remove(index);
    }

    public void removeData(String word) throws Exception
    {
        int index = _data.indexOf(new WordFrequencyData(word, 0.0));
        if (index >= 0)
            _data.remove(index);
    }

    public double findSimilarity(DocWordData other)
    {
        int index1 = 0;
        int index2 = 0;
        double result = 0.0;
        while ((index1 < _data.size()) && (index2 < other._data.size())) {
            int compare = _data.get(index1).compareTo(other._data.get(index2));
            if (compare < 0)
                index1++;
            else if (compare > 0)
                index2++;
            else { // Match
                result += (_data.get(index1).getData() *
                           other._data.get(index2).getData());
                index1++;
                index2++;
            }
        }
        return result;
    }

    public void normalize() throws Exception
    {
        double similarity = findSimilarity(this);
        similarity = Math.sqrt(similarity);
        similarity = 1.0 / similarity;
        int index;
        for (index = 0; index < size(); index++)
            scaleData(index, similarity);
    }

    public int size()
    {
        return _data.size();
    }
    
    public boolean isEmpty()
    {
        return _data.size() == 0;
    }

    public String toString()
    {
        return _data.toString();
    }

    public static void main(String[] args) throws Exception
    {
        DocWordData test = new DocWordData();
        test.addData("test1", 3.5);
        test.addData("test2", 2.6);
        test.addData("test4", 1.1);
        System.out.println(test);
        test.addData("test3", 5.1);
        System.out.println(test);

        System.out.println("Value for test3:" + test.getData("test3"));
        System.out.println("Value for foobar:" + test.getData("foobar"));
        System.out.println("Word for position 1:" + test.getWordForIndex(1));
        System.out.println("Value for position 0:" + test.getData(0));
        test.scaleData(1, -1.1);
        System.out.println(test);

        test.removeData("test2");
        test.removeData("foobar");
        test.removeData(0);
        System.out.println(test);

        DocWordData test2 = new DocWordData();
        test2.addData("test1", 0.6);
        test2.addData("test3", 0.8);

        DocWordData test3 = new DocWordData();
        test3.addData("test2", 0.92307692);
        test3.addData("test3", 0.38461538);

        System.out.println("Similarity is: " + test2.findSimilarity(test3));

        DocWordData test4 = new DocWordData();
        test4.addData("test6", 3.0);
        test4.addData("test8", 4.0);
        test4.normalize();
        System.out.println("Normalized doc data: " + test4);
    }
}
