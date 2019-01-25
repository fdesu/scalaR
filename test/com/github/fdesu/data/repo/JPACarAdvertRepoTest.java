package com.github.fdesu.data.repo;

import com.github.fdesu.data.model.CarAdvert;
import org.junit.Before;
import org.junit.Test;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class JPACarAdvertRepoTest {
    private static final CarAdvert EXAMPLE = new CarAdvert();
    private static final long ID = 123L;

    private JPAApi jpaApi = mock(JPAApi.class);
    private EntityManager em = mock(EntityManager.class);
    private JPACarAdvertRepo repo = new JPACarAdvertRepo(jpaApi);

    @Before
    public void setUpMocks() {
        when(jpaApi.em()).thenReturn(em);

        // execute JPAApi#withTransaction's Supplier
        doAnswer(args -> ((Supplier<CarAdvert>)args.getArgument(0)).get())
            .when(jpaApi).withTransaction(any(Supplier.class));
    }

    @Test
    public void shouldDelegateFindCallToEntityManager() {
        when(em.find(eq(CarAdvert.class), eq(ID))).thenReturn(EXAMPLE);

        assertThat(repo.findById(ID)).isEqualTo(EXAMPLE);
    }

    @Test
    public void shouldDelegatePersistToEM() {
        repo.persist(EXAMPLE);

        verify(em).persist(EXAMPLE);
    }

    @Test
    public void shouldDelegateUpdateCallToEM() {
        repo.persist(EXAMPLE);

        verify(em).persist(EXAMPLE);
    }

    @Test
    public void shouldDelegateDeleteCallToEM() {
        when(em.getReference(CarAdvert.class, 321L)).thenReturn(EXAMPLE);

        repo.delete(321L);

        verify(em).getReference(CarAdvert.class, 321L);
        verify(em).remove(EXAMPLE);
    }
}
