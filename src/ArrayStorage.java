/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    void clear() {
    }

    void save(Resume r) {
        storage[size()] = r;
    }

    Resume get(String uuid) {
        int arrSize = size();
        for (int i = 0; i < arrSize; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        int arrSize = size();
        for (int i = 0; i < arrSize; i++) {
            if (storage[i].uuid.equals(uuid)) {
                for (int j = i; j < arrSize - 1; j++) {
                    storage[j] = storage[j + 1];
                }
                storage[arrSize - 1] = null;
                return;
            }
        }

    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return new Resume[0];
    }

    int size() {
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] == null) {
                return i;
            }
        }
        return storage.length;
    }
}
