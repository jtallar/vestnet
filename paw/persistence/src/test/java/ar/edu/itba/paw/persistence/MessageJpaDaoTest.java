package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.FilterCriteria;
import ar.edu.itba.paw.model.components.OrderField;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.model.components.PageRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

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
    private static final String OFFER = "Offer here.";
    private static final String INTEREST = "Interest here.";

    private static final int COUNTRY_ID = 1;
    private static final int STATE_ID = 2;
    private static final int CITY_ID = 3;
    private static final int ROLE_ID = 3;
    private static final int USER_ID = 1;
    private static final int PROJECT_ID = 1;

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
    }

    @Test
    public void testCreate()  {
        // 1 - Setup - Create both users, and project
        Number investorId = createUser();
        Number entrepreneurId = createUser();
        Number projectId = createProject(entrepreneurId);
        Message.MessageContent content = new Message.MessageContent(MESSAGE, OFFER, INTEREST);

        // 2 - Execute
        Message message = messageJpaDao.create(content, new User(investorId.longValue()), new User(entrepreneurId.longValue()), new Project(projectId.longValue()));


        // 3 - Assert
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, MESSAGE_TABLE));
    }


    @Test
    public void testGetConversationsIfTableEmpty() {
        // 1 - Setup - Empty message tables
        Number investorId = createUser();
        Number entrepreneurId = createUser();
        Number projectId = createProject(entrepreneurId);
        List<FilterCriteria> filters = fillConversationFilters(investorId, entrepreneurId, projectId);

        // 2 - Execute
        Page<Message> messages = messageJpaDao.findAll(filters, OrderField.DATE_DESCENDING, new PageRequest(1, 10));

        // 3 - Assert
        assertTrue(messages.getContent().isEmpty());
    }




    @Test
     public void testGetConversationsIfTableNotEmpty() {
        // 1 - Setup - Create message tables
        Number investorId = createUser();
        Number entrepreneurId = createUser();
        Number projectId = createProject(entrepreneurId);
        createMessage(investorId, entrepreneurId, projectId, false);
        createMessage(investorId,entrepreneurId, projectId, true);

        List<FilterCriteria> filters = fillConversationFilters(investorId, entrepreneurId, projectId);

        // 2 - Execute
        Page<Message> messages = messageJpaDao.findAll(filters, OrderField.DATE_DESCENDING, new PageRequest(1, 10));

        // 3 - Assert - Quantity, Name
        assertEquals(2, messages.getContent().size());
        assertEquals(MESSAGE, messages.getContent().get(0).getContent().getMessage());
    }

    @Test
    public void testGetUnreadMessagesIfTableEmpty() {
        // 1 - Setup - Empty message tables
        Number investorId = createUser();
        Number entrepreneurId = createUser();
        Number projectId = createProject(entrepreneurId);
        List<FilterCriteria> filters = fillUnreadFilter(entrepreneurId, projectId);

        // 2 - Execute
        List<Message> messages = messageJpaDao.findAll(filters, OrderField.DATE_DESCENDING);

        // 3 - Assert
        assertTrue(messages.isEmpty());
    }

    @Test
    public void testGetUnreadMessagesIfTableNotEmpty() {
        // 1 - Setup - Create message tables
        Number investorId = createUser();
        Number entrepreneurId = createUser();
        Number projectId = createProject(entrepreneurId);
        createMessage(investorId, entrepreneurId, projectId, false);
        List<FilterCriteria> filters = fillUnreadFilter(entrepreneurId, projectId);

        // 2 - Execute
        List<Message> messages = messageJpaDao.findAll(filters, OrderField.DATE_DESCENDING);

        // 3 - Assert
        assertEquals(1, messages.size());
        assertEquals(MESSAGE, messages.get(0).getContent().getMessage());
        assertNull(messages.get(0).getAccepted());
    }

    /**
     * Auxiliary functions
     */

    /**
     * Creates a project.
     * @return The category auto generated id.
     */
    public Number createProject(Number ownerId) {
        Map<String, Object> project = new HashMap<>();
        project.put("project_name", "Project name here.");
        project.put("summary", "Summary here.");
        project.put("owner_id", ownerId);
        project.put("cost", 10000);
        project.put("hits", 0);
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
     * @param senderId The user sender id.
     * @param receiverId The user receiver id.
     * @param projectId The project id.
     * @param changeOrder Boolean to change order.
     * @return Created message id.
     */
    private Number createMessage(Number senderId, Number receiverId, Number projectId, boolean changeOrder) {
        Map<String, Object> message = new HashMap<>();
        message.put("content_message", MESSAGE);
        message.put("content_offer", OFFER);
        message.put("content_interest", INTEREST);
        message.put("sender_id", changeOrder ? receiverId : senderId);
        message.put("receiver_id", changeOrder ? senderId : receiverId);
        message.put("project_id", projectId);
        return jdbcInsertMessage.executeAndReturnKey(message);
    }


    /**
     * Creates filters to get all the conversation.
     * @param senderId Senders unique id.
     * @param receiverId Receivers unique id.
     * @param projectId Project unique id.
     * @return The list of filters.
     */
    private List<FilterCriteria> fillConversationFilters(Number senderId, Number receiverId, Number projectId) {
        List<FilterCriteria> filters = new ArrayList<>();
        filters.add(new FilterCriteria("receiver", new User(receiverId.intValue())));
        filters.add(new FilterCriteria("receiver", new User(senderId.intValue())));
        filters.add(new FilterCriteria("sender", new User(receiverId.intValue())));
        filters.add(new FilterCriteria("sender", new User(senderId.intValue())));
        filters.add(new FilterCriteria("project", new Project(projectId.intValue())));
        return filters;
    }


    /**
     * Creates filters for searching the unread messages.
     * @param userId The unique user id.
     * @param projectId The unique project id.
     * @return The list with the criteria.
     */
    public List<FilterCriteria> fillUnreadFilter(Number userId, Number projectId) {
        List<FilterCriteria> filters = new ArrayList<>();
        filters.add(new FilterCriteria("receiver", new User(userId.intValue())));
        filters.add(new FilterCriteria("project", new Project(projectId.intValue())));
        filters.add(new FilterCriteria("unread", true));
        return filters;
    }

}
