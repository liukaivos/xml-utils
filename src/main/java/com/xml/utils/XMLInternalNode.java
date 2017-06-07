package com.xml.utils;

import java.util.Map;

public class XMLInternalNode extends XMLNode
{
    public String key;
    public Map<String, String> attributes;

    public XMLInternalNode(XMLNode parent, String key,
            Map<String, String> attributes)
    {
        super(parent);
        
        this.key = key;
        this.attributes = attributes;
    }
    
    public String generateXML()
    {
        StringBuilder attrsString = new StringBuilder();
        if (attributes != null)
        {
            for (String name : attributes.keySet())
                attrsString.append(String.format(" %s=\"%s\"", name,
                        attributes.get(name)));
        }
        return String.format("<%s%s>%s</%s>", key, attrsString.toString(),
                super.generateXML(), key);
    }
}
