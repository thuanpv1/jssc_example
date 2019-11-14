package reader;

import jssc.SerialPort;

import java.io.IOException;
import java.util.Map;

public class PortReaderByDelimiter extends PortReader {
    private final String delimiter;

    public PortReaderByDelimiter(final String delimiter, final SerialPort serialPort, final Map<String, String> startMarks, final String stopMark) {
        super(serialPort, startMarks, stopMark);
        this.delimiter = delimiter;
    }


    void collectAndProcess(final String data) throws IOException {
        final String tmp = getCollectedData() + data;
        if (tmp.contains(delimiter)) {
            final String[] lineAndRemain = splitByDelimiter(tmp);
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

    String[] splitByDelimiter(final String data) {
        final String[] tmp = data.split(delimiter);
        if (tmp.length == 1) {
            return new String[]{tmp[0], ""};
        } else {
            return tmp;
        }
    }
}
