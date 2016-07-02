package vn.brine.haileader.musicjena.ontology;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;

/**
 * Created by HaiLeader on 6/23/2016.
 */
public class FinalOntology {
    public static final String NS = "http://www.finalSample./finalExam.owl#";
    public static OntModel ontModel = null;
    public static OntClass ontClass = null;

    public static Reasoner reasoner;
    public static InfModel infModel;

    public static String result;

    public static Model generateModel(String gerne){
        ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        ontModel.setNsPrefix("finalExam", NS);

        ontClass = ontModel.createClass(NS + "User");
        ontModel.write(System.out);

        return ontModel;
    }
}
