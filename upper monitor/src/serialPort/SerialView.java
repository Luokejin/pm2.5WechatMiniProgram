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


	public SerialPort serialPort = null; // 保存串口对象
	boolean life = true;
	static String pm25 ;

	public static String pm25data() {
		
		pm25="";
		List<String> commList = SerialTool.findPort(); // 程序初始化时就扫描一次有效串口
		// 检查是否有可用串口，有则加入选项中
		if (commList == null || commList.size() < 1) {
			System.out.print("没有搜索到有效串口！" + "错误" + JOptionPane.INFORMATION_MESSAGE);
			Dataprocessing.jta.append("没有搜索到有效串口！" + "错误" + JOptionPane.INFORMATION_MESSAGE);
            Dataprocessing.jta.setLineWrap(true);
			// JOptionPane.showMessageDialog(null, "没有搜索到有效串口！", "错误",
			// JOptionPane.INFORMATION_MESSAGE);
		}

		SerialView a = new SerialView();
		a.listen();


		return pm25;
	}

	private void listen() {
		// (1)打开串口连接
		String commName = "COM6";// 获取串口名称
		String bpsStr = "9600";// 获取波特率

		// 检查串口名称是否获取正确
		if (commName == null || commName.equals("")) {
			System.out.print("没有搜索到有效串口！" + "错误:" + JOptionPane.INFORMATION_MESSAGE);
		} else {
			// 检查波特率是否获取正确
			if (bpsStr == null || bpsStr.equals("")) {
				System.out.println("波特率获取错误！" + "错误:" + JOptionPane.INFORMATION_MESSAGE);
			} else {
				// 串口名、波特率均获取正确时
				int bps = Integer.parseInt(bpsStr);
				try {
					// 获取指定端口名及波特率的串口对象
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

		// 发送数据

		try {
			SerialTool.sendToPort(serialPort, hex2byte(message));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// 关闭串口连接
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} 

		SerialTool.closePort(serialPort);

	}

	/**
	 * 字符串转16进制
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
	 * 字节数组转16进制
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
	 * 以内部类形式创建一个串口监听类
	 * 
	 * @author zhong
	 */
	class SerialListener implements SerialPortEventListener {

		/**
		 * 处理监控到的串口事件
		 */
		public void serialEvent(SerialPortEvent serialPortEvent) {

			switch (serialPortEvent.getEventType()) {
			case SerialPortEvent.BI: // 10 通讯中断
				System.out.println("与串口设备通讯中断" + "错误" + JOptionPane.INFORMATION_MESSAGE);
				break;
			case SerialPortEvent.OE: // 7 溢位（溢出）错误
				break;
			case SerialPortEvent.FE: // 9 帧错误
				break;
			case SerialPortEvent.PE: // 8 奇偶校验错误
				break;
			case SerialPortEvent.CD: // 6 载波检测
				break;
			case SerialPortEvent.CTS: // 3 清除待发送数据
				break;
			case SerialPortEvent.DSR: // 4 待发送数据准备好了
				break;
			case SerialPortEvent.RI: // 5 振铃指示
				break;
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
				break;
			case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
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
