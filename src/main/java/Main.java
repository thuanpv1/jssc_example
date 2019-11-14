import jssc.SerialPort;
import jssc.SerialPortException;
import reader.PortReaderByDelimiter;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws SerialPortException {
        final Map<String, String> startMarks = new HashMap<>();
        startMarks.put("Em-Marine[2C00] 082,12941", "calc.exe");
        final String stopMark = "No card";
        //Передаём в конструктор имя порта
        final SerialPort serialPort = new SerialPort("COM3");
        //Открываем порт
        serialPort.openPort();
        //Выставляем параметры
        serialPort.setParams(SerialPort.BAUDRATE_9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        //Включаем аппаратное управление потоком
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
        //Устанавливаем ивент лисенер и маску
        serialPort.addEventListener(new PortReaderByDelimiter("\r\n", serialPort, startMarks, stopMark), SerialPort.MASK_RXCHAR);
        //Отправляем запрос устройству
        serialPort.writeString("Get data");
    }

}