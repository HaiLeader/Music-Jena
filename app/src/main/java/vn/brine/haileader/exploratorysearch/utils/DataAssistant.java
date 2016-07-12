package vn.brine.haileader.exploratorysearch.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HaiLeader on 7/5/2016.
 */
public class DataAssistant {
    public static List<String> splitTextSearchToPhrase(String textSearch) {
        List<String> mListKeyWord = new ArrayList<String>();

        String[] splitTextSearch = textSearch.split(" ");
        int lengthListSplit = splitTextSearch.length;
        for (int i = 0; i < lengthListSplit; i++) {
            if (i + 2 < lengthListSplit) {
                String pharse = splitTextSearch[i] + " " + splitTextSearch[i + 1] + " " + splitTextSearch[i + 2];
                mListKeyWord.add(pharse);
            }
            if (i + 1 < lengthListSplit) {
                String pharse = splitTextSearch[i] + " " + splitTextSearch[i + 1];
                mListKeyWord.add(pharse);
            }
            if (!splitTextSearch[i].equals("film")) {
                mListKeyWord.add(splitTextSearch[i]);
            }
        }
        return mListKeyWord;
    }

    public static boolean isStopWord(String word) {
        List<String> listStopWord = Arrays.asList(Config.STOP_WORD);
        if (listStopWord.contains(word)) return true;
        return false;
    }

    public static String replaceUnderlineToSpace(String localName) {
        String result = localName.replace("_", " ");
        return result;
    }

    public static String replaceSpaceToUnderline(String localName) {
        String result = localName.replace(" ", "_");
        return result;
    }

    public static boolean isProperty(String property) {
        if (property.contains("film") || property.contains("actor") ||
                property.contains("editor") || property.contains("writer") ||
                property.contains("director") || property.contains("producer") ||
                property.contains("character"))
            return true;
        return false;
    }

    public static List<String> convertStringArrayToList(){
        List<String> listMovieType = Arrays.asList(Config.MOVIE_TYPE);
        return listMovieType;
    }

    public static String changeTextType(String uriKey, String type) {
        if (type.equals("movie:film")) {
            if (uriKey == null) return null;
            String endUri = uriKey.replace("http://data.linkedmdb.org/resource/", "");
            String[] splitType = endUri.split("/");
            type = splitType[0];
        }

        if (type.equals("movie:content_rating")) {
            type = "movie:rating";
        }
        if (type.equals("movie:country")) {
            type = "oddlinker:link_source";
        }
        if (type.equals("movie:film_costume_designer")) {
            type = "movie:costume_designer";
        }
        if (type.equals("movie:film_crew_gig")) {
            type = "movie:film_crew_gig_film";
        }
        if (type.equals("movie:film_genre")) {
            type = "movie:genre";
        }
        if (type.equals("movie:film_crew_gig_film_job")) {
            type = "movie:film_job";
        }
        if (type.equals("movie:film_location")) {
            type = "movie:featured_film_location";
        }
        if (type.equals("movie:special_film_performance_type")) {
            type = "movie:performance_special_performance_type";
        }
        return type;
    }
}
