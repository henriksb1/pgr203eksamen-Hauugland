package no.kristiania.http;

public class QueryString {

    private final String value;
    private final String parameterName;

    public QueryString(String queryString) {
        int equalPos = queryString.indexOf("=");
        value = queryString.substring(equalPos+1);
        parameterName = queryString.substring(0, equalPos);
    }

    public String getParameter(String name) {
        if (name.equals(parameterName)){
            return value;
        }
        return null;
    }

}
