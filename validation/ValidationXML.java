package validation;

import org.xml.sax.SAXException;

import java.io.IOException;

public interface ValidationXML {

    public void validate() throws SAXException, IOException;
}
