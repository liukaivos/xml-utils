package com.xml.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XMLParser extends DefaultHandler
{
    
    private XMLNode rootNode;
    private XMLNode currentNode;

    public XMLNode parse(String xml)
            throws SAXException, ParserConfigurationException, IOException
    {
        InputStream xmlStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        return parse(xmlStream);
    }
    
    public XMLNode parse(InputStream xmlStream)
            throws SAXException, ParserConfigurationException, IOException
    {
        rootNode = new XMLNode();
        currentNode = rootNode;
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser(); 
        XMLReader xmlReader = parser.getXMLReader(); 
        xmlReader.setContentHandler(this);
        xmlReader.parse(new InputSource(xmlStream)); 
        return rootNode;
    }

    // DefaultHandler
    
    public void startElement(String uri, String localName, String qName,
            Attributes atts)
            throws SAXException
    {
        Map<String, String> attributes = new HashMap<String, String>();
        for (int i = 0; i < atts.getLength(); ++i)
            attributes.put(atts.getLocalName(i), atts.getValue(i));
        XMLInternalNode node = new XMLInternalNode(currentNode, localName,
                attributes);
        currentNode.children.add(node);
        currentNode = node;
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException
    {
        currentNode = currentNode.parent;
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException
    {
        String string = new String(ch, start, length);
        if (currentNode.children.size() != 0)
        {
            XMLNode lastChild = currentNode.children.get(
                    currentNode.children.size() - 1);
            if (lastChild instanceof XMLLeafNode)
                ((XMLLeafNode)lastChild).value.append(string);
            else
                currentNode.children.add(new XMLLeafNode(currentNode, string));
        }
        else
            currentNode.children.add(new XMLLeafNode(currentNode, string));
    }
    
}
