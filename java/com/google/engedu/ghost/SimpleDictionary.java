/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
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

        if(prefix.isEmpty())
        {
            Random ran = new Random();
            int x = ran.nextInt(words.size());
            return words.get(x);
        }
        else
        {
            int start = 0;
            int end = words.size()-1;
            while(start<=end)
            {
                int mid = (start+end)/2;
                if(words.get(mid).contains(prefix))
                    return words.get(mid);
                int c = words.get(mid).compareTo(prefix);
                if(c<0)
                {
                    start = mid+1;
                }
                if(c>0)
                {
                    end = mid-1;
                }
            }
            return null;
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        if(prefix.isEmpty())
        {
            Random ran = new Random();
            int x = ran.nextInt(words.size());
            return words.get(x);
        }
        else
        {
            int start = 0;
            int end = words.size()-1;
            while(start<=end)
            {
                int mid = (start+end)/2;
                if(words.get(mid).contains(prefix)) {

                    ArrayList<String> odd= new ArrayList<>(),even= new ArrayList<>();

                    for(int i = mid; i<=end; i++)
                    {
                        if(words.get(i).length()%2==1)
                        {
                            odd.add(words.get(i));
                        }
                        else
                            even.add(words.get(i));
                    }
                    Random ran = new Random();
                    int x;

                    x= ran.nextInt(odd.size());

                    selected = odd.get(x);
                    return selected;
                }
                int c = words.get(mid).compareTo(prefix);
                if(c<0)
                {
                    start = mid+1;
                }
                if(c>0)
                {
                    end = mid-1;
                }
            }
            return selected;
        }

    }
}
