package infrastructure.importer;

import infrastructure.importer.reader.*;

public record DataSizer(
    Reader reader
) {

    public int size(String path) {
        return reader.read(path).size();
    }
}
