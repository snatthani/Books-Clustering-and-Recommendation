package com.miniproject;

import java.util.*;

public final class FrequencyByDoc
{
    private double _frequency;
    private int _document;

    public FrequencyByDoc(double frequency, int document)
    {
        _frequency = frequency;
        _document = document;
    }

    public double getFrequency()
    {
        return _frequency;
    }

    public int getDocument()
    {
        return _document;
    }

    public String toString()
    {
        return String.valueOf(_document) + ":" + String.valueOf(_frequency);
    }
}
