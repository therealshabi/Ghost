package com.google.engedu.ghost;

import android.util.Log;

import java.util.HashMap;


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;

    public HashMap<Character, TrieNode> getChildren() {
        return children;
    }

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        int length = s.length();
        TrieNode temp = this;

        for (int i = 0; i < length; i++) {
            HashMap<Character, TrieNode> child = temp.getChildren();
            char ch = s.charAt(i);

            if (child.containsKey(ch)) {   // If ch is in the node we are moving further
                temp = child.get(ch);
            } else {
                TrieNode new_node = new TrieNode();  // If ch is not in the node we are creating new node and inserting ch in the new node
                child.put(ch, new_node);
                temp = new_node;
            }
        }
        temp.setIsEnd(true);
    }

    public boolean isWord(String s) {
        TrieNode crawl = this;
        int length = s.length();

        for (int i = 0; i < length; i++) {
            char ch = s.charAt(i);

            HashMap<Character, TrieNode> child = crawl.getChildren();

            if (child.containsKey(ch)) {
                crawl = child.get(ch);
            } else {
                return false;
            }
        }
        if (crawl.isWord) {
            return true;
        }
        return false;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode crawl = this;
        int length = s.length();
        String word = "";

        if (length == 0) {
            String starting_word = "";
            int min_char_index = 97;
            int max_char_index = 122;
            int random_char_index = min_char_index + (int) (Math.random() * ((max_char_index - min_char_index) + 1));
            for (int i = random_char_index; i <= 122; i++) {
                char ch = (char) i;
                HashMap<Character, TrieNode> child = crawl.getChildren();
                if (child.containsKey(ch)) {
                    starting_word += ch;
                    crawl = child.get(ch);
                    i = 96;
                }
            }
            return starting_word;
        } else {
            for (int i = 0; i < length; i++) {
                char ch = s.charAt(i);
                HashMap<Character, TrieNode> child = crawl.getChildren();
                if (child.containsKey(ch)) {
                    word += ch;
                    crawl = child.get(ch);
                }
                else
                {
                    return null;
                }
            }

            for (int i = 97; i <= 122; i++) {
                char ch = (char) i;
                HashMap<Character, TrieNode> child = getChildren();

                if (child.containsKey(ch)) {
                    word += ch;
                    crawl = child.get(ch);
                    i = 96;
                }
            }

            return word;
        }
    }

    public String getGoodWordStartingWith(String s) {
        TrieNode crawl = this;
        int length = s.length();
        String word= "";

        if(length==0)
        {
            String starting_word = "";
            int min_char_index = 97;
            int max_char_index = 122;
            int random_char_index = min_char_index+ (int)(Math.random() * ((max_char_index-min_char_index)+1));
            for(int i = random_char_index;i<=122;i++)
            {
                char ch = (char)i;
                HashMap<Character,TrieNode> child = crawl.getChildren();
                if(child.containsKey(ch))
                {
                    starting_word+=ch;
                    crawl = child.get(ch);
                    i=96;
                }
            }
            return starting_word;
        }
        else
        {
            String complete_word="";
            for(int i=0;i<length;i++)
            {
                char ch = s.charAt(i);

                HashMap<Character,TrieNode> child = crawl.getChildren();

                if(child.containsKey(ch))
                {
                    word+=ch;
                    crawl = child.get(ch);
                }
                else
                {
                    return null;
                }
            }

            TrieNode orig_crawl = crawl;
            for(int i=97;i<=122;i++)
            {
                char ch = (char)i;
                HashMap<Character, TrieNode> child = crawl.getChildren();
                if(child.containsKey(ch))
                {
                    Log.e("Shahbaz","Words Formed uptil now "+ch);
                    word+=ch;
                    crawl = child.get(ch);
                    if(crawl.getIsWord())
                    {
                        Log.e("Shahbaz","Coming Here");
                        complete_word = word;
                        word =s;
                        crawl = orig_crawl;
                        continue;
                    }
                    else
                    {
                        return word;
                    }
                }
            }
            return complete_word;
        }
    }

    //We are storing boolean true or false at each end of a node when a word is completed
    public void setIsEnd(boolean val) {
        this.isWord = val;
    }

    public boolean getIsWord() {
        return isWord;
    }
}
