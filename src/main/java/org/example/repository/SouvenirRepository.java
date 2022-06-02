package org.example.repository;

import org.example.configs.AppConfig;
import org.example.models.Model;
import org.example.models.Souvenir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SouvenirRepository extends BaseRepository{
    @Override
    String getDataFile() {
        return "souvenirs.csv";
    }

    @Override
    Stream<Model> convertLines(Stream<String> lines) {
        return lines.map(Souvenir::new);
    }

    @Override
    void copyData(Model oldModel, Model newModel) {
        ((Souvenir)oldModel).setName(((Souvenir)newModel).getName());
        ((Souvenir)oldModel).setManufacturerId(((Souvenir)newModel).getManufacturerId());
        ((Souvenir)oldModel).setCreatedAt(((Souvenir)newModel).getCreatedAt());
        ((Souvenir)oldModel).setPrice(((Souvenir)newModel).getPrice());
    }

    @Override
    void cascadeDelete(int id) {}

    public void deleteByManufacturerId(int id) {
        try (Stream<String> lines = Files.lines(Path.of(AppConfig.DATA_STORAGE_DIR + "/" + this.getDataFile())).parallel()) {
            List<Model> models = convertLines(lines).collect(Collectors.toList());
            models.removeIf(m -> ((Souvenir) m).getManufacturerId() == id);
            rewriteDataFile(models);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
