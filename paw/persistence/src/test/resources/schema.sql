-- TODO: NO ME DEJA PONER bytea porque ROMPE TODO
-- HSQL WONT ACCEPT NOT NULL AND DEFAULT TOGETHER
-- HSQL WONT ACCEPT LOCAL DATE AS DATE, USE DATE
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
    last_update     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
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
    country_id      INT REFERENCES countries ON DELETE RESTRICT,
    state_id        INT REFERENCES states ON DELETE RESTRICT,
    city_id         INT REFERENCES cities ON DELETE RESTRICT,
    -- Aux date. Format should be dd/mm/yyy. For natural persons its birth date.
    aux_date        DATE NOT NULL,

    -- CONTACT INFO
    email           VARCHAR(255) NOT NULL,
    password        VARCHAR(76),
    phone           VARCHAR(25),
    linkedin        VARCHAR(100),

    -- EXTRA INFO
    -- Profile picture URN. Optional. Max 100 characters.
    profile_pic     VARCHAR(50),
    join_date       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- BACK OFFICE INFO
    -- Trust Index. -100 to 100 range. Default 0.
    trust_index     SMALLINT DEFAULT 0
);


/*
** All projects created.
** Project basic data. Each project has stages.
*/
CREATE TABLE IF NOT EXISTS projects (
    id              INTEGER IDENTITY PRIMARY KEY,
    owner_id        INT REFERENCES users ON DELETE CASCADE,

    -- TOP INFO
    project_name    VARCHAR(50) NOT NULL,
    summary         VARCHAR(250) NOT NULL,
    cost            INT DEFAULT 0,

    -- EXTRA INFO
    publish_date    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    update_date     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    images          VARCHAR(50),
    hits            INT DEFAULT 0,

    -- BACK OFFICE INFO
    aproved         BOOLEAN DEFAULT false,
    profit_index    INT DEFAULT 0,
    risk_index      INT DEFAULT 0
);


/*
** Project categories. Each project can have many categories.
** Categories also allow subcategories.
*/
CREATE TABLE IF NOT EXISTS categories (
    id              INTEGER IDENTITY PRIMARY KEY,
    parent          INT REFERENCES categories ON DELETE CASCADE,
    category        VARCHAR(25) NOT NULL
);

CREATE TABLE IF NOT EXISTS project_categories (
    project_id      INT REFERENCES projects ON DELETE CASCADE,
    category_id     INT REFERENCES categories ON DELETE CASCADE,

    PRIMARY KEY (project_id, category_id)
);


/*
** Stages. Each project needs to have at least one.
** They determine cost. Additional info for investors.
*/
CREATE TABLE IF NOT EXISTS stage_types (
    id              INTEGER IDENTITY PRIMARY KEY,
    category_id     INT REFERENCES categories ON DELETE SET NULL,
    type_name       VARCHAR(25) NOT NULL
);

CREATE TABLE IF NOT EXISTS stages (
    project_id      INT REFERENCES projects ON DELETE CASCADE,
    stage_number    SMALLINT NOT NULL,

    -- TOP INFO
    type_id         INT REFERENCES stage_types ON DELETE SET NULL,
--     TODO: CHECK IF INTERVAL DAY o QUE INTERVAL TYPE
    duration        INTERVAL DAY NOT NULL,
    key_result      VARCHAR(50) NOT NULL,
    cost            INT DEFAULT 0,

    PRIMARY KEY (project_id, stage_number)
);

/*
** Resources per stage tables.
** Infrastructure, human and law resources.
*/
-- Human, infrastructure and law for example.
CREATE TABLE IF NOT EXISTS resource_types (
    id              INTEGER IDENTITY PRIMARY KEY,
    type_name       VARCHAR(20) NOT NULL
);

-- All items for each type of resource. Ex Software Developer -> Human resources.
CREATE TABLE IF NOT EXISTS resource_items (
    id              INTEGER IDENTITY PRIMARY KEY,
    type_id         INT REFERENCES resource_types ON DELETE RESTRICT,
    category_id     INT REFERENCES categories ON DELETE SET NULL,
    item_name       VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS resources (
    project_id      INT NOT NULL,
    stage_number    SMALLINT NOT NULL,
    item_number     SMALLINT NOT NULL,

    -- INFO
    item_id         INT REFERENCES resource_items ON DELETE RESTRICT,
    quantity        SMALLINT DEFAULT 1,
    cost            INT NOT NULL,

    FOREIGN KEY (project_id, stage_number) REFERENCES stages (project_id, stage_number) ON DELETE CASCADE,
    PRIMARY KEY (project_id, stage_number, item_number)
);

/*
** Favourites of each user
*/
CREATE TABLE IF NOT EXISTS favorites (
	user_id		INT REFERENCES users ON DELETE CASCADE,
	project_id 	INT REFERENCES projects ON DELETE CASCADE,

	PRIMARY KEY (user_id, project_id)
);

/*
** Messages table
*/
CREATE TABLE IF NOT EXISTS messages (
    id                  INTEGER IDENTITY PRIMARY KEY,

    content_message     VARCHAR(250),
    content_offer       VARCHAR(100) NOT NULL,
    content_interest    VARCHAR(100),

    publish_date        TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    accepted            BOOLEAN,

    sender_id           INT REFERENCES users ON DELETE CASCADE,
    receiver_id         INT REFERENCES users ON DELETE CASCADE,
    project_id          INT REFERENCES projects ON DELETE CASCADE
);
