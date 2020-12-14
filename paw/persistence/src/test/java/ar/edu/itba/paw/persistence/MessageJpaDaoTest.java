package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.*;
import ar.edu.itba.paw.model.enums.OrderField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class MessageJpaDaoTest {

    private static final String MESSAGE_TABLE = "messages";
    private static final String COUNTRIES_TABLE = "countries";
    private static final String STATES_TABLE = "states";
    private static final String CITIES_TABLE = "cities";
    private static final String ROLES_TABLE = "roles";
    private static final String USERS_TABLE = "users";
    private static final String PROJECTS_TABLE = "projects";
    private static final String LOCATIONS_TABLE = "user_location";


    private static final String MESSAGE = "Message here.";
    private static final long OFFER = 5000;
    private static final String INTEREST = "Interest here.";
    private static final boolean DIRECTION = true;
    private static final int EXPIRY = 5;


    private static final int COUNTRY_ID = 1;
    private static final int STATE_ID = 2;
    private static final int CITY_ID = 3;
    private static final int ROLE_ID = 3;
    private static final int USER_ID = 1;
    private static final int PROJECT_ID = 1;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MessageJpaDao messageJpaDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertMessage;
    private SimpleJdbcInsert jdbcInsertCountry, jdbcInsertState, jdbcInsertCity, jdbcInsertLocation;
    private SimpleJdbcInsert jdbcInsertRole, jdbcInsertUser, jdbcInsertProject;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsertMessage = new SimpleJdbcInsert(dataSource)
                .usingGeneratedKeyColumns("id")
                .withTableName(MESSAGE_TABLE);
        jdbcInsertCountry = new SimpleJdbcInsert(dataSource)
                .withTableName(COUNTRIES_TABLE);
        jdbcInsertState = new SimpleJdbcInsert(dataSource)
                .withTableName(STATES_TABLE);
        jdbcInsertCity = new SimpleJdbcInsert(dataSource)
                .withTableName(CITIES_TABLE);
        jdbcInsertRole = new SimpleJdbcInsert(dataSource)
                .withTableName(ROLES_TABLE);
        jdbcInsertUser = new SimpleJdbcInsert(dataSource)
                .usingGeneratedKeyColumns("id")
                .withTableName(USERS_TABLE);
        jdbcInsertProject = new SimpleJdbcInsert(dataSource)
                .usingGeneratedKeyColumns("id")
                .withTableName(PROJECTS_TABLE);
        jdbcInsertLocation = new SimpleJdbcInsert(dataSource)
                .usingGeneratedKeyColumns("id")
                .withTableName(LOCATIONS_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PROJECTS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, CITIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, STATES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, COUNTRIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, ROLES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, LOCATIONS_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, MESSAGE_TABLE);

        createLocation();
        createRole();
        createProject(createUser());
    }

    @Test
    public void testCreate()  {
        // 1 - Setup - Create both users, and project
        Number investorId = createUser();
        Number entrepreneurId = createUser();
        Number projectId = createProject(entrepreneurId);
        Message.MessageContent content = new Message.MessageContent(MESSAGE, OFFER, INTEREST);
        Message message = new Message(content, new User(entrepreneurId.longValue()), new User(investorId.longValue()), new Project(projectId.longValue()),
                DIRECTION, EXPIRY);

        // 2 - Execute
        messageJpaDao.create(message);


        // 3 - Assert
        assertEquals(1, TestUtils.countRowsInTable(entityManager, MESSAGE_TABLE));
    }


    @Test
    public void testGetUnreadMessagesIfTableEmpty() {
        // 1 - Setup - Empty message tables
        Number investorId = createUser();
        Number entrepreneurId = createUser();
        Number projectId = createProject(entrepreneurId);
        RequestBuilder request = new MessageRequestBuilder()
                .setOwner(entrepreneurId.longValue())
                .setProject(projectId.longValue())
                .setSeen()
                .setOrder(OrderField.DATE_DESCENDING);

        // 2 - Execute
        List<Message> messages = messageJpaDao.findAll(request);

        // 3 - Assert
        assertTrue(messages.isEmpty());
    }

    @Test
    public void testGetUnreadMessagesIfTableNotEmpty() {
        // 1 - Setup - Create message tables
        Number investorId = createUser();
        Number entrepreneurId = createUser();
        Number projectId = createProject(entrepreneurId);
        createMessage(entrepreneurId, investorId, projectId, true);
        RequestBuilder request = new MessageRequestBuilder()
                .setOwner(entrepreneurId.longValue())
                .setProject(projectId.longValue())
                .setUnseen()
                .setOrder(OrderField.DATE_DESCENDING);

        // 2 - Execute
        List<Message> messages = messageJpaDao.findAll(request);

        // 3 - Assert
        assertEquals(1, messages.size());
        assertEquals(MESSAGE, messages.get(0).getContent().getComment());
        assertNull(messages.get(0).getAccepted());
    }

    @Test
    public void testGetUnreadMessagesCount() {
        // 1 - Setup - Create message tables
        Number investorId = createUser();
        Number entrepreneurId = createUser();
        Number projectId = createProject(entrepreneurId);
        createMessage(entrepreneurId, investorId, projectId, true);

        // 2 - Execute
        long count = messageJpaDao.countEntrepreneurNotifications(entrepreneurId.longValue());

        // 3 - Assert
        assertEquals(1, count);
    }

    /**
     * Auxiliary functions
     */

    /**
     * Creates a project.
     * @return The category auto generated id.
     */
    private Number createProject(Number ownerId) {
        Map<String, Object> project = new HashMap<>();
        project.put("project_name", "Project name here.");
        project.put("summary", "Summary here.");
        project.put("owner_id", ownerId);
        project.put("cost", 10000);
        project.put("hits", 0);
        project.put("message_count", 0);
        return jdbcInsertProject.executeAndReturnKey(project);
    }

    /**
     * Creates a location.
     */
    private void createLocation() {
        Map<String, Object> country = new HashMap<>();
        country.put("id", COUNTRY_ID);
        country.put("country", "Peronlandia");
        jdbcInsertCountry.execute(country);

        Map<String, Object> state = new HashMap<>();
        state.put("id", STATE_ID);
        state.put("state", "Buenos Aires");
        state.put("country_id", COUNTRY_ID);
        jdbcInsertState.execute(state);

        Map<String, Object> city = new HashMap<>();
        city.put("id", CITY_ID);
        city.put("city", "La Matanza");
        city.put("state_id", STATE_ID);
        jdbcInsertCity.execute(city);
    }

    /**
     * Creates a user role.
     */
    private void createRole() {
        Map<String, Object> role = new HashMap<>();
        role.put("id", ROLE_ID);
        role.put("user_role", "Entrepreneur");
        jdbcInsertRole.execute(role);
    }

    /**
     * Creates a user and inserts it on database
     * @return The user id.
     */
    private Number createUser() {
        Number location_id = createUserLocation();

        Map<String, Object> user = new HashMap<>();
        user.put("role_id", ROLE_ID);
        user.put("password", "test-password");
        user.put("first_name", "FirstName");
        user.put("last_name", "LastName");
        user.put("real_id", "RealId");
        user.put("location_id", location_id);
        user.put("aux_date", new Date());
        user.put("email", "test@test.com");
        user.put("phone", "5491100000000");
        user.put("linkedin", "www.linkedin.com/in/test");
        user.put("verified", false);
        user.put("locale", "en");
        return jdbcInsertUser.executeAndReturnKey(user);
    }

    /**
     * Inserts a User location un DB.
     * @return The generated ID.
     */
    private Number createUserLocation() {
        Map<String, Object> location = new HashMap<>();
        location.put("country_id", COUNTRY_ID);
        location.put("state_id", STATE_ID);
        location.put("city_id", CITY_ID);
        return jdbcInsertLocation.executeAndReturnKey(location);
    }


    /**
     * Creates a message and inserts it.
     * @param ownerId The user owner id.
     * @param investorId The user investor id.
     * @param projectId The project id.
     * @param direction Boolean to change order.
     * @return Created message id.
     */
    private Number createMessage(Number ownerId, Number investorId, Number projectId, boolean direction) {
        Map<String, Object> message = new HashMap<>();
        message.put("content_comment", MESSAGE);
        message.put("content_offer", OFFER);
        message.put("content_interest", INTEREST);
        message.put("owner_id", ownerId.longValue());
        message.put("investor_id", investorId.longValue());
        message.put("project_id", projectId.longValue());
        message.put("i_to_e", direction);
        message.put("seen", false);
        return jdbcInsertMessage.executeAndReturnKey(message);
    }
}
