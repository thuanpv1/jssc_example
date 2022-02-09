import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import reader.PortReaderByDelimiter;

import java.util.HashMap;
import java.util.Map;

public class Main {
	static SerialPort serialPort;
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static void main(String[] args) throws SerialPortException {
//        final Map<String, String> startMarks = new HashMap<>();
//        startMarks.put("Em-Marine[2C00] 082,12941", "calc.exe");
//        final String stopMark = "No card";
//        final SerialPort serialPort = new SerialPort("COM6");
//        serialPort.openPort();
//        serialPort.setParams(SerialPort.BAUDRATE_9600,
//                SerialPort.DATABITS_8,
//                SerialPort.STOPBITS_1,
//                SerialPort.PARITY_NONE);
//        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
//        serialPort.addEventListener(new PortReaderByDelimiter("\r\n", serialPort, startMarks, stopMark), SerialPort.MASK_RXCHAR);
//        byte[] test = new byte[] {(byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x05, (byte) 0xCB};
////        serialPort.writeString("Get data");
//        serialPort.writeBytes(test);
//        byte[] test2 = serialPort.readBytes(3);
//        System.out.println("test2====" + String.valueOf(test2[0]));
    	
    	serialPort = new SerialPort("COM6");
        try {
            serialPort.openPort();//Open port
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                            SerialPort.DATABITS_8,
                             SerialPort.STOPBITS_1,
                             SerialPort.PARITY_NONE);//Set params
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            serialPort.setEventsMask(mask);//Set mask
//            serialPort.addEventListener(new SerialPortReader());//Add SerialPortEventListener
            byte[] buffer = new byte[] {(byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x05, (byte) 0xCB};
            serialPort.writeBytes(buffer);
            byte[] test2 = serialPort.readBytes(12);
            System.out.println("test2 test2: " + bytesToHex(test2));
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    	
    	
    	
    	
    }
    
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    public static class SerialPortReader implements SerialPortEventListener {

        String buffer = "";
        private void onMessage() {
                // constructing message  
                System.out.println("RECEIVED MESSAGE: " + buffer);
                buffer = "";
        }

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String b = serialPort.readString(event.getEventValue());
                    System.out.println("event:"+b);
                    if (b.equals("\n") ) {
                        onMessage();
                    } else {
                        buffer += b;
                    }
                } catch (SerialPortException ex) {
                    System.out.println("Error in receiving string from COM-port: " + ex);
                }
            }
        }
    }

}