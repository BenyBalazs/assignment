package com.epam.training.ticketservice.data.dto;

import com.epam.training.ticketservice.data.dao.Movie;
import org.junit.jupiter.api.BeforeEach;

public class MovieTest {

    private Movie movie;

    @BeforeEach
    public void setUp() {
        movie = new Movie("Pestis", "asd", 120,null);
    }

    /**
     *     @Test
     *     public void testEqualsShouldTrueWhenTheTwoObjectsFieldsAreEqual() {
     *         List<Screening> screeningList1 = new ArrayList<>();
     *         screeningList1.add(new Screening());
     *         List<Screening> screeningList2 = new ArrayList<>();
     *         screeningList2.add(new Screening(1, movie, null, null, null));
     *
     *
     *         EqualsVerifier.forClass( Movie.class )
     *                 .withIgnoredAnnotations(Entity.class, Id.class, Embeddable.class, MappedSuperclass.class, Transient.class)
     *                 .withPrefabValues(Screening.class, new Screening(), new Screening(1, movie, null, null, null))
     *                 .suppress( Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
     *                 .verify();
     *     }
     */

}
