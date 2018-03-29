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
	
	/** ���������*/
    public static final int WIDTH = 800;
    /** �������߶�*/
    public static final int HEIGHT = 620;
    /** ����������λ�ã������꣩ */
    public static final int LOC_X = 200;
    /** ����������λ�ã������꣩*/
    public static final int LOC_Y = 70;

    static JTextArea jta;
    private JLabel jl=null;
    private JPanel jp=null;
    private JScrollPane jsp=null;
	
	public static void main(String[] args )
	{
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");
		
		//��ʾ����
		new Dataprocessing();
		Dataprocessing.jta.append("--����������--\n\r");
		Dataprocessing.jta.setLineWrap(true);
		
		
		
		//�������ݿ�
		MongoClient mongoClient=MongoDBTool.getMongoClient();
		
		
		
//		Dataprocessing a=new Dataprocessing();
		
		for (int i = 0; i==0 ; ) {
			
			//���Ͳ�ѯ
			String pm25=SerialView.pm25data();
			String time = df.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()),
					ZoneId.of("Asia/Shanghai")));
			int temp=(pm25.length());
			
			//ɸѡ���ϸ������
			if (temp==28) {
//				int pm25number=a.tonumber(pm25);
				int pm25number=tonumber(pm25);
//				System.out.println(pm25+"\r");
				System.out.println(time + "  pm2.5="+pm25number +"ug/������"+ "\r\n");
				jta.append(time+"  pm2.5="+pm25number +"ug/������"+ "\r\n");
				jta.setLineWrap(true);
				//���������ɳ�ʼ����
//				MongoDBTool.choosetable(MongoDBTool.getMongoDataBase(mongoClient), time, pm25number);
				//��������
				MongoDBTool.updata(MongoDBTool.getMongoDataBase(mongoClient), pm25number);
			}
			
			
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} 
		}
		
		
	}
	
	//��ȡ����,ת����ʮ����
	public static int tonumber(String string){
		int temp;
		String pm25=new String();

		//��ȡ��Ч����λ,����ȥ�ո�
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
		jta.setFont(new Font("΢���ź�", Font.BOLD, 20));
		jsp=new JScrollPane(jta);
		jl=new JLabel("GreenHat PM2.5�������");
		jl.setBounds(170, 80,600,50);
		jl.setFont(new Font("΢���ź�", Font.BOLD, 40));
		jp=new JPanel();
		
		//�������
		jp.add(jl);
		this.add(jp,BorderLayout.NORTH);
		this.add(jsp);
		
		
		//��������
		this.setSize(800, 500);
		this.setTitle("PM25Tool");
		this.setIconImage((new ImageIcon("image\\icon_pm25.png")).getImage());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
        
	}
	
}
