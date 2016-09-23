package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
                words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if (prefix.length() == 0) {
            int min = 0;
            int max = words.size();
            int random_word_index = min + (int) (Math.random() * ((max - min) + 1));
            return words.get(random_word_index);
        } else {
            int index = Collections.binarySearch(words, prefix);
            if (index < 0)  //Prefix does not occur as a complete word by itself
            {
                index = (-1) * index - 1;
            }

            if (words.get(index).contains(prefix)) {
                return words.get(index);
            } else {
                return null;
            }

        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        ArrayList<String> even_length_words = new ArrayList<>();
        ArrayList<String> odd_length_words = new ArrayList<>();


        if (prefix.length() == 0) {
            int min = 0;
            int max = words.size();
            int random_word_index = min + (int) (Math.random() * ((max - min) + 1));
            return words.get(random_word_index);
        } else {
            int index = Collections.binarySearch(words, prefix);
            if (index < 0)  //Prefix does not occur as a complete word by itself
            {
                index = (-1) * index - 1;
            }

            if (words.get(index).contains(prefix)) {
                if (words.get(index).length() % 2 == 0) {
                    even_length_words.add(words.get(index));
                } else {
                    odd_length_words.add(words.get(index));
                }
            }


            int i = index + 1;
            boolean flag = true;

            while(flag)
            {
                if(i<words.size() && words.get(i).contains(prefix))
                {
                    if(words.get(i).length()%2==0)
                    {
                        even_length_words.add(words.get(i));
                    }
                    else
                    {
                        odd_length_words.add(words.get(i));
                    }
                    i=i+1;
                }
                else
                {
                    flag=false;
                }
            }
            if(GhostActivity.user_started_first)
            {
                if(odd_length_words.size()>0)
                {
                    return odd_length_words.get(0);
                }
                return even_length_words.get(0);
            }
            else
            {
                if(even_length_words.size()>0)
                {
                    return even_length_words.get(0);
                }
                return odd_length_words.get(0);
            }
        }
       // return null;
    }
}
