package reader;

import jssc.SerialPort;

import java.io.IOException;
import java.util.Map;

public class PortReaderByLen extends PortReader {
    private final int maxLen;

    public PortReaderByLen(final int maxLen, final SerialPort serialPort, final Map<String, String> startMarks, final String stopMark) {
        super(serialPort, startMarks, stopMark);
        this.maxLen = maxLen;
    }

    void collectAndProcess(final String data) throws IOException {
        final String tmp = getCollectedData() + data;
        if (tmp.length() >= maxLen) {
            final String[] lineAndRemain = splitByMaxLen(tmp);
            setCollectedData(lineAndRemain[0]);
            try {
                processCollectedData();
            } finally {
                setCollectedData(lineAndRemain[1]);
            }
        } else {
            setCollectedData(tmp);
        }
    }

    String[] splitByMaxLen(final String data) {
        if (data.length() <= maxLen) {
            return new String[]{data, ""};
        } else {
            return new String[]{data.substring(0, maxLen), data.substring(maxLen)};
        }
    }
}
