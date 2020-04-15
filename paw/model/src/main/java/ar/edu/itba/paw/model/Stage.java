package ar.edu.itba.paw.model;

import org.joda.time.Interval;

import java.util.List;

public class Stage {
    private final long id;
    private final int number;
    private final String keyResult;
    private final int cost;
    private final StageType type; // TODO> USO EL MISMO CATEGORY?
    private final Interval duration;
    private final List<StageResource> resources;

    public Stage(long id, int number, String keyResult, int cost, StageType type, Interval duration, List<StageResource> resources) {
        this.id = id;
        this.number = number;
        this.keyResult = keyResult;
        this.cost = cost;
        this.type = type;
        this.duration = duration;
        this.resources = resources;
    }

    public long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getKeyResult() {
        return keyResult;
    }

    public int getCost() {
        return cost;
    }

    public StageType getType() {
        return type;
    }

    public Interval getDuration() {
        return duration;
    }

    public List<StageResource> getResources() {
        return resources;
    }

    public class StageResource {
        private final String number;
        private final int quantity;
        private final int cost;
        private final long id;
        private final String name;
        private final String type;
        private final Category category;

        public StageResource(String number, int quantity, int cost, long id, String name, String type, Category category) {
            this.number = number;
            this.quantity = quantity;
            this.cost = cost;
            this.id = id;
            this.name = name;
            this.type = type;
            this.category = category;
        }

        public String getNumber() {
            return number;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getCost() {
            return cost;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public Category getCategory() {
            return category;
        }
    }

    public class StageType { // TODO> VER SI HACE FALTA
        private final long id;
        private final String name;
        private final Category parent;

        public StageType(long id, String name, Category parent) {
            this.id = id;
            this.name = name;
            this.parent = parent;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Category getParent() {
            return parent;
        }
    }

}