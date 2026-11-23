package member4;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ActionLogger {
    private final String logFilePath;

    public ActionLogger(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public void logAction(String actionName, String details) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("[ACTION] ")
          .append(actionName)
          .append(" - ")
          .append(details)
          .append(System.lineSeparator());

        try (FileOutputStream fos = new FileOutputStream(logFilePath, true)) {
            fos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}