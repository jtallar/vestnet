package ar.edu.itba.paw.persistence;

import static ar.edu.itba.paw.persistence.JdbcQueries.*;

public class JdbcProjectQueryBuilder {
    private StringBuilder query;

    public JdbcProjectQueryBuilder() {
        this.query = new StringBuilder();
        query.append("SELECT ");
    }

    public void selectProjectIds() {
        this.query.append("p.id ");
        this.query.append("FROM " + PROJECT_TABLE + " p ");
    }

    public void selectProjectCount() {
        this.query.append("COUNT(*) ");
        this.query.append("FROM " + PROJECT_TABLE + " p ");
    }

    public void addSort(SortQuery query) {
        this.query.append("ORDER BY ");
        this.query.append(query.getQuery());
        this.query.append(", p.id DESC ");
    }

    public void addLimitOffset() {
        this.query.append("OFFSET (:offset) LIMIT (:limit)");
    }

    public void addMinCost() {
        addWhere();
        this.query.append("p.cost >= (:minCost) ");
    }

    public void addMaxCost() {
        addWhere();
        this.query.append("p.cost <= (:maxCost) ");
    }

    public void addCategory() {
        joinCategoryTable();
        addWhere();
        this.query.append("pcat.category_id IN (:category) ");
    }

    public void addSearch(SearchQuery query) {
        if (query.locationTable) joinLocationsTable();
        else if (query.userTable) joinUserTable();
        addWhere();
        this.query.append(query.getQuery());
    }

    public String getQuery() {
        return this.query.toString();
    }

    public enum SortQuery {
        DEFAULT(0, "p.hits DESC"),
        DATE(1, "p.publish_date"),
        COST_ASCENDING(2, "p.cost"),
        COST_DESCENDING(3, "p.cost DESC"),
        ALPHABETICAL(4, "p.project_name");

        private int id;
        private String query;

        SortQuery(int id, String query) {
            this.id = id;
            this.query = query;
        }

        public int getId() {
            return id;
        }

        public String getQuery() {
            return query;
        }

        public static SortQuery getEnum(int id) {
            for (SortQuery value : values())
                if (value.id == id) return value;
            return DEFAULT;
        }
    }

    public enum SearchQuery {
        DEFAULT(0, "lower(p.project_name) LIKE (:keyword) ", false, false),
        PROJECT_INFO(1, "lower(p.summary) LIKE (:keyword) ", true, false),
        OWNER_NAME(2, "lower(u.first_name) LIKE (:keyword) OR lower(u.last_name) LIKE (:keyword) ", true, false),
        OWNER_MAIL(3, "lower(u.email) LIKE (:keyword) ", true, false),
        PROJECT_LOCATION(4, "lower(co.country) LIKE (:keyword) OR lower(st.state) LIKE (:keyword) OR lower(ci.city) LIKE (:keyword) ", true, true);

        private int id;
        private String query;
        boolean userTable;
        boolean locationTable;

        SearchQuery(int id, String query, boolean userTable, boolean locationTable) {
            this.id = id;
            this.query = query;
            this.userTable = userTable;
            this.locationTable = locationTable;
        }

        public int getId() {
            return id;
        }

        public String getQuery() {
            return query;
        }

        public static SearchQuery getEnum(int id) {
            for (SearchQuery value : values())
                if (value.id == id) return value;
            return DEFAULT;
        }
    }

    /**
     * Auxiliary functions
     */

    private void addWhere() {
        if (this.query.indexOf("WHERE") == -1) this.query.append("WHERE ");
        else this.query.append("AND ");
    }

    private void joinCategoryTable() {
        String categoryJoin =   "JOIN " + PROJECT_CATEGORIES_TABLE + " pcat ON (p.id = pcat.project_id) " +
                                "JOIN " + CATEGORIES_TABLE + " cat ON (pcat.category_id = cat.id) ";
        makeJoin(categoryJoin);
    }

    private void joinUserTable() {
        String userJoin = "JOIN " + USER_TABLE + " u ON (p.owner_id = u.id) ";
        makeJoin(userJoin);
    }


    public void joinLocationsTable() {
        String userJoin = "JOIN " + USER_TABLE + " u ON (p.owner_id = u.id) ";
        String locationsJoin =  "JOIN " + COUNTRY_TABLE + " co ON (u.country_id = co.id) " +
                                "JOIN " + STATE_TABLE + " st ON (u.state_id = st.id) " +
                                "JOIN " + CITY_TABLE + " ci ON (u.city_id = ci.id) ";
        makeJoin(userJoin + locationsJoin);
    }

    private void makeJoin(String join) {
        int index = this.query.indexOf("WHERE");
        if (index == -1) this.query.append(join);
        else this.query.insert(index, join);
    }

}
