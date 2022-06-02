package org.example.repository;

import org.example.models.Manufacturer;
import org.example.models.Model;

import java.util.stream.Stream;

public class ManufacturerRepository extends BaseRepository{
    @Override
    String getDataFile() {
        return "manufacturers.csv";
    }

    @Override
    Stream<Model> convertLines(Stream<String> lines) {
        return lines.map(Manufacturer::new);
    }

    @Override
    void copyData(Model oldModel, Model newModel) {
        ((Manufacturer) oldModel).setName(((Manufacturer)newModel).getName());
        ((Manufacturer) oldModel).setCountry(((Manufacturer)newModel).getCountry());
    }

    @Override
    void cascadeDelete(int id) {
        SouvenirRepository souvenirRepository = new SouvenirRepository();
        souvenirRepository.deleteByManufacturerId(id);
    }
}
