package ar.edu.itba.paw.model.comparators;

import ar.edu.itba.paw.model.Project;

import java.util.Comparator;

public class HitsComparator implements Comparator<Project> {

    @Override
    public int compare(Project o1, Project o2) {
        if (o1.getHits() == o2.getHits()) return (int) (o1.getId() - o2.getId());
        return (int) (o1.getHits() - o2.getHits());
    }
}
