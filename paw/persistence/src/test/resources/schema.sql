-- HSQL WONT ACCEPT NOT NULL AND DEFAULT TOGETHER
-- HSQL WONT ACCEPT LOCAL DATE AS DATE, USE DATE
-- HIBERNATE ISSUE WITH REFERENCES - CREATED BY HIBERNATE ONLY
/****************************************
**     CREATE ALL NECESSARY TABLES     **
*****************************************/

/*
** Countries, provinces and cities lists.
** Each references the previous one.
** To avoid user manual input.
*/
CREATE TABLE IF NOT EXISTS countries (
    id              SMALLINT PRIMARY KEY,
    country         VARCHAR(50) NOT NULL,

    -- EXTRA INFO
    iso2            VARCHAR(2),
    phonecode       VARCHAR(10),
    currency        VARCHAR(10),
    last_update     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    locale          VARCHAR(5)
);

CREATE TABLE IF NOT EXISTS states (
    id              INT PRIMARY KEY,
    state           VARCHAR(75) NOT NULL,

    country_id      INT REFERENCES countries ON DELETE CASCADE,
    iso2            VARCHAR(10),
    last_update     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cities (
    id              INT PRIMARY KEY,
    city            VARCHAR(75) NOT NULL,
    state_id        INT REFERENCES states ON DELETE CASCADE,
    last_update     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


/*
** User roles.
** Entrepreneur, Investor (natural or legal person).
*/
CREATE TABLE IF NOT EXISTS roles (
    id              SMALLINT PRIMARY KEY,
    user_role       VARCHAR(25) NOT NULL
);


CREATE TABLE IF NOT EXISTS user_location (
    id                  INTEGER IDENTITY PRIMARY KEY,
    country_id          INT NOT NULL,
    state_id            INT NOT NULL,
    city_id             INT NOT NULL
);

CREATE TABLE IF NOT EXISTS user_images (
    id                 INT PRIMARY KEY,
    image              VARCHAR(50)
);

/*
** All users table.
*/
CREATE TABLE IF NOT EXISTS users (
    id              INTEGER IDENTITY PRIMARY KEY,
    role_id         INT REFERENCES roles ON DELETE RESTRICT,

    -- PERSONAL INFO
    first_name      VARCHAR(25) NOT NULL,
    last_name       VARCHAR(25) NOT NULL,
    -- CUIT/CUIL/DNI something.
    real_id         VARCHAR(15) NOT NULL,
    -- Location.
    location_id     INT NOT NULL,
    -- Aux date. Format should be dd/mm/yyy. For natural persons its birth date.
    aux_date        DATE NOT NULL,

    -- CONTACT INFO
    email           VARCHAR(255) NOT NULL,
    password        VARCHAR(76),
    phone           VARCHAR(25),
    linkedin        VARCHAR(100),

    -- EXTRA INFO
    join_date       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    verified        BOOLEAN DEFAULT FALSE,
    locale          VARCHAR(5) NOT NULL,
    image_id        INT
);


/*
** All projects created.
** Project basic data. Each project has stages.
*/
CREATE TABLE IF NOT EXISTS projects (
    id              INTEGER IDENTITY PRIMARY KEY,
    owner_id        INT NOT NULL,

    -- TOP INFO
    project_name    VARCHAR(50) NOT NULL,
    summary         VARCHAR(250) NOT NULL,
    cost            INT DEFAULT 0,

    -- EXTRA INFO
    publish_date    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    update_date     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    hits            INT DEFAULT 0,
    funded          BOOLEAN DEFAULT FALSE,
    message_count   INT DEFAULT 0
);


/*
** Project categories. Each project can have many categories.
** Categories also allow subcategories.
*/
CREATE TABLE IF NOT EXISTS categories (
    id              INTEGER IDENTITY PRIMARY KEY,
    parent_id          INT REFERENCES categories ON DELETE CASCADE,
    category        VARCHAR(25) NOT NULL
);

CREATE TABLE IF NOT EXISTS project_categories (
    project_id      INT NOT NULL,
    category_id     INT NOT NULL,

    PRIMARY KEY (project_id, category_id)
);


/*
** Favourites of each user
*/
CREATE TABLE IF NOT EXISTS favorites (
	user_id		INT NOT NULL,
	project_id 	INT NOT NULL,

	PRIMARY KEY (user_id, project_id)
);

/*
** Messages table
*/
CREATE TABLE IF NOT EXISTS messages (
    id                  INTEGER IDENTITY PRIMARY KEY,

    content_comment     VARCHAR(250),
    content_offer       VARCHAR(100) NOT NULL,
    content_interest    VARCHAR(100),

    publish_date        TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    accepted            BOOLEAN,

    owner_id           INT NOT NULL,
    investor_id         INT NOT NULL,
    project_id          INT NOT NULL,

    i_to_e              BOOLEAN NOT NULL,
    seen                BOOLEAN NOT NULL,
    expiry_date         TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS project_images (
    id                  INT PRIMARY KEY,
    project_id          INT NOT NULL,
    image               VARCHAR(50),
    main                BOOLEAN NOT NULL
);


