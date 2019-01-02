package com.miniproject;

import java.util.*;
import java.util.regex.*;
import java.lang.IllegalArgumentException;
public final class PorterStemmer
{
    private static PorterStemmer _stemmer = null;

    Pattern _syllableStart = null;
    Pattern _ySyllableStart = null;
    Pattern _sses = null;
    Pattern _ies = null;
    Pattern _s = null;
    Pattern _eed = null;
    Pattern _ed = null;
    Pattern _yed = null;
    Pattern _ing = null;
    Pattern _ying = null;
    Pattern _at = null;
    Pattern _bl = null;
    Pattern _iz = null;
    Pattern _cvc = null;
    Pattern _cyc = null;
    Pattern _double = null;
    Pattern _y = null;
    Pattern _yy = null;
    Pattern _ational = null;
    Pattern _tional = null;
    Pattern _enci = null;
    Pattern _anci = null;
    Pattern _izer = null;
    Pattern _abli = null;
    Pattern _alli = null;
    Pattern _entli = null;
    Pattern _eli = null;
    Pattern _ousli = null;
    Pattern _ization = null;
    Pattern _ation = null;
    Pattern _ator = null;
    Pattern _alism = null;
    Pattern _iveness = null;
    Pattern _fulness = null;
    Pattern _ousness = null;
    Pattern _aliti = null;
    Pattern _iviti = null;
    Pattern _biliti = null;
    Pattern _icate = null;
    Pattern _ative = null;
    Pattern _alize = null;
    Pattern _iciti = null;
    Pattern _ical = null;
    Pattern _ful = null;
    Pattern _ness = null;
    Pattern _al = null;
    Pattern _ance = null;
    Pattern _ence = null;
    Pattern _er = null;
    Pattern _ic = null;
    Pattern _able = null;
    Pattern _ible = null;
    Pattern _ant = null;
    Pattern _ement = null;
    Pattern _ment = null;
    Pattern _ent = null;
    Pattern _sion = null;
    Pattern _ou = null;
    Pattern _ism = null;
    Pattern _ate = null;
    Pattern _iti = null;
    Pattern _ous = null;
    Pattern _ive = null;
    Pattern _ize = null;
    Pattern _e = null;
    Pattern _ll = null;
    Pattern _cvce = null;
    Pattern _cyce = null;
    
    public static PorterStemmer getStemmer() throws Exception
    {
        if (_stemmer == null) {
            try {
                _stemmer = new PorterStemmer();
            }
            catch (Exception e) {
                _stemmer = null;
                throw e;
            }
        }
        return _stemmer;
    }

    public PorterStemmer()
    {
        String letter = new String("[a-z]");
        String vowel = new String("aeiou"); 
        String anyVowel = new String("[" + vowel + "]");
        String consanant = new String("[" + letter + "&&[^" + vowel + "]]");

        String yConsanant = new String("[" + consanant + "&&[^y]]");
        String containsVowel = letter + "*" + anyVowel + letter + "*";
        String containsYvowel = letter + "*" + yConsanant + "y" + letter + "*";

        _syllableStart = Pattern.compile(anyVowel + consanant);
        _ySyllableStart = Pattern.compile(yConsanant + "y" + yConsanant);

        _sses = Pattern.compile("sses$");
        _ies = Pattern.compile("ies$");
        _s = Pattern.compile("([" + letter + "&&[^s]])s$");

        _eed = Pattern.compile("eed$");
        _ed = Pattern.compile("(" + containsVowel + ")ed$");
        _yed = Pattern.compile("(" + containsYvowel + ")ed$");
        _ing = Pattern.compile("(" + containsVowel + ")ing$");
        _ying = Pattern.compile("(" + containsYvowel + ")ing$");
        _y = Pattern.compile("(" + containsVowel + ")y$");
        _yy = Pattern.compile("(" + containsYvowel + ")y$");
        _at = Pattern.compile("at$");
        _bl = Pattern.compile("bl$");
        _iz = Pattern.compile("iz$");

        _cvc = Pattern.compile(consanant + anyVowel + "[" + consanant + "&&[^wxy]]");
        _cyc = Pattern.compile(yConsanant + "y[" + consanant + "&&[^wxy]]");
        
        _double = Pattern.compile("([" + consanant + "&&[^lsz]]){2,}");
       
        _ational = Pattern.compile("ational$");
        _tional = Pattern.compile("tional$");
        _enci = Pattern.compile("enci$");
        _anci = Pattern.compile("anci$");
        _izer = Pattern.compile("izer$");
        _abli = Pattern.compile("abli$");
        _alli = Pattern.compile("alli$");
        _entli = Pattern.compile("entli$");
        _eli = Pattern.compile("eli$");
        _ousli = Pattern.compile("ousli$");
        _ization = Pattern.compile("ization$");
        _ation = Pattern.compile("ation$");
        _ator = Pattern.compile("ator$");
        _alism = Pattern.compile("alism$");
        _iveness = Pattern.compile("iveness$");
        _fulness = Pattern.compile("fulness$");
        _ousness = Pattern.compile("ousness$");
        _aliti = Pattern.compile("aliti$");
        _iviti = Pattern.compile("iviti$");
        _biliti = Pattern.compile("biliti$");
        _icate = Pattern.compile("icate$");
        _ative = Pattern.compile("ative$");
        _alize = Pattern.compile("alize$");
        _iciti = Pattern.compile("iciti$");
        _ical = Pattern.compile("ical$");
        _ful = Pattern.compile("ful$");
        _ness = Pattern.compile("ness$");
        _al = Pattern.compile("al$");
        _ance = Pattern.compile("ance$");
        _ence = Pattern.compile("ence$");
        _er = Pattern.compile("er$");
        _ic = Pattern.compile("ic$");
        _able = Pattern.compile("able$");
        _ible = Pattern.compile("ible$");
        _ant = Pattern.compile("ant$");
        _ement = Pattern.compile("ement$");
        _ment = Pattern.compile("ment$");
        _ent = Pattern.compile("ent$");
        _sion = Pattern.compile("([st])ion$");
        _ou = Pattern.compile("ou$");
        _ism = Pattern.compile("ism$");
        _ate = Pattern.compile("ate$");
        _iti = Pattern.compile("iti$");
        _ous = Pattern.compile("ous$");
        _ive = Pattern.compile("ive$");
        _ize = Pattern.compile("ize$");

        _e = Pattern.compile("e$");
        _ll = Pattern.compile("ll$");
        _cvce = Pattern.compile(consanant + anyVowel + "[" + consanant + "&&[^wxy]]e$");
        _cyce = Pattern.compile(yConsanant + "y[" + consanant + "&&[^wxy]]e$");
    }

    private int[] getSyllables(String word) throws Exception
    {
        Matcher syllable = _syllableStart.matcher(word);
        Matcher ySyllable = _ySyllableStart.matcher(word);

        int[] result = new int[3];
        int resultCount = 0;
        boolean foundSyllable = syllable.find();
        boolean foundYsyllable = ySyllable.find();
        while ((resultCount < result.length) &&
               (foundSyllable || foundYsyllable)) {
            if (!foundYsyllable) {
                result[resultCount] = syllable.end() - 1;
                resultCount++;
                foundSyllable = syllable.find();
            }
            else if (!foundSyllable) {
                result[resultCount] = ySyllable.end() - 1;
                resultCount++;
                foundYsyllable = ySyllable.find();
            }
            
            else if (syllable.end() < ySyllable.end()) {
                result[resultCount] = syllable.end() - 1;
                resultCount++;
                foundSyllable = syllable.find();
            }
            else {
                result[resultCount] = ySyllable.end() - 1;
                resultCount++;
                foundYsyllable = ySyllable.find();
            }
        }
        return result;
    }

    private String replaceSuffix(String word, int reqStemLength, 
                                 ArrayList<Pattern> suffixes,
                                 ArrayList<String> replacements) throws Exception
    {
        if ((word == null) || (suffixes == null) || (replacements == null))
            throw new IllegalArgumentException();
        if (suffixes.size() != replacements.size())
            throw new IllegalArgumentException();

        Matcher matcher = suffixes.get(0).matcher(word);
        int index = 0;
        boolean haveMatch = false;
        while ((!haveMatch) && (index < suffixes.size())) {
            if (matcher.find())
                haveMatch = (matcher.start() > reqStemLength);
            if (!haveMatch) {
                index++;
                if (index < suffixes.size())
                    matcher.usePattern(suffixes.get(index));
            }
        }
        if (haveMatch) {
            StringBuffer result = new StringBuffer();
            matcher.appendReplacement(result, replacements.get(index));
            word = result.toString();
        }
        return word;
    }
            
    public String getStem(String word) throws Exception
    {

        int [] syllables = getSyllables(word);
        
        StringBuffer result = new StringBuffer();
        Matcher matcher = _sses.matcher(word);

        if (word.charAt(word.length() - 1) == 's') {
            if (matcher.find())
                matcher.appendReplacement(result, "ss");
            if (result.length() == 0) {
                matcher.usePattern(_ies);
                if (matcher.find())
                    matcher.appendReplacement(result, "i");
            }
            if (result.length() == 0) {
                matcher.usePattern(_s);
                if (matcher.find())
                    matcher.appendReplacement(result, "$1");
            }
        } 
        else {
            matcher.usePattern(_eed);
            if (matcher.find()) {
                if ((syllables[0] > 0) &&
                    (syllables[0] < matcher.start()))
                    matcher.appendReplacement(result, "ee");
            }
            else {
                boolean haveTense = false;
                matcher.usePattern(_ed);
                if (matcher.find())
                    haveTense = true;
                else {
                    matcher.reset();
                    matcher.usePattern(_yed);
                    if (matcher.find())
                        haveTense = true;
                    else {
                        matcher.reset();
                        matcher.usePattern(_ing);
                        if (matcher.find())
                            haveTense = true;
                        else {
                            matcher.reset();
                            matcher.usePattern(_ying);
                            if (matcher.find())
                                haveTense = true;
                        }
                    }
                }
                if (haveTense) {
                    matcher.appendReplacement(result, "$1");
                    word = result.toString();
                    result.setLength(0);

                    matcher = _at.matcher(word);
                    boolean needE = false;
                    if (matcher.find())
                        needE = true;
                    else {
                        matcher.usePattern(_bl);
                        if (matcher.find())
                            needE = true;
                        else {
                            matcher.usePattern(_iz);
                            if (matcher.find())
                                needE = true;
                            else if ((syllables[0] == (word.length() - 1)) &&
                                     ((syllables[1] == 0) ||
                                      (syllables[1] >= word.length()))) {
                                matcher.usePattern(_cvc);
                                if (matcher.find())
                                    needE = true;
                                else {
                                    matcher.usePattern(_cyc);
                                    needE = matcher.find();
                                }
                            }
                        }
                    }
                    if (needE) {
                        result.append(word);
                        result.append("e");
                    } 
                    else {
                        matcher.usePattern(_double);
                        if (matcher.find())
                            matcher.appendReplacement(result, "$1");
                    } 
                } 
            } 
        } 
        
        if (result.length() > 0) 
            word = result.toString();

        matcher = _y.matcher(word);
        boolean haveY = false;
        if (matcher.find())
            haveY = true;
        else {
            matcher.reset();
            matcher.usePattern(_yy);
            haveY = matcher.find();
        }
        if (haveY) {
            result.setLength(0);
            matcher.appendReplacement(result, "$1");
            result.append("i");
            word = result.toString();
        }

        if ((word.length() > 3) && (syllables[0] != 0)) {
            ArrayList<Pattern> suffixes = new ArrayList<Pattern>();
            ArrayList<String> replacements = new ArrayList<String>();
            
            switch (word.charAt(word.length() - 2)) {
            case 'a':
                suffixes.add(_ational);
                replacements.add(new String("ate"));
                suffixes.add(_tional);
                replacements.add(new String("tion"));
                break;
            case 'c':
                suffixes.add(_enci);
                replacements.add(new String("ence"));
                suffixes.add(_anci);
                replacements.add(new String("ance"));
                break;
            case 'e':
                suffixes.add(_izer);
                replacements.add(new String("ize"));
                break;
            case 'l':
                suffixes.add(_abli);
                replacements.add(new String("able"));
                suffixes.add(_alli);
                replacements.add(new String("al"));
                suffixes.add(_entli);
                replacements.add(new String("ent"));
                suffixes.add(_eli);
                replacements.add(new String("e"));
                suffixes.add(_ousli);
                replacements.add(new String("ous"));
                break;
            case 'o':
                suffixes.add(_ization);
                replacements.add(new String("ize"));
                suffixes.add(_ation);
                replacements.add(new String("ate"));
                suffixes.add(_ator);
                replacements.add(new String("ate"));
                break;
            case 's':
                suffixes.add(_alism);
                replacements.add(new String("al"));
                suffixes.add(_iveness);
                replacements.add(new String("ive"));
                suffixes.add(_fulness);
                replacements.add(new String("ful"));
                suffixes.add(_ousness);
                replacements.add(new String("ous"));
                break;

            case 't':
                suffixes.add(_aliti);
                replacements.add(new String("al"));
                suffixes.add(_iviti);
                replacements.add(new String("ive"));
                suffixes.add(_biliti);
                replacements.add(new String("ble"));
                break;

            default:
                break;
            }

            if (!suffixes.isEmpty())
                word = replaceSuffix(word, syllables[0], suffixes,
                                     replacements);
        }
        
        if ((word.length() > 2) && (syllables[0] != 0)) {
            ArrayList<Pattern> suffixes = new ArrayList<Pattern>();
            ArrayList<String> replacements = new ArrayList<String>();

            switch (word.charAt(word.length() - 1)) {
            case 'e':
                suffixes.add(_icate);
                replacements.add(new String("ic"));
                suffixes.add(_ative);
                replacements.add(new String(""));
                suffixes.add(_alize);
                replacements.add(new String("al"));
                break;
            case 'i':
                suffixes.add(_iciti);
                replacements.add(new String("ic"));
                break;
            case 'l':
                suffixes.add(_ical);
                replacements.add(new String("ic"));
                suffixes.add(_ful);
                replacements.add(new String(""));
                break;
            case 's':
                suffixes.add(_ness);
                replacements.add(new String(""));
                break;
            default:
                break;
            } 

            if (!suffixes.isEmpty())
                word = replaceSuffix(word, syllables[0], suffixes,
                                     replacements);
        } 
		if ((word.length() > 3) && (syllables[1] != 0)) {
            boolean ionMatch = false;
            if (word.charAt(word.length() - 2) == 'o') {
                matcher = _sion.matcher(word);
                if (matcher.find())
                    if (syllables[1] <= matcher.start()) {
                        result.setLength(0);
                        matcher.appendReplacement(result, "$1");
                        word = result.toString();
                        ionMatch = true;
                    }
            }
            if (!ionMatch) {
                ArrayList<Pattern> suffixes = new ArrayList<Pattern>();
                ArrayList<String> replacements = new ArrayList<String>();
                switch (word.charAt(word.length() - 2)) {
                case 'a':
                    suffixes.add(_al);
                    replacements.add(new String(""));
                    break;
                case 'c':
                    suffixes.add(_ance);
                    replacements.add(new String(""));
                    suffixes.add(_ence);
                    replacements.add(new String(""));
                    break;
                case 'e':
                    suffixes.add(_er);
                    replacements.add(new String(""));
                    break;
                case 'i':
                    suffixes.add(_ic);
                    replacements.add(new String(""));
                    break;
                case 'l':
                    suffixes.add(_able);
                    replacements.add(new String(""));
                    suffixes.add(_ible);
                    replacements.add(new String(""));
                    break;
                case 'n':
                    suffixes.add(_ant);
                    replacements.add(new String(""));
                    suffixes.add(_ement);
                    replacements.add(new String(""));
                    suffixes.add(_ment);
                    replacements.add(new String(""));
                    suffixes.add(_ent);
                    replacements.add(new String(""));
                    break;
                case 'o':
                    suffixes.add(_ou);
                    replacements.add(new String(""));
                    break;
                case 's':
                    suffixes.add(_ism);
                    replacements.add(new String(""));
                    break;
                case 't':
                    suffixes.add(_ate);
                    replacements.add(new String(""));
                    suffixes.add(_iti);
                    replacements.add(new String(""));
                    break;
                case 'u':
                    suffixes.add(_ous);
                    replacements.add(new String(""));
                    break;
                case 'v':
                    suffixes.add(_ive);
                    replacements.add(new String(""));
                    break;
                case 'z':
                    suffixes.add(_ize);
                    replacements.add(new String(""));
                    break;
                default:
                    break;
                } 

                if (!suffixes.isEmpty())
                    word = replaceSuffix(word, syllables[1], suffixes,
                                         replacements);
            } 
        }
        
        
        result.setLength(0);
        if ((syllables[1] > 0) && (syllables[1] < word.length())) {
            
            matcher = _e.matcher(word);
            if (matcher.find())
                matcher.appendReplacement(result, "");
            else {
                matcher.usePattern(_ll);
                if (matcher.find())
                    matcher.appendReplacement(result, "l");
            } 
        } 
        else if ((syllables[0] > 0) && (syllables[1] < word.length()) &&
                 (syllables[1] == 0)) {
            matcher = _e.matcher(word);
            if (matcher.find()) { 
                Matcher exlMatcher = _cvce.matcher(word);
                if (!exlMatcher.find()) {
                    exlMatcher.usePattern(_cyce);
                    if (!exlMatcher.find())
                        matcher.appendReplacement(result, "");
                } 
            } 
        }
       
        if (result.length() > 0)
            word = result.toString();
        return word;
    }

    public static void main(String[] args) throws Exception
    {
        try {
            PorterStemmer test = getStemmer();

            ArrayList<String> testWords = new ArrayList<String>();
            ArrayList<String> stemWords = new ArrayList<String>();
            testWords.add(new String("caresses"));
            stemWords.add(new String("caress"));
            testWords.add(new String("ponies"));
            stemWords.add(new String("poni"));
            testWords.add(new String("ties"));
            stemWords.add(new String("ti"));
            testWords.add(new String("caress"));
            stemWords.add(new String("caress"));
            testWords.add(new String("cats"));
            stemWords.add(new String("cat"));
            
            testWords.add(new String("syllables"));
            stemWords.add(new String("syllabl"));
            testWords.add(new String("feed"));
            stemWords.add(new String("feed"));
            testWords.add(new String("agreed"));
            stemWords.add(new String("agree"));
            testWords.add(new String("plastered"));
            stemWords.add(new String("plaster"));
            testWords.add(new String("bled"));
            stemWords.add(new String("bled"));
            
            testWords.add(new String("sing"));
            stemWords.add(new String("sing"));
            testWords.add(new String("flying"));
            stemWords.add(new String("fly"));
            testWords.add(new String("conflated"));
            stemWords.add(new String("conflat"));
            testWords.add(new String("troubled"));
            stemWords.add(new String("trouble"));
            testWords.add(new String("sized"));
            stemWords.add(new String("size"));

            testWords.add(new String("hopping"));
            stemWords.add(new String("hop"));
            testWords.add(new String("falling"));
            stemWords.add(new String("fall"));
            testWords.add(new String("hissing"));
            stemWords.add(new String("hiss"));
            testWords.add(new String("failing"));
            stemWords.add(new String("fail"));
            testWords.add(new String("filing"));
            stemWords.add(new String("file"));

            testWords.add(new String("sky"));
            stemWords.add(new String("sky"));
            testWords.add(new String("relational"));
            stemWords.add(new String("relat"));
            testWords.add(new String("conditional"));
            stemWords.add(new String("condit"));
            testWords.add(new String("rational"));
            stemWords.add(new String("ration"));
            testWords.add(new String("valency"));
            stemWords.add(new String("valenc"));

            testWords.add(new String("digitizer"));
            stemWords.add(new String("digit"));
            testWords.add(new String("conformably"));
            stemWords.add(new String("conform"));
            testWords.add(new String("differently"));
            stemWords.add(new String("differ"));
            testWords.add(new String("analogously"));
            stemWords.add(new String("analog"));
            testWords.add(new String("authorization"));
            stemWords.add(new String("author"));

            testWords.add(new String("predication"));
            stemWords.add(new String("predic"));
            testWords.add(new String("operator"));
            stemWords.add(new String("oper"));
            testWords.add(new String("feudalism"));
            stemWords.add(new String("feudal"));
            testWords.add(new String("decisiveness"));
            stemWords.add(new String("decis"));
            testWords.add(new String("hopefulness"));
            stemWords.add(new String("hope"));

            testWords.add(new String("callousness"));
            stemWords.add(new String("callous"));
            testWords.add(new String("formality"));
            stemWords.add(new String("formal"));
            testWords.add(new String("sensitivity"));
            stemWords.add(new String("sensit"));
            testWords.add(new String("sensibility"));
            stemWords.add(new String("sensibl"));
            testWords.add(new String("ability"));
            stemWords.add(new String("abil"));

            testWords.add(new String("triplicate"));
            stemWords.add(new String("triplic"));
            testWords.add(new String("formative"));
            stemWords.add(new String("form"));
            testWords.add(new String("formalize"));
            stemWords.add(new String("formal"));
            testWords.add(new String("electricity"));
            stemWords.add(new String("electr"));
            testWords.add(new String("electrical"));
            stemWords.add(new String("electr"));

            testWords.add(new String("revival"));
            stemWords.add(new String("reviv"));
            testWords.add(new String("allowance"));
            stemWords.add(new String("allow"));
            testWords.add(new String("inference"));
            stemWords.add(new String("infer"));
            testWords.add(new String("airliner"));
            stemWords.add(new String("airlin"));
            testWords.add(new String("adjustable"));
            stemWords.add(new String("adjust"));

            testWords.add(new String("defensible"));
            stemWords.add(new String("defens"));
            testWords.add(new String("replacement"));
            stemWords.add(new String("replac"));
            testWords.add(new String("element"));
            stemWords.add(new String("element"));
            testWords.add(new String("dependent"));
            stemWords.add(new String("depend"));
            testWords.add(new String("activate"));
            stemWords.add(new String("activ"));

            testWords.add(new String("effective"));
            stemWords.add(new String("effect"));
            testWords.add(new String("rate"));
            stemWords.add(new String("rate"));
            testWords.add(new String("cease"));
            stemWords.add(new String("ceas"));
            testWords.add(new String("controller"));
            stemWords.add(new String("control"));
            testWords.add(new String("roll"));
            stemWords.add(new String("roll"));

            int index;
            String result;
            boolean valid = true;
            for (index = 0; index < testWords.size(); index++) {
                result = test.getStem(testWords.get(index));
                if (stemWords.get(index).compareTo(result) != 0) {
                    valid = false;
                    System.out.println("Stem test failed. Word: " +
                                       testWords.get(index) +
                                       " Expected stem:" +
                                       stemWords.get(index) +
                                       " Actual stem: " + result);
                }
            } 

            if (valid)
                System.out.println("All tests passed successfully");
        }
        catch (Exception e) {
            System.out.println("Exception " + e + " caught");
            throw e;
        }
    }
}
