package org.owasp.webgoat.session;

public class Parameter implements Comparable {

    String name;
    String value;
    
    public Parameter(String name, String value) {
        this.name=name;
        this.value=value;
    }

    public String getName()
    {
        return name;
    }
    
    public String getValue()
    {
        return value;
    }

    //@Override
    public boolean equals(Object obj) {
        if ( obj instanceof Parameter )
        {
            Parameter other = (Parameter)obj;
            return ( name.equals( other.getName() ) && value.equals( other.getValue() ) );
        }
        return false;
    }

    //@Override
    public int hashCode() {
        return toString().hashCode();
    }

    //@Override
    public String toString() {
        return( name + "=" + value );
    }

    public int compareTo(Object o) {
        return toString().compareTo( o.toString() );
    }
}
