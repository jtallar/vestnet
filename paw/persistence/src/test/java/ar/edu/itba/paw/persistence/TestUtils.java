package ar.edu.itba.paw.persistence;

import javax.persistence.EntityManager;
import java.math.BigInteger;

/** Package-Private */ class TestUtils {

    static int countRowsInTable(EntityManager entityManager, String table) {
        return ((BigInteger) entityManager.createNativeQuery("SELECT COUNT(*) FROM " + table).getSingleResult()).intValue();
    }
}
