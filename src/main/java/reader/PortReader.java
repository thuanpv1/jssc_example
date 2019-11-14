package reader;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

abstract class PortReader implements SerialPortEventListener {
    private static final Logger logger = LoggerFactory.getLogger(PortReader.class);

    private final SerialPort serialPort;
    private final Map<String, String> startMarks;
    private final String stopMark;

    PortReader(final SerialPort serialPort, final Map<String, String> startMarks, final String stopMark) {
        this.serialPort = serialPort;
        this.startMarks = startMarks;
        this.stopMark = stopMark;
    }

    public void serialEvent(final SerialPortEvent event) {
        //Объект типа SerialPortEvent несёт в себе информацию о том какое событие произошло и значение.
        //Так например если пришли данные то метод event.getEventValue() вернёт нам количество байт во входном буфере.
        if (event.isRXCHAR()) {
            logger.trace("isRXCHAR - true");
            if (event.getEventValue() > 0) {
                try {
                    final String data = serialPort.readString();
                    logger.trace(data);
                    if (data != null) {
                        collectAndProcess(data);
                    }
                } catch (final Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        }
        //Если изменилось состояние линии CTS, то метод event.getEventValue() вернёт 1 если линия включения и 0 если выключена.
        else if (event.isCTS()) {
            if (event.getEventValue() == 1) {
                logger.trace("CTS - ON");
            } else {
                logger.trace("CTS - OFF");
            }
        } else if (event.isDSR()) {
            if (event.getEventValue() == 1) {
                logger.trace("DSR - ON");
            } else {
                logger.trace("DSR - OFF");
            }
        }
    }

    abstract void collectAndProcess(final String data) throws IOException;

    private String collectedData = "";

    String getCollectedData() {
        return collectedData;
    }

    void setCollectedData(final String data) {
        collectedData = data;
    }

    void processCollectedData() throws IOException {
        if (startMarks.containsKey(getCollectedData())) {
            stopProcess();
            startProcess(startMarks.get(getCollectedData()));
        } else if (getCollectedData().equals(stopMark)) {
            stopProcess();
        }
    }

    private Process process;

    private void startProcess(final String cmd) throws IOException {
        if (!cmd.isEmpty()) {
            logger.info("startProcess {}", cmd);
            process = Runtime.getRuntime().exec(cmd);
        }
    }

    private void stopProcess() {
        if (process != null) {
            logger.info("stopProcess");
            process.destroy();
            process = null;
        }
    }
}
