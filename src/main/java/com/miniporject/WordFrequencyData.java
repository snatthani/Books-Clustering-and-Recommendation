package com.miniproject;

import java.util.*;
import java.security.InvalidParameterException;
public final class WordFrequencyData implements Comparable<WordFrequencyData>
{
    private String _word;
    private double _data;
    
    public WordFrequencyData(String word, double data) throws Exception
    {
        if (word == null)
            throw new InvalidParameterException("WordFrequencyData invalid, no word specified");
        else if (word.isEmpty())
            throw new InvalidParameterException("WordFrequencyData invalid, no word specified");
        else {
            _word = word;
            _data = data;
        }
    }
    public int compareTo(WordFrequencyData other)
    {
        return _word.compareTo(other._word);
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other == null)
            return false;
        else if (other == this)
            return true;
        else if (!(other instanceof WordFrequencyData))
            return false; 
        return (compareTo((WordFrequencyData)other) == 0);
    }
    
    public String getWord()
    {
        return _word;
    }
    
    public double getData()
    {
        return _data;
    }
    
    public String toString()
    {
        return _word + "=" + _data;
    }
} 


