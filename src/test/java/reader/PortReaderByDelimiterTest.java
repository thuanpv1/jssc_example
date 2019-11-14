package reader;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class PortReaderByDelimiterTest {
    @Test
    void testSplitByDelimiter() {
        final Map<String, String> startMarks = new HashMap<>();
        startMarks.put("Em-Marine[2C00] 082,12941", "calc.exe");
        final String stopMark = "No card";
        final PortReaderByDelimiter reader = new PortReaderByDelimiter("\r\n", null, startMarks, stopMark);
        final String[] tmp1 = reader.splitByDelimiter("Em-\r\nisRXCHAR - true");
        assert (tmp1[0].equals("Em-"));
        assert (tmp1[1].equals("isRXCHAR - true"));

        final String[] tmp2 = reader.splitByDelimiter("No card\r\n");
        assert (tmp2[0].equals("No card"));
        assert (tmp2[1].equals(""));
    }
}
