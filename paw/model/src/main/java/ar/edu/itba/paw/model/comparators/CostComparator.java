package ar.edu.itba.paw.model.comparators;

import ar.edu.itba.paw.model.Project;

import java.util.Comparator;

public class CostComparator implements Comparator<Project> {
    @Override
    public int compare(Project o1, Project o2) {
        return (int) (o1.getCost() - o2.getCost());
    }

    @Override
    public Comparator<Project> reversed() {
        return (o1, o2) -> (int) (o2.getCost()-o1.getCost());
    }
}
