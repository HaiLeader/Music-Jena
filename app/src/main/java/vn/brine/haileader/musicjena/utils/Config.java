package vn.brine.haileader.musicjena.utils;

/**
 * Created by HaiLeader on 7/2/2016.
 */
public class Config {
    public static final String DBPEDIA_ENDPOINT = "http://dbpedia.org/sparql";
    public static final String LINKEDMDB_ENDPOINT = "http://www.linkedmdb.org/sparql";
    public static final String WIKIDATA_ENDPOINT = "http://query.wikidata.org";
    public static final String PREFIX_LINKEDMDB =
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                    "PREFIX oddlinker: <http://data.linkedmdb.org/resource/oddlinker/>\n" +
                    "PREFIX map: <file:/C:/d2r-server-0.4/mapping.n3#>\n" +
                    "PREFIX db: <http://data.linkedmdb.org/resource/>\n" +
                    "PREFIX dbpedia: <http://dbpedia.org/property/>\n" +
                    "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                    "PREFIX dc: <http://purl.org/dc/terms/>\n" +
                    "PREFIX movie: <http://data.linkedmdb.org/resource/movie/>";

    public static final String[] STOP_WORD =
            {
                    "a", "about", "above", "after", "again", "against", "all", "am", "an", "and",
                    "any", "are", "aren't", "as", "at", "be", "because", "been", "before",
                    "being", "below", "between", "both", "but", "by", "can't", "cannot",
                    "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing",
                    "don't", "down", "during", "each", "few", "for", "from", "further",
                    "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he",
                    "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself",
                    "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm",
                    "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself",
                    "let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor",
                    "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our",
                    "ours", "ourselves", "out", "over", "own", "same", "shan't", "she", "she'd",
                    "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than",
                    "that", "that's", "the", "their", "theirs", "them", "themselves", "then",
                    "there", "there's", "these", "they", "they'd", "they'll", "they're",
                    "they've", "this", "those", "through", "to", "too", "under", "until",
                    "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've",
                    "were", "weren't", "what", "what's", "when", "when's", "where", "where's",
                    "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't",
                    "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your",
                    "yours", "yourself", "yourselves"
            };
    public static final String[] MOVIE_TYPE = {
                   "film distribution medium", "film art director", "special film performance type",
                    "interlink", "film screening venue", "film festival", "film awards ceremony",
                    "film production designer", "personal film appearance", "film collection",
                    "film character", "content rating", "linkage run", "country", "production company",
                    "music contributor", "film cut", "film format", "writer", "performance",
                    "film film company relationship", "film job", "content rating system",
                    "dubbing performance", "film location", "film casting director", "Agent",
                    "film company", "personal film appearance type", "film featured song",
                    "film distributor", "film series", "film set designer", "film festival event",
                    "film costume designer", "film", "film genre", "film festival focus", "Person",
                    "film crewmember", "film subject", "film critic", "director", "film regional release date",
                    "film festival sponsor", "film theorist", "cinematographer", "film film distributor relationship",
                    "producer", "film crew gig", "actor", "editor", "film story contributor"
            };
}
