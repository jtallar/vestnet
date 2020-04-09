package ar.edu.itba.paw.model;

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

    public static class Country {
        private final int id;
        private final String name;
        private final String isoCode;
        private final String phoneCode;
        private final String currency;

        public Country(int id, String name, String isoCode, String phoneCode, String currency) {
            this.id = id;
            this.name = name;
            this.isoCode = isoCode;
            this.phoneCode = phoneCode;
            this.currency = currency;
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
    }

    public static class State {
        private final int id;
        private final String name;
        private final String isoCode;

        public State(int id, String name, String isoCode, String countryId) {
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
    }

    public static class City {
        private final int id;
        private final String name;

        public City(int id, String name, String stateId) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
