/*
** Countries, provinces and cities lists.
** Each references the previous one.
** To avoid user manual input.
*/
CREATE TABLE countries (
    -- Country ID.
    id          SERIAL PRIMARY KEY,
    -- Country name and phone prefix.
    country     VARCHAR(25),
    prefix      SMALLINT,
);

CREATE TABLE provinces (
    -- Province ID and respective country ID.
    id          SERIAL PRIMARY KEY,
    country_id  INT REFERENCES countries ON DELETE CASCADE,
    -- Province name. Max 25 characters.
    province    VARCHAR(25),
);

CREATE TABLE cities (
    -- City ID and respective province ID.
    id          SERIAl PRIMARY KEY,
    prov_id     INT REFERENCES provinces ON DELETE CASCADE,
    -- City name. Max 25 characters.
    city        VARCHAR(25),
);


/*
** User roles.
** Entrepreneur, Investor (natural or legal person).
*/
CREATE TABLE roles (
    -- User type Id and name.
    id          SERIAL PRIMARY KEY,
    user_role   VARCHAR(15) NOT NULL
);


/*
** All users table.
*/
CREATE TABLE users (
    -- ID. Serial. Primary Key.
    id          SERIAL PRIMARY KEY,
    -- Role of user.
    role_id     INT REFERENCES roles ON DELETE RESTRICT,

    -- PERSONAL INFORMATION
    -- First and last names. Max 25 characters each.
    first_name  VARCHAR(25) NOT NULL,
    last_name   VARCHAR(25) NOT NULL,
    -- CUIT/CUIL/DNI something.
    real_id     VARCHAR(15) NOT NULL,
    -- Country, province, city.
    country_id  INT REFERENCES countries ON DELETE RESTRICT,
    prov_id     INT REFERENCES provinces ON DELETE RESTRICT,
    city_id     INT REFERENCES cities ON DELETE RESTRICT,
    -- Aux date. Format should be dd/mm/yyy. For natural persons its birth date.
    aux_date    DATE NOT NULL,

    -- CONTACT INFORMATION
    -- Email. Max 25 characters.
    email       VARCHAR(25) NOT NULL,
    -- Phone number. Optional. Max length is 25. Allows all international phones.
    phone       VARCHAR(25),
    -- LinkedIn Profile/Chat Link. Optional. Max 100 characters.
    linkedin    VARCHAR(100),

    -- EXTRA INFORMATION
    -- Profile picture URN. Optional. Max 100 characters.
    profile_pic VARCHAR(100),
    -- Trust Index. -100 to 100 range. Default 0.
    trust_index SMALLINT NOT NULL DEFAULT 0,
    -- Registration date.
    join_date   DATE NOT NULL
);