package ar.edu.itba.paw.persistence;

public class Queries {

    static final String PROJECT_TABLE = "projects";
    private static final String USER_TABLE = "users";
    private static final String COUNTRY_TABLE = "countries";
    private static final String STATE_TABLE = "states";
    private static final String CITY_TABLE = "cities";
    static final String PROJECT_CATEGORIES_TABLE = "project_categories";
    private static final String CATEGORIES_TABLE = "categories";


    static final String PROJECT_FIND_ALL = "SELECT " +
            "p.id, " +
            "p.project_name AS name, " +
            "p.summary, " +
            "p.cost, " +
            "p.hits, " +
            "p.publish_date, " +
            "p.update_date, " +
            "p.images AS has_images, " +

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
            "u.profile_pic AS owner_profile_picture, " +
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

    static final String PROJECT_FIND_BY_ID = PROJECT_FIND_ALL + "WHERE p.id = ?";

    static final String PROJECT_FIND_BY_CAT = PROJECT_FIND_ALL + "WHERE pcat.category_id IN (:categories)";
}
