package ar.edu.itba.paw.model.components;

public enum SearchField {
    DEFAULT("default", 0),
    PROJECT_INFO("project_info", 1),
    OWNER_NAME("owner_name", 2),
    OWNER_MAIL("owner_mail", 3),
    PROJECT_LOCATION("project_location", 4);

    private String match;
    private int id;

    SearchField(String match, Integer id) {
        this.match = match;
        this.id = id;
    }

    public String getMethod() {
        return match;
    }

    public int getId() {
        return id;
    }

    public static SearchField getEnum(String match) {
        for (SearchField field : values())
            if (field.getMethod().equals(match)) return field;
        return DEFAULT;
    }

    @Override
    public String toString() {
        return "SearchField{" +
                "match='" + match + '\'' +
                '}';
    }
}
