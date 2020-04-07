/*
** Countries, provinces and cities lists.
** Each references the previous one.
** To avoid user manual input.
*/
CREATE TABLE IF NOT EXISTS countries (
    id              SERIAL PRIMARY KEY,
    country         VARCHAR(25),

    -- Prefix of the country and length of the number after the prefix.
    prefix          SMALLINT,
    phone_length    SMALLINT
);

CREATE TABLE IF NOT EXISTS provinces (
    id              SERIAL PRIMARY KEY,
    country_id      INT REFERENCES countries ON DELETE CASCADE,

    province        VARCHAR(25)
);

CREATE TABLE IF NOT EXISTS cities (
    id              SERIAl PRIMARY KEY,
    province_id     INT REFERENCES provinces ON DELETE CASCADE,

    city            VARCHAR(25)
);


/*
** User roles.
** Entrepreneur, Investor (natural or legal person).
*/
CREATE TABLE IF NOT EXISTS roles (
    id              SERIAL PRIMARY KEY,
    user_role       VARCHAR(15) NOT NULL
);


/*
** All users table.
*/
CREATE TABLE IF NOT EXISTS users (
    id              SERIAL PRIMARY KEY,
    role_id         INT REFERENCES roles ON DELETE RESTRICT,

    -- PERSONAL INFO
    first_name      VARCHAR(25) NOT NULL,
    last_name       VARCHAR(25) NOT NULL,
    -- CUIT/CUIL/DNI something.
    real_id         VARCHAR(15) NOT NULL,
    -- Location.
    country_id      INT REFERENCES countries ON DELETE RESTRICT,
    province_id     INT REFERENCES provinces ON DELETE RESTRICT,
    city_id         INT REFERENCES cities ON DELETE RESTRICT,
    -- Aux date. Format should be dd/mm/yyy. For natural persons its birth date.
    aux_date        DATE NOT NULL,

    -- CONTACT INFO
    email           VARCHAR(25) NOT NULL,
    phone           VARCHAR(25),
    linkedin        VARCHAR(100),

    -- EXTRA INFO
    -- Profile picture URN. Optional. Max 100 characters.
    profile_pic     VARCHAR(100),
    join_date       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- BACK OFFICE INFO
    -- Trust Index. -100 to 100 range. Default 0.
    trust_index     SMALLINT NOT NULL DEFAULT 0
);


/*
** All projects created.
** Project basic data. Each project has stages.
*/
CREATE TABLE IF NOT EXISTS projects (
    id              SERIAL PRIMARY KEY,
    owner_id        INT REFERENCES users ON DELETE CASCADE,

    -- TOP INFO
    project_name    VARCHAR(25) NOT NULL,
    summary         VARCHAR(250) NOT NULL,
    cost            INT NOT NULL DEFAULT 0,

    -- EXTRA INFO
    publish_date    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    update_date     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    images          BOOLEAN NOT NULL DEFAULT false,
    hits            INT NOT NULL DEFAULT 0,

    -- BACK OFFICE INFO
    aproved         BOOLEAN NOT NULL DEFAULT false,
    profit_index    INT NOT NULL DEFAULT 0,
    risk_index      INT NOT NULL DEFAULT 0
);


/*
** Project categories. Each project can have many categories.
** Categories also allow subcategories.
*/
CREATE TABLE IF NOT EXISTS categories (
    id              SERIAL PRIMARY KEY,
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
    id              SERIAL PRIMARY KEY,
    type_name       VARCHAR(25) NOT NULL
);

CREATE TABLE IF NOT EXISTS stages (
    project_id      INT REFERENCES projects ON DELETE CASCADE,
    stage_number    SMALLINT NOT NULL,

    -- TOP INFO
    type_id         INT REFERENCES stage_types,
    duration        INTERVAL NOT NULL,
    key_result      VARCHAR(50) NOT NULL,
    cost            INT NOT NULL DEFAULT 0,

    PRIMARY KEY (project_id, stage_number)
);

/*
** Resources per stage tables.
** Infrastructure, human and law resources.
*/
CREATE TABLE IF NOT EXISTS infrastructure_items (
    id              SERIAL PRIMARY KEY,
    item_name       VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS infrastructure_resources (
    project_id      INT NOT NULL,
    stage_number    SMALLINT NOT NULL,
    item_number     SMALLINT NOT NULL,

    -- INFO
    item_id         INT REFERENCES infrastructure_items ON DELETE RESTRICT,
    cost            INT NOT NULL,

    FOREIGN KEY (project_id, stage_number) REFERENCES stages (project_id, stage_number) ON DELETE CASCADE,
    PRIMARY KEY (project_id, stage_number, item_number)
);

CREATE TABLE IF NOT EXISTS law_items (
    id              SERIAL PRIMARY KEY,
    item_name       VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS law_resources (
    project_id      INT NOT NULL,
    stage_number    SMALLINT NOT NULL,
    item_number     SMALLINT NOT NULL,

    -- INFO
    item_id         INT REFERENCES law_items ON DELETE RESTRICT,
    cost            INT NOT NULL,

    FOREIGN KEY (project_id, stage_number) REFERENCES stages (project_id, stage_number) ON DELETE CASCADE,
    PRIMARY KEY (project_id, stage_number, item_number)
);

CREATE TABLE IF NOT EXISTS worker_types (
    id              SERIAL PRIMARY KEY,
    worker_type     VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS human_resources (
    project_id      INT NOT NULL,
    stage_number    SMALLINT NOT NULL,
    item_number     SMALLINT NOT NULL,

    -- INFO
    worker_type_id  INT REFERENCES worker_types ON DELETE RESTRICT,
    total_hours     SMALLINT NOT NULL,
    cost_hour       INT NOT NULL DEFAULT 0,

    FOREIGN KEY (project_id, stage_number) REFERENCES stages (project_id, stage_number) ON DELETE CASCADE,
    PRIMARY KEY (project_id, stage_number, item_number)
);