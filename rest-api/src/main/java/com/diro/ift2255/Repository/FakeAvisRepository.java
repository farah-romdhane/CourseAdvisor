package com.diro.ift2255.Repository;

import com.diro.ift2255.model.Avis;

import java.util.ArrayList;
import java.util.List;

public class FakeAvisRepository extends AvisRepository {

    private final List<Avis> data = new ArrayList<>();

    @Override
    public void save(Avis avis) {
        data.add(avis);
    }

    @Override
    public List<Avis> findByCours(String coursCode) {
        return data;
    }

    @Override
    public int countAvisForCours(String coursCode) {
        return data.size();
    }
}
