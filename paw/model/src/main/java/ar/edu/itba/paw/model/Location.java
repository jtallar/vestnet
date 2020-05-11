package ar.edu.itba.paw.model;

/**
 * Models a location object with Country, State and City.
 */
public class Location {
    private final Country country;
    private final State state;
    private final City city;

    public Location(Country country, State state, City city) {
        this.country = country;
        this.state = state;
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public State getState() {
        return state;
    }

    public City getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Location{" +
                "country=" + country +
                ", state=" + state +
                ", city=" + city +
                '}';
    }

    /**
     * Model of a Country.
     */
    public static class Country {
        private final int id;
        private final String name;
        private final String isoCode;
        private final String phoneCode;
        private final String currency;
        private final String locale;

        public Country(int id, String name, String isoCode, String phoneCode, String currency, String locale) {
            this.id = id;
            this.name = name;
            this.isoCode = isoCode;
            this.phoneCode = phoneCode;
            this.currency = currency;
            this.locale = locale;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getIsoCode() {
            return isoCode;
        }

        public String getPhoneCode() {
            return phoneCode;
        }

        public String getCurrency() {
            return currency;
        }

        public String getLocale() {
            return locale;
        }

        @Override
        public String toString() {
            return "Country{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", isoCode='" + isoCode + '\'' +
                    ", phoneCode='" + phoneCode + '\'' +
                    ", currency='" + currency + '\'' +
                    ", locale='" + locale + '\'' +
                    '}';
        }
    }

    /**
     * Model of a State.
     */
    public static class State {
        private final int id;
        private final String name;
        private final String isoCode;

        public State(int id, String name, String isoCode) {
            this.id = id;
            this.name = name;
            this.isoCode = isoCode;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getIsoCode() {
            return isoCode;
        }

        @Override
        public String toString() {
            return "State{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", isoCode='" + isoCode + '\'' +
                    '}';
        }
    }

    /**
     * Model of a City.
     */
    public static class City {
        private final int id;
        private final String name;

        public City(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "City{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
