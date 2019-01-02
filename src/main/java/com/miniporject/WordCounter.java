package com.miniproject;

import java.util.*;

public final class WordCounter
{
    private int _value;

    public WordCounter()
    {
        _value = 0;
    }

    public void increment()
    {
        _value++;
    }

    public int getCount()
    {
        return _value;
    }

    public String toString()
    {
        return String.valueOf(_value);
    }
}