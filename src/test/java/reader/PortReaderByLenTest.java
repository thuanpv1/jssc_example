package reader;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class PortReaderByLenTest {
    @Test
    void testSplitByMaxLen() {
        final Map<String, String> startMarks = new HashMap<>();
        startMarks.put("Em-Marine[2C00] 082,12941", "calc.exe");
        final String stopMark = "No card";
        final PortReaderByLen reader = new PortReaderByLen(7, null, startMarks, stopMark);
        final String[] tmp1 = reader.splitByMaxLen("Em-isRXis true");
        assert (tmp1[0].equals("Em-isRX"));
        assert (tmp1[1].equals("is true"));

        final String[] tmp2 = reader.splitByMaxLen("No card");
        assert (tmp2[0].equals("No card"));
        assert (tmp2[1].equals(""));
    }
}
