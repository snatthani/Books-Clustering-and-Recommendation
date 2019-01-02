package com.miniproject;

import java.util.*;
import java.security.InvalidParameterException;

public class CorpusByWord
{
    private class DocIndex
    {
        private DocWordData _docPointer;
        private int _document;
        private int _index;
        private boolean _deleteSinceLastAdvance; 

        public DocIndex(int document) throws Exception
        {
            if ((document < 0) || (document > _corpusData.size()))
                throw new InvalidParameterException("Document " + document + " does not exist in data set");
            _document = document;
            _index = 0;
            _deleteSinceLastAdvance = false;
            _docPointer = _corpusData.get(document);
        }

        public String getWord() throws Exception
        {
            if ((!hasData()) || _deleteSinceLastAdvance)
                return null;
            else
                return _docPointer.getWordForIndex(_index);
        }

        public FrequencyByDoc getData() throws Exception
        {
            if ((!hasData()) || _deleteSinceLastAdvance)
                return new FrequencyByDoc(0.0, _document);
            else
                return new FrequencyByDoc(_docPointer.getData(_index),
                                          _document);
        }

        public void removeData() throws Exception
        {
            if (!_deleteSinceLastAdvance) {
                if (hasData())
                    _docPointer.removeData(_index);
                _deleteSinceLastAdvance = true;
            }
        }

        public void scaleData(double scaleAmt) throws Exception
        {
            if (hasData() && (!_deleteSinceLastAdvance))
                _docPointer.scaleData(_index, scaleAmt);
        }
        
        public boolean hasData()
        {
            return (_index < _docPointer.size());
        }

        public boolean isLastWord()
        {
            if (_deleteSinceLastAdvance)
                return (!hasData());
            else
                return ((_index + 1) == _docPointer.size());
        }

        public void advance()
        {
            if (_deleteSinceLastAdvance)
                _deleteSinceLastAdvance = false;
            else if (hasData())
                _index++;
        }

        public String toString()
        {
            try {
                if (_deleteSinceLastAdvance)
                    return "DocIndex:" + _document + " WordIndex:" + _index + " Data:DELETED";
                else
                    return "DocIndex:" + _document + " WordIndex:" + _index + " Data:" + getWord() + "-" + getData();
            }
            catch (Exception e) {
                return "DocIndex:" + _document + " WordIndex:" + _index + " Data:INVALID";
            }
        }
    }

    private ArrayList<DocWordData> _corpusData;
    private TreeMap<String, ArrayList<DocIndex>> _wordList;
    boolean _haveProcessedWord;

    public CorpusByWord(ArrayList<DocWordData> corpusData) throws Exception
    {
        _corpusData = corpusData;
        _wordList = new TreeMap<String, ArrayList<DocIndex>>();
        int index;
        for (index = 0; index < _corpusData.size(); index++)
            if (_corpusData.get(index) != null)
                insertDocIndex(new DocIndex(index));
        if (_wordList.isEmpty())
            throw new InvalidParameterException("Corpus to analyze " + _corpusData + " has no data");
        _haveProcessedWord = false;
    }

    private void insertDocIndex(DocIndex newIndex) throws Exception
    {
        if (newIndex.hasData()) {
            ArrayList<DocIndex> wantList = _wordList.get(newIndex.getWord());
            if (wantList == null) {
                wantList = new ArrayList<DocIndex>();
                _wordList.put(newIndex.getWord(), wantList);
            }
            wantList.add(newIndex);
        }
    }

    public boolean hasNext()
    {
        if ((_wordList.size() > 1) || (!_haveProcessedWord))
            return true;
        else if (_wordList.isEmpty())
            return false;
        else {
            Iterator<DocIndex> testIterator = _wordList.firstEntry().getValue().iterator();
            boolean lastWord = true;
            while (testIterator.hasNext() && lastWord) {
                DocIndex testIndex = testIterator.next();
                lastWord = (!testIndex.hasData()) || testIndex.isLastWord();
            }
            return (!lastWord);
        }
    }

    public ArrayList<FrequencyByDoc> nextWord() throws Exception
    {
        if (!_haveProcessedWord)
            _haveProcessedWord = true;
        else {
            Iterator<DocIndex> procIterator = _wordList.pollFirstEntry().getValue().iterator();
            while (procIterator.hasNext()) {
                DocIndex newIndex = procIterator.next();
                newIndex.advance();
                insertDocIndex(newIndex);
            }
            if (_wordList.isEmpty())
                throw new NoSuchElementException();
        }
        Iterator<DocIndex> dataIterator = _wordList.firstEntry().getValue().iterator();
        ArrayList<FrequencyByDoc> results = new ArrayList<FrequencyByDoc>();
        while (dataIterator.hasNext())
            results.add(dataIterator.next().getData());
        return results;
    }

    public String getProcessedWord()
    {
        if ((!_haveProcessedWord) || (_wordList.isEmpty()))
            return null;
        else
            return _wordList.firstKey();
    }

    public void deleteProcessedWord() throws Exception
    {
        if (_haveProcessedWord && (!_wordList.isEmpty())) {
            Iterator<DocIndex> dataIterator = _wordList.firstEntry().getValue().iterator();
            while (dataIterator.hasNext())
                dataIterator.next().removeData();
        }
    }
    
    public void scaleProcessedWord(double scaleValue) throws Exception
    {
        if (_haveProcessedWord && (!_wordList.isEmpty())) {
            Iterator<DocIndex> dataIterator = _wordList.firstEntry().getValue().iterator();
            while (dataIterator.hasNext())
                dataIterator.next().scaleData(scaleValue);
        }
    }

    public String toString()
    {
        return _wordList.toString();
    }
}
