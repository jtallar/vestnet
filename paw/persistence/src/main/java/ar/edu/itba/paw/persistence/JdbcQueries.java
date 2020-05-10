package ar.edu.itba.paw.persistence;

/**
 * Contains all the postgre sql queries
 */
public class JdbcQueries {

    static final String PROJECT_TABLE = "projects";
    static final String USER_TABLE = "users";
    private static final String COUNTRY_TABLE = "countries";
    private static final String STATE_TABLE = "states";
    private static final String CITY_TABLE = "cities";
    static final String PROJECT_CATEGORIES_TABLE = "project_categories";
    private static final String CATEGORIES_TABLE = "categories";
    public static final String PASSWORDS_TABLE = "passwords";
    public static final String FAVORITES_TABLE = "favorites";
    public static final String MESSAGE_TABLE = "messages";


    /**
     * Category queries
     */

    static final String CATEGORY_FIND_ALL =
            "SELECT " +
            "cat.id, " +
            "cat.category AS name, " +
            "cat.parent AS parent_id " +
            "FROM " + CATEGORIES_TABLE + " cat ";

    static final String CATEGORY_FIND_BY_PROJECT_ID =
            CATEGORY_FIND_ALL +
            "JOIN " + PROJECT_CATEGORIES_TABLE + " pcat ON (pcat.category_id = cat.id) " +
            "WHERE pcat.project_id = ? ";


    /**
     * User queries.
     */

    static final String USER_FIND_ALL =
            "SELECT " +
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
            "JOIN " + CITY_TABLE + " ci ON (u.city_id = ci.id) ";

    static final String USER_FIND_COINCIDENCE =
            USER_FIND_ALL +
            "WHERE lower(u.first_name) LIKE ? " +
            "OR lower(u.last_name) LIKE ? " +
            "OR lower(u.email) LIKE ?" +
            "OR lower(u.first_name || ' ' || u.last_name) LIKE ? ";

    static final String USER_FIND_BY_ID =
            USER_FIND_ALL +
            "WHERE u.id = ? ";

    static final String USER_FIND_BY_USERNAME =
            USER_FIND_ALL +
            "WHERE u.email = ? ";

    static final String USER_FIND_IMAGE_BY_ID =
            "SELECT u.profile_pic " +
            "FROM " + USER_TABLE + " u " +
            "WHERE u.id = ? ";

    static final String USER_UPDATE =
            "UPDATE users SET " +
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
            "WHERE users.id = ? ";

    static final String USER_FIND_FAVORITES =
            "SELECT project_id " +
            "FROM " + FAVORITES_TABLE + " " +
            "WHERE user_id = ? ";

    static final String USER_DELETE_FAVORITE =
            "DELETE " +
            "FROM " + FAVORITES_TABLE + " " +
            "WHERE project_id = ? AND user_id = ? ";


    /**
     * Location queries.
     */

    static final String COUNTRY_FIND_ALL =
            "SELECT " +
            "id, " +
            "country AS name, " +
            "iso2 AS iso_code, " +
            "phonecode AS phone_code, " +
            "currency " +
            "FROM " + COUNTRY_TABLE + " ";

    static final String STATE_FIND_ALL =
            "SELECT " +
            "id, " +
            "state AS name, " +
            "iso2 AS iso_code " +
            "FROM " + STATE_TABLE + " ";

    static final String CITY_FIND_ALL =
            "SELECT " +
            "id, " +
            "city AS name " +
            "FROM " + CITY_TABLE + " ";

    static final String STATE_FIND_BY_COUNTRY_ID =
            STATE_FIND_ALL +
            "WHERE country_id = ? ";

    static final String CITY_FIND_BY_STATE_ID =
            CITY_FIND_ALL +
            "WHERE state_id = ?";


    /**
     * Message queries.
     */

    static final String MESSAGE_UPDATE_STATUS =
            "UPDATE " + MESSAGE_TABLE + " SET " +
            "accepted = ? " +
            "WHERE sender_id = ? AND receiver_id = ? AND project_id = ? ";

    static final String MESSAGE_GET_CONVERSATION =
            "SELECT * FROM " + MESSAGE_TABLE + " " +
            "WHERE project_id = ? AND (( sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)) " +
            "ORDER BY publish_date ";

    static final String MESSAGE_GET_PROJECT_UNREAD =
            "SELECT * FROM " + MESSAGE_TABLE + " " +
            "WHERE project_id = ? AND receiver_id = ? AND accepted IS NULL ";

    static final String MESSAGE_GET_SENT_TO =
            "SELECT * FROM " + MESSAGE_TABLE + " " +
            "WHERE project_id = ? AND sender_id = ? AND receiver_id = ? " +
            "ORDER BY publish_date DESC ";


    /**
     * Project queries.
     */

    static final String PROJECT_DEFAULT_ORDER_BY = "ORDER BY p.hits DESC, p.id DESC ";

    static final String PROJECT_FIND_ALL =
            "SELECT " +
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

    static final String PROJECT_FIND_BY_IDS =
            PROJECT_FIND_ALL +
            "WHERE p.id IN (:ids) " +
            PROJECT_DEFAULT_ORDER_BY;

    static final String PROJECT_FIND_BY_OWNER =
            PROJECT_FIND_ALL +
            "WHERE p.owner_id = ? " +
            PROJECT_DEFAULT_ORDER_BY;

    static final String PROJECT_FIND_BY_ID =
            PROJECT_FIND_ALL +
            "WHERE p.id = ? ";



    static final String PROJECT_IDS_ALL_NAME_MATCH =
            "SELECT p.id " +
            "FROM " + PROJECT_TABLE + " p " +
            "JOIN " + USER_TABLE + " u ON (p.owner_id = u.id) " +
            "JOIN " + COUNTRY_TABLE + " co ON (u.country_id = co.id) " +
            "JOIN " + STATE_TABLE + " st ON (u.state_id = st.id) " +
            "JOIN " + CITY_TABLE + " ci ON (u.city_id = ci.id) " +
            "WHERE lower(p.project_name) LIKE (:name) " +
            "OR lower(u.first_name) LIKE (:name) OR lower(u.last_name) LIKE (:name) " +
            "OR lower(co.country) LIKE (:name) OR lower(st.state) LIKE (:name) OR lower(ci.city) LIKE (:name) " +
            "OR lower(p.summary) LIKE (:name) OR lower(u.email) LIKE (:name) " +
            PROJECT_DEFAULT_ORDER_BY +
            "OFFSET (:from) LIMIT (:to) ";

    static final String PROJECT_IDS_COST_MATCH =
            "SELECT id " +
            "FROM " + PROJECT_TABLE + " p " +
            "WHERE cost >= ? AND cost <= ? " +
            PROJECT_DEFAULT_ORDER_BY +
            "OFFSET ? LIMIT ? ";

    static final String PROJECT_IDS_NAME_SUMMARY_MATCH =
            "SELECT p.id " +
            "FROM " + PROJECT_TABLE + " p " +
            "WHERE lower(p.project_name) LIKE (:name) OR lower(p.summary) LIKE (:name) " +
            PROJECT_DEFAULT_ORDER_BY +
            "OFFSET (:from) LIMIT (:to) ";

    static final String PROJECT_IDS_LOCATION_MATCH =
            "SELECT p.id " +
            "FROM " + PROJECT_TABLE + " p " +
            "JOIN " + USER_TABLE + " u ON (p.owner_id = u.id) " +
            "JOIN " + COUNTRY_TABLE + " co ON (u.country_id = co.id) " +
            "JOIN " + STATE_TABLE + " st ON (u.state_id = st.id) " +
            "JOIN " + CITY_TABLE + " ci ON (u.city_id = ci.id) " +
            "WHERE lower(co.country) LIKE (:name) OR lower(st.state) LIKE (:name) OR lower(ci.city) LIKE (:name) " +
            PROJECT_DEFAULT_ORDER_BY +
            "OFFSET (:from) LIMIT (:to) ";

    static final String PROJECT_IDS_CATEGORY_COST_MATCH =
            "SELECT p.id " +
            "FROM " + PROJECT_TABLE + " p " +
            "JOIN " + PROJECT_CATEGORIES_TABLE + " pcat ON (p.id = pcat.project_id) " +
            "JOIN " + CATEGORIES_TABLE + " cat ON (pcat.category_id = cat.id) "+
            "WHERE pcat.category_id IN (:categories) AND p.cost >= (:min) AND p.cost <= (:max) " +
            PROJECT_DEFAULT_ORDER_BY +
            "OFFSET (:from) LIMIT (:to) ";

    static final String PROJECT_IDS_FULL_NAME_MATCH =
            "SELECT p.id " +
            "FROM " + PROJECT_TABLE + " p " +
            "JOIN " + USER_TABLE + " u ON (p.owner_id = u.id) " +
            "WHERE lower(u.first_name) LIKE (:name) OR lower(u.last_name) LIKE (:name) " +
            PROJECT_DEFAULT_ORDER_BY +
            "OFFSET (:from) LIMIT (:to) ";

    static final String PROJECT_IDS_EMAIL_MATCH =
            "SELECT p.id " +
            "FROM " + PROJECT_TABLE + " p " +
            "JOIN " + USER_TABLE + " u ON (p.owner_id = u.id) " +
            "WHERE lower(u.email) LIKE (:name) " +
            PROJECT_DEFAULT_ORDER_BY +
            "OFFSET (:from) LIMIT (:to) ";



    static final String PROJECT_COUNT_ALL_NAME_MATCH =
            "SELECT COUNT(*) " +
            "FROM " + PROJECT_TABLE + " p " +
            "JOIN " + USER_TABLE + " u ON (p.owner_id = u.id) " +
            "JOIN " + COUNTRY_TABLE + " co ON (u.country_id = co.id) " +
            "JOIN " + STATE_TABLE + " st ON (u.state_id = st.id) " +
            "JOIN " + CITY_TABLE + " ci ON (u.city_id = ci.id) " +
            "WHERE lower(p.project_name) LIKE (:name) " +
            "OR lower(u.first_name) LIKE (:name) OR lower(u.last_name) LIKE (:name) " +
            "OR lower(co.country) LIKE (:name) OR lower(st.state) LIKE (:name) OR lower(ci.city) LIKE (:name) " +
            "OR lower(u.email) LIKE (:name) " +
            "OR lower(p.summary) LIKE (:name) ";

    static final String PROJECT_COUNT_EMAIL_MATCH =
            "SELECT COUNT(*) " +
            "FROM " + PROJECT_TABLE + " p " +
            "JOIN " + USER_TABLE + " u ON (p.owner_id = u.id) " +
            "WHERE lower(u.email) LIKE (:name) ";

    static final String PROJECT_COUNT_NAME_SUMMARY_MATCH =
            "SELECT COUNT(*) " +
            "FROM " + PROJECT_TABLE + " p " +
            "WHERE lower(p.project_name) LIKE (:name) OR lower(p.summary) LIKE (:name) ";

    static final String PROJECT_COUNT_FULL_NAME_MATCH =
            "SELECT COUNT(*) " +
            "FROM " + PROJECT_TABLE + " p " +
            "JOIN " + USER_TABLE + " u ON (p.owner_id = u.id) " +
            "WHERE lower(u.first_name) LIKE (:name) OR lower(u.last_name) LIKE (:name) ";

    static final String PROJECT_COUNT_LOCATION_MATCH =
            "SELECT COUNT(*) " +
            "FROM " + PROJECT_TABLE + " p " +
            "JOIN " + USER_TABLE + " u ON (p.owner_id = u.id) " +
            "JOIN " + COUNTRY_TABLE + " co ON (u.country_id = co.id) " +
            "JOIN " + STATE_TABLE + " st ON (u.state_id = st.id) " +
            "JOIN " + CITY_TABLE + " ci ON (u.city_id = ci.id) " +
            "WHERE  lower(co.country) LIKE (:name) OR lower(st.state) LIKE (:name) OR lower(ci.city) LIKE (:name) ";

    static final String PROJECT_COUNT_COST_MATCH =
            "SELECT COUNT(*) " +
            "FROM " + PROJECT_TABLE + " " +
            "WHERE cost >= ? AND cost <= ? ";

    static final String PROJECT_COUNT_CATEGORY_COST_MATCH =
            "SELECT COUNT(*) " +
            "FROM " + PROJECT_TABLE + " p " +
            "JOIN " + USER_TABLE + " u ON (p.owner_id = u.id) " +
            "JOIN " + COUNTRY_TABLE + " co ON (u.country_id = co.id) " +
            "JOIN " + STATE_TABLE + " st ON (u.state_id = st.id) " +
            "JOIN " + CITY_TABLE + " ci ON (u.city_id = ci.id) " +
            "JOIN " + PROJECT_CATEGORIES_TABLE + " pcat ON (p.id = pcat.project_id) " +
            "JOIN " + CATEGORIES_TABLE + " cat ON (pcat.category_id = cat.id) " +
            "WHERE pcat.category_id IN (:categories) " +
            "AND p.cost >= (:min)  AND p.cost <= (:max) ";

    static final String PROJECT_COUNT_FAVORITE =
            "SELECT COUNT(*) " +
            "FROM " + FAVORITES_TABLE + " " +
            "WHERE project_id = ? ";

    static final String PROJECT_ADD_HIT =
            "UPDATE " + PROJECT_TABLE + " SET " +
            "hits = hits + 1 " +
            "WHERE id = ? ";

    static final String PROJECT_IMAGE =
            "SELECT p.images " +
            "FROM " + PROJECT_TABLE + " p " +
            "WHERE p.id = ? ";

    static final String PROJECT_IS_FAVORITE_BY_ID_USER =
            "SELECT " +
            "CASE WHEN f.user_id isnull THEN 0 ELSE 1 END AS isFav " +
            "FROM (VALUES (%s)) v(p_id) LEFT OUTER JOIN " +
            "(SELECT * FROM favorites WHERE user_id = ?) AS f ON v.p_id = f.project_id ";
}
