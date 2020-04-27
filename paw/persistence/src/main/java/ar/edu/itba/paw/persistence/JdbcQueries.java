package ar.edu.itba.paw.persistence;

public class JdbcQueries {

    static final String PROJECT_TABLE = "projects";
    static final String USER_TABLE = "users";
    private static final String COUNTRY_TABLE = "countries";
    private static final String STATE_TABLE = "states";
    private static final String CITY_TABLE = "cities";
    static final String PROJECT_CATEGORIES_TABLE = "project_categories";
    private static final String CATEGORIES_TABLE = "categories";
    public static final String PASSWORDS_TABLE = "passwords";

    static final String CATEGORY_FIND_ALL = "SELECT " +
            "cat.id, " +
            "cat.category AS name, " +
            "cat.parent AS parent_id " +
            "FROM " + CATEGORIES_TABLE + " cat ";

    static final String CATEGORY_FIND_BY_PROJECT_ID = CATEGORY_FIND_ALL +
            "JOIN " + PROJECT_CATEGORIES_TABLE + " pcat ON (pcat.category_id = cat.id) " +
            "WHERE pcat.project_id = ?";

    static final String USER_FIND_ALL = "SELECT " +
            "u.id, " +
            "u.role_id AS role, " +
            "u.first_name, " +
            "u.last_name, " +
            "u.real_id, " +

            "u.aux_date AS birth_date, " +
            "u.email, " +
            "u.password, " +
            "u.phone, " +
            "u.linkedin, " +
            "u.join_date, " +
            "u.trust_index, " +

            "co.id AS location_country_id, " +
            "co.country AS location_country_name, " +
            "co.iso2 AS location_country_iso_code, " +
            "co.phonecode AS location_country_phone_code, " +
            "co.currency AS location_country_currency, " +

            "st.id AS location_state_id, " +
            "st.state AS location_state_name, " +
            "st.iso2 AS location_state_iso_code, " +

            "ci.id AS location_city_id, " +
            "ci.city AS location_city_name " +

            "FROM " + USER_TABLE + " u " +
            "JOIN " + COUNTRY_TABLE + " co ON (u.country_id = co.id) " +
            "JOIN " + STATE_TABLE + " st ON (u.state_id = st.id) " +
            "JOIN " + CITY_TABLE + " ci ON (u.city_id = ci.id)";

    static final String USER_FIND_COINCIDENCE = USER_FIND_ALL + "WHERE lower(u.first_name) LIKE ? " +
                                                                "OR lower(u.last_name) LIKE ? " +
                                                                "OR lower(u.email) LIKE ?";

    static final String USER_FIND_BY_ID = USER_FIND_ALL + "WHERE u.id = ?";

    static final String USER_FIND_BY_USERNAME = USER_FIND_ALL + "WHERE u.email = ?";

    static final String USER_FIND_PASSWORD = "SELECT password FROM " + PASSWORDS_TABLE + " p " + "WHERE p.id = ?";

    static final String PROJECT_FIND_ALL = "SELECT " +
            "p.id, " +
            "p.project_name AS name, " +
            "p.summary, " +
            "p.cost, " +
            "p.hits, " +
            "p.publish_date, " +
            "p.update_date, " +

            "p.aproved AS back_office_approved, " +
            "p.profit_index AS back_office_profit_index, " +
            "p.risk_index AS back_office_risk_index, " +

            "u.id AS owner_id, " +
            "u.first_name AS owner_first_name, " +
            "u.last_name AS owner_last_name, " +
            "u.real_id AS owner_real_id, " +

            "u.aux_date AS owner_birth_date, " +
            "u.email AS owner_email, " +
            "u.phone AS owner_phone, " +
            "u.linkedin AS owner_linkedin, " +
            "u.join_date AS owner_join_date, " +
            "u.trust_index AS owner_trust_index, " +

            "co.id AS owner_location_country_id, " +
            "co.country AS owner_location_country_name, " +
            "co.iso2 AS owner_location_country_iso_code, " +
            "co.phonecode AS owner_location_country_phone_code, " +
            "co.currency AS owner_location_country_currency, " +

            "st.id AS owner_location_state_id, " +
            "st.state AS owner_location_state_name, " +
            "st.iso2 AS owner_location_state_iso_code, " +

            "ci.id AS owner_location_city_id, " +
            "ci.city AS owner_location_city_name, " +

            "cat.id AS categories_id, " +
            "cat.category AS categories_name, " +
            "cat.parent AS categories_parent_id " +

            "FROM " + PROJECT_TABLE + " p " +
            "JOIN " + USER_TABLE + " u ON (p.owner_id = u.id) " +
            "JOIN " + COUNTRY_TABLE + " co ON (u.country_id = co.id) " +
            "JOIN " + STATE_TABLE + " st ON (u.state_id = st.id) " +
            "JOIN " + CITY_TABLE + " ci ON (u.city_id = ci.id) " +
            "JOIN " + PROJECT_CATEGORIES_TABLE + " pcat ON (p.id = pcat.project_id) " +
            "JOIN " + CATEGORIES_TABLE + " cat ON (pcat.category_id = cat.id) ";

    static final String PROJECT_FIND_COINCIDENCE = PROJECT_FIND_ALL + "WHERE lower(p.project_name) LIKE ?";

    static final String PROJECT_FIND_BY_OWNER = PROJECT_FIND_ALL + "WHERE p.owner_id = ?";

    static final String PROJECT_FIND_BY_ID = PROJECT_FIND_ALL + "WHERE p.id = ?";

    static final String PROJECT_FIND_BY_CAT = PROJECT_FIND_ALL + "WHERE pcat.category_id IN (:categories)";

    static final String PROJECT_IMAGE = "SELECT p.images FROM " + PROJECT_TABLE + " p WHERE p.id = ?";

    static final String USER_IMAGE = "SELECT u.profile_pic FROM " + USER_TABLE + " u WHERE u.id = ?";

    static final String USER_UPDATE = "UPDATE users SET " +
            "role_id = ?, " +
            "first_name = ?, " +
            "last_name = ?, " +
            "real_id = ?, " +
            "aux_date = ?, " +
            "country_id = ?, " +
            "state_id = ?, " +
            "city_id = ?, " +
            "phone = ?, " +
            "linkedin = ?, " +
            "profile_pic = ?, " +
            "password = ? " +
            "WHERE users.id = ?";
}
