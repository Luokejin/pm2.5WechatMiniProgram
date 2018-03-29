package serialPort;

import java.io.IOException;
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TooManyListenersException;
//import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;



public class SerialView {


	public SerialPort serialPort = null; // ���洮�ڶ���
	boolean life = true;
	static String pm25 ;

	public static String pm25data() {
		
		pm25="";
		List<String> commList = SerialTool.findPort(); // �����ʼ��ʱ��ɨ��һ����Ч����
		// ����Ƿ��п��ô��ڣ��������ѡ����
		if (commList == null || commList.size() < 1) {
			System.out.print("û����������Ч���ڣ�" + "����" + JOptionPane.INFORMATION_MESSAGE);
			Dataprocessing.jta.append("û����������Ч���ڣ�" + "����" + JOptionPane.INFORMATION_MESSAGE);
            Dataprocessing.jta.setLineWrap(true);
			// JOptionPane.showMessageDialog(null, "û����������Ч���ڣ�", "����",
			// JOptionPane.INFORMATION_MESSAGE);
		}

		SerialView a = new SerialView();
		a.listen();


		return pm25;
	}

	private void listen() {
		// (1)�򿪴�������
		String commName = "COM6";// ��ȡ��������
		String bpsStr = "9600";// ��ȡ������

		// ��鴮�������Ƿ��ȡ��ȷ
		if (commName == null || commName.equals("")) {
			System.out.print("û����������Ч���ڣ�" + "����:" + JOptionPane.INFORMATION_MESSAGE);
		} else {
			// ��鲨�����Ƿ��ȡ��ȷ
			if (bpsStr == null || bpsStr.equals("")) {
				System.out.println("�����ʻ�ȡ����" + "����:" + JOptionPane.INFORMATION_MESSAGE);
			} else {
				// �������������ʾ���ȡ��ȷʱ
				int bps = Integer.parseInt(bpsStr);
				try {
					// ��ȡָ���˿����������ʵĴ��ڶ���
					serialPort = SerialTool.openPort(commName, bps);
					SerialTool.addListener(serialPort, new SerialListener());
					if (serialPort == null)
						return;

				} catch (UnsupportedCommOperationException | PortInUseException | NoSuchPortException
						| TooManyListenersException e1) {
					e1.printStackTrace();
				}
			}

		}

		String message = "01 03 00 58 00 01 05 d9";

		// ��������

		try {
			SerialTool.sendToPort(serialPort, hex2byte(message));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// �رմ�������
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} 

		SerialTool.closePort(serialPort);

	}

	/**
	 * �ַ���ת16����
	 * 
	 * @param hex
	 * @return
	 */
	private byte[] hex2byte(String hex) {

		String digital = "0123456789abcdef";
		String hex1 = hex.replace(" ", "");
		char[] hex2char = hex1.toCharArray();
		byte[] bytes = new byte[hex1.length() / 2];
		byte temp;
		for (int p = 0; p < bytes.length; p++) {
			temp = (byte) (digital.indexOf(hex2char[2 * p]) * 16);
			temp += digital.indexOf(hex2char[2 * p + 1]);
			bytes[p] = (byte) (temp & 0xff);
		}
		return bytes;
	}

	/**
	 * �ֽ�����ת16����
	 * 
	 * @param b
	 * @return
	 */
	private String printHexString(byte[] b) {

		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sbf.append(hex.toUpperCase() + "  ");
		}
		return sbf.toString().trim();
	}

	/**
	 * ���ڲ�����ʽ����һ�����ڼ�����
	 * 
	 * @author zhong
	 */
	class SerialListener implements SerialPortEventListener {

		/**
		 * �����ص��Ĵ����¼�
		 */
		public void serialEvent(SerialPortEvent serialPortEvent) {

			switch (serialPortEvent.getEventType()) {
			case SerialPortEvent.BI: // 10 ͨѶ�ж�
				System.out.println("�봮���豸ͨѶ�ж�" + "����" + JOptionPane.INFORMATION_MESSAGE);
				break;
			case SerialPortEvent.OE: // 7 ��λ�����������
				break;
			case SerialPortEvent.FE: // 9 ֡����
				break;
			case SerialPortEvent.PE: // 8 ��żУ�����
				break;
			case SerialPortEvent.CD: // 6 �ز����
				break;
			case SerialPortEvent.CTS: // 3 �������������
				break;
			case SerialPortEvent.DSR: // 4 ����������׼������
				break;
			case SerialPortEvent.RI: // 5 ����ָʾ
				break;
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 ��������������
				break;
			case SerialPortEvent.DATA_AVAILABLE: // 1 ���ڴ��ڿ�������
//				String time = df.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()),
//						ZoneId.of("Asia/Shanghai")));
				byte[] data;// FE0400030001D5C5
				try {
					data = SerialTool.readFromPort(serialPort);
					String stringdata = printHexString(data);

					pm25 = pm25 +"  "+ stringdata;

//					System.out.println(pm25);
//					System.out.println(time + " [" + serialPort.getName().split("/")[3] + "] : " + printHexString(data) + "\r\n");

				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}

		}

	}
}
