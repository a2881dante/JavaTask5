package org.example.repository;

import org.example.configs.AppConfig;
import org.example.models.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseRepository {
    
    abstract String getDataFile();

    abstract Stream<Model> convertLines(Stream<String> lines);
    
    abstract void copyData(Model oldModel, Model newModel);

    abstract void cascadeDelete(int id);

    public List<Model> all() {
        try (Stream<String> lines = Files.lines(Path.of(AppConfig.DATA_STORAGE_DIR + "/" + this.getDataFile()))) {
            return convertLines(lines).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Model get(int id) {
        try (Stream<String> lines = Files.lines(Path.of(AppConfig.DATA_STORAGE_DIR + "/" + this.getDataFile())).parallel()) {
            List<Model> models = convertLines(lines).collect(Collectors.toList());
            return models.stream()
                    .filter(m -> m.getId() == id)
                    .findFirst().orElse(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void store(Model model) {
        try {
            Files.write( Paths.get(AppConfig.DATA_STORAGE_DIR + "/" + this.getDataFile()),
                    (model.toCSVString() +  System.lineSeparator()).getBytes(),
                    StandardOpenOption.APPEND
            );
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(int id, Model newModel) {
        Model oldModel;

        try (Stream<String> lines = Files.lines(Path.of("data_store/manufacturers.csv")).parallel()) {
            List<Model> models = convertLines(lines).collect(Collectors.toList());
            oldModel = models.stream()
                    .filter(m -> m.getId() == id)
                    .findFirst().orElse(null);
            copyData(oldModel, newModel);
            rewriteDataFile(models);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        try (Stream<String> lines = Files.lines(Path.of(AppConfig.DATA_STORAGE_DIR + "/" + this.getDataFile())).parallel()) {
            List<Model> models = convertLines(lines).collect(Collectors.toList());
            models.removeIf(m -> m.getId() == id);
            rewriteDataFile(models);
            cascadeDelete(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void rewriteDataFile(List<? extends Model> models) throws IOException {
        Files.newBufferedWriter(Paths.get(AppConfig.DATA_STORAGE_DIR + "/" + this.getDataFile())).write("");
        models.stream().parallel().forEach(model -> {
            try {
                Files.write( Paths.get(AppConfig.DATA_STORAGE_DIR + "/" + this.getDataFile()),
                        (model.toCSVString() +  System.lineSeparator()).getBytes(),
                        StandardOpenOption.APPEND
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public int getMaxId() {
        int lastId = 0;
        try (Stream<String> lines = Files.lines(Path.of(AppConfig.DATA_STORAGE_DIR + "/" + this.getDataFile())).parallel()) {
            List<Model> models = convertLines(lines).collect(Collectors.toList());
            if(!models.isEmpty()) {
                lastId = models.stream().parallel()
                        .max(Comparator.comparing(Model::getId))
                        .get()
                        .getId();
            }
        } catch (IOException ignored) {}
        return lastId;
    }
}
