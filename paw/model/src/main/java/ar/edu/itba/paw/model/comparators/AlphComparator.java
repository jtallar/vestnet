package ar.edu.itba.paw.model.comparators;

import ar.edu.itba.paw.model.Project;

import java.util.Comparator;

// todo: estos comparators van aca?
public class AlphComparator implements Comparator<Project> {
    @Override
    public int compare(Project o1, Project o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
