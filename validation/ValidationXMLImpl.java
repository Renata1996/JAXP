package validation;

import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class ValidationXMLImpl implements ValidationXML {

    private static final String STANDARD_SCHEMA_FACTORY_PATH = "http://www.w3.org/2001/XMLSchema";
    private String xmlPath;

    public ValidationXMLImpl(String xmlPath ) {
        this.xmlPath = xmlPath;
    }

    @Override
    public void validate() throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance(STANDARD_SCHEMA_FACTORY_PATH);
        Schema schema = factory.newSchema();
        Validator validator = schema.newValidator();
        Source source = new StreamSource(xmlPath);
        validator.validate(source);
    }
}
