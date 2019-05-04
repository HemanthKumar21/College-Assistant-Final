package com.example.hemanth.collegeassistant;

import android.content.Context;
import android.os.Environment;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import android.content.Context.*;

public class CosineSimilarity {
    public class values {
        int val1;
        int val2;

        values(int v1, int v2) {
            this.val1 = v1;
            this.val2 = v2;
        }

        public void Update_VAl(int v1, int v2) {
            this.val1 = v1;
            this.val2 = v2;
        }
    }//end of class values

    public double CosineSimilarityScore(String Text1, String Text2) {
        double sim_score = 0.0000000;
        //1. Identify distinct words from both documents
        String[] word_seq_text1 = Text1.split("\\W+");
        String[] word_seq_text2 = Text2.split("\\W+");

        String[] stop_words = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};
        /*
        String[] word_seq_text1 = new String[20];
        String[] word_seq_text2 = new String[20];
        int j=0;
        for(String x : word_seq_text11) {
            if(!Arrays.asList(stop_words).contains(x)) {
                word_seq_text1[j] = x;
                j++;
            }
        }

        j=0;
        for(String x : word_seq_text21) {
            if(!Arrays.asList(stop_words).contains(x)) {
                word_seq_text2[j] = x;
                j++;
            }
        }
        */
        Hashtable<String, values> word_freq_vector = new Hashtable<String, CosineSimilarity.values>();
        LinkedList<String> Distinct_words_text_1_2 = new LinkedList<String>();

        //prepare word frequency vector by using Text1
        for (int i = 0; i < word_seq_text1.length; i++) {
            String tmp_wd = word_seq_text1[i].trim();

            if(!Arrays.asList(stop_words).contains(tmp_wd.toLowerCase())) {
                tmp_wd = tmp_wd.toUpperCase();

                if (tmp_wd.length() > 0) {
                    if (word_freq_vector.containsKey(tmp_wd)) {
                        values vals1 = word_freq_vector.get(tmp_wd);
                        int freq1 = vals1.val1 + 1;
                        int freq2 = vals1.val2;
                        vals1.Update_VAl(freq1, freq2);
                        word_freq_vector.put(tmp_wd, vals1);
                    } else {
                        values vals1 = new values(1, 0);
                        word_freq_vector.put(tmp_wd, vals1);
                        Distinct_words_text_1_2.add(tmp_wd);
                    }
                }
            }

        }

        //prepare word frequency vector by using Text2
        for (int i = 0; i < word_seq_text2.length; i++) {
            String tmp_wd = word_seq_text2[i].trim();

            if(!Arrays.asList(stop_words).contains(tmp_wd.toLowerCase())) {
                tmp_wd = tmp_wd.toUpperCase();
                if (tmp_wd.length() > 0) {
                    if (word_freq_vector.containsKey(tmp_wd)) {
                        values vals1 = word_freq_vector.get(tmp_wd);
                        int freq1 = vals1.val1;
                        int freq2 = vals1.val2 + 1;
                        vals1.Update_VAl(freq1, freq2);
                        word_freq_vector.put(tmp_wd, vals1);
                    } else {
                        values vals1 = new values(0, 1);
                        word_freq_vector.put(tmp_wd, vals1);
                        Distinct_words_text_1_2.add(tmp_wd);
                    }
                }
            }

        }

        //calculate the cosine similarity score.
        double VectAB = 0.0000000;
        double VectA_Sq = 0.0000000;
        double VectB_Sq = 0.0000000;

        for (int i = 0; i < Distinct_words_text_1_2.size(); i++) {
            values vals12 = word_freq_vector.get(Distinct_words_text_1_2.get(i));

            double freq1 = (double) vals12.val1;
            double freq2 = (double) vals12.val2;
            //System.out.println(Distinct_words_text_1_2.get(i)+"#"+freq1+"#"+freq2);

            VectAB = VectAB + (freq1 * freq2);

            VectA_Sq = VectA_Sq + freq1 * freq1;
            VectB_Sq = VectB_Sq + freq2 * freq2;
        }
        //System.out.println("VectAB "+VectAB+" VectA_Sq "+VectA_Sq+" VectB_Sq "+VectB_Sq);
        sim_score = ((VectAB) / (Math.sqrt(VectA_Sq) * Math.sqrt(VectB_Sq)));

        return (sim_score);
    }

    public String cosineSim(String query,File file) throws FileNotFoundException, IOException{
        String match = "";
        //System.out.println("[Word # VectorA # VectorB]");
        //double sim_score = cs1.CosineSimilarity_Score("distributed system syllabus", "syllabus for machine learning");

        //FileInputStream questionSet = new FileInputStream("/collegeAssistant/bots/CollegeAssistant/aiml/questions.txt");
        //BufferedReader in = new BufferedReader(new InputStreamReader(questionSet));
        BufferedReader in = new BufferedReader(new FileReader(file));

        double max_score=0.0000000;
        System.out.println("Query : "+query);
        String s = null;
        while ((s = in.readLine()) != null) {
            double score = CosineSimilarityScore(query, s);
            System.out.println(score+"\t"+s);
            if(score > max_score) {
                max_score = score;
                match = s;
            }
        }
        if(max_score >= 0.5) {
            return match;
        }
        else {
            match = "NA";
        }
        return match;
    }
}