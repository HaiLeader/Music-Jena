package vn.brine.haileader.exploratorysearch.utils;

/**
 * Created by HaiLeader on 7/12/2016.
 */
public class QueryAssistant {

    public static String searchAccurateQuery(String keyword){
        String queryString = Config.PREFIX_LINKEDMDB +
                "SELECT DISTINCT * WHERE " +
                "{" +
                " {" +
                "{?s dc:title " + "\"" + keyword + "\"" + "} " +
                "UNION {?s movie:actor_name " + "\"" + keyword + "\"" + "} " +
                "UNION {?s movie:editor_name " + "\"" + keyword + "\"" + "} " +
                "UNION {?s movie:director_name " + "\"" + keyword + "\"" + "} " +
                "UNION {?s movie:writer_name " + "\"" + keyword + "\"" + "} " +
                "UNION {?s movie:producer_name " + "\"" + keyword + "\"" + "} " +
                "UNION {?s movie:film_character_name " + "\"" + keyword + "\"" + "} " +
                "UNION {?s movie:film_genre_name " + "\"" + keyword + "\"" + "}" +
                "}. " +
                "?s foaf:page ?url.?s rdfs:label ?label" +
                "}";
        return queryString;
    }

    public static String searchExpandQuery(String keyword){
        String queryString = Config.PREFIX_LINKEDMDB +
                "SELECT DISTINCT * WHERE " +
                "{ ?s rdfs:label ?o . FILTER regex(?o, " + "\"" + keyword + "\"" + " ,'i'). " +
                "?s foaf:page ?url} limit 16";
        return queryString;
    }

    public static String getInfoFromTypeQuery(String uriKey, String movieType){
        String queryString = Config.PREFIX_LINKEDMDB +
                "SELECT * WHERE {" +
                "{<" + uriKey + "> " + movieType + " ?s}" +
                "UNION{?s " + movieType + "<" + uriKey + "> }. " +
                "?s foaf:page ?url}";
        return queryString;
    }
}
