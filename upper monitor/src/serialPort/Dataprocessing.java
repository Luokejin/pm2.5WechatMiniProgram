package serialPort;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import com.mongodb.MongoClient;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

import javax.swing.*;



public class Dataprocessing extends JFrame{
	
	/** 程序界面宽度*/
    public static final int WIDTH = 800;
    /** 程序界面高度*/
    public static final int HEIGHT = 620;
    /** 程序界面出现位置（横坐标） */
    public static final int LOC_X = 200;
    /** 程序界面出现位置（纵坐标）*/
    public static final int LOC_Y = 70;

    static JTextArea jta;
    private JLabel jl=null;
    private JPanel jp=null;
    private JScrollPane jsp=null;
	
	public static void main(String[] args )
	{
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");
		
		//显示界面
		new Dataprocessing();
		Dataprocessing.jta.append("--程序已启动--\n\r");
		Dataprocessing.jta.setLineWrap(true);
		
		
		
		//链接数据库
		MongoClient mongoClient=MongoDBTool.getMongoClient();
		
		
		
//		Dataprocessing a=new Dataprocessing();
		
		for (int i = 0; i==0 ; ) {
			
			//发送查询
			String pm25=SerialView.pm25data();
			String time = df.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()),
					ZoneId.of("Asia/Shanghai")));
			int temp=(pm25.length());
			
			//筛选不合格的数据
			if (temp==28) {
//				int pm25number=a.tonumber(pm25);
				int pm25number=tonumber(pm25);
//				System.out.println(pm25+"\r");
				System.out.println(time + "  pm2.5="+pm25number +"ug/立方米"+ "\r\n");
				jta.append(time+"  pm2.5="+pm25number +"ug/立方米"+ "\r\n");
				jta.setLineWrap(true);
				//创建表并生成初始数据
//				MongoDBTool.choosetable(MongoDBTool.getMongoDataBase(mongoClient), time, pm25number);
				//更新数据
				MongoDBTool.updata(MongoDBTool.getMongoDataBase(mongoClient), pm25number);
			}
			
			
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} 
		}
		
		
	}
	
	//提取数据,转换成十进制
	public static int tonumber(String string){
		int temp;
		String pm25=new String();

		//提取有效数据位,并除去空格
		pm25=string.substring(14, 20).replace(" ", "");
		System.out.println(string+"\r");

		temp=Integer.parseInt(pm25,16);
		System.out.println(Integer.parseInt(pm25,16));
		
		return temp;
	}

	public Dataprocessing(){
		init ();
	}
	
	public void init(){
		
		jta=new JTextArea();
		jta.setFont(new Font("微软雅黑", Font.BOLD, 20));
		jsp=new JScrollPane(jta);
		jl=new JLabel("GreenHat PM2.5环境监测");
		jl.setBounds(170, 80,600,50);
		jl.setFont(new Font("微软雅黑", Font.BOLD, 40));
		jp=new JPanel();
		
		//加入组件
		jp.add(jl);
		this.add(jp,BorderLayout.NORTH);
		this.add(jsp);
		
		
		//窗体属性
		this.setSize(800, 500);
		this.setTitle("PM25Tool");
		this.setIconImage((new ImageIcon("image\\icon_pm25.png")).getImage());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
        
	}
	
}
