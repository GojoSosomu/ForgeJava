package infrastructure.importer.translation.maker;

public interface Maker<DATA, RESULT> {
    default RESULT make(DATA raw, String id) {
        return null;
    }
    default RESULT make(DATA raw) {
        return null;
    }
}
