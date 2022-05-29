package manager;

import java.io.IOException;
import java.nio.file.Path;

public class Managers {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault(String uri) throws IOException, InterruptedException {
        return new HTTPTaskManager(uri);
    }

    public FileBackedTasksManager getDefaultFileBacked() {
        return new FileBackedTasksManager();
    }
}
