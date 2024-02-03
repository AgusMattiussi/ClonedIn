package ar.edu.itba.paw.models.utils;

import ar.edu.itba.paw.models.User;

import java.util.Comparator;

public class YearsOfExpComparator implements Comparator<User> {

    @Override
    public int compare(User o1, User o2) {
        return Integer.compare(o1.getYearsOfExperience(), o2.getYearsOfExperience());
    }
}
