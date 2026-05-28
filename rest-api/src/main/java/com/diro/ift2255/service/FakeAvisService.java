package com.diro.ift2255.service;

import com.diro.ift2255.Repository.FakeAvisRepository;
import com.diro.ift2255.model.Avis;

import java.util.Collections;
import java.util.List;

/**
 * Faux service d’avis utilisé pour les tests.
 */
public class FakeAvisService extends AvisService {

    public FakeAvisService(CourseService courseService) {
        super(new FakeAvisRepository(), courseService);
    }

    @Override
    public List<Avis> getAvisChargeExploitables(String coursCode) {
        return Collections.emptyList();
    }

    @Override
    public List<Avis> getAvisAffichables(String coursCode) {
        return Collections.emptyList();
    }
}
