package ar.edu.itba.paw.model.comparators;

import ar.edu.itba.paw.model.Project;

import java.util.Comparator;

public class DateComparator implements Comparator<Project> {
    @Override
    public int compare(Project o1, Project o2) {
        return o1.getUpdateDate().compareTo(o2.getPublishDate());
    }
}
