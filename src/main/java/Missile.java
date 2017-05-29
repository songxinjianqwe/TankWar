import java.awt.*;
import java.util.*;


public class Missile {

	private int x;
	private int y;
	private Dir dir = Dir.STOP;
	private boolean live = true;
	private boolean good = true;
	private Color cMissile = null;
	public static final int w = 12;
	public static final int h = 12;
	public  static final int XSPEED = 10;
	public  static final int YSPEED = 10;
	TankClient tc = null;
	private static Map<String ,Image> imgs = new HashMap<String ,Image>();
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image [] tankImgs = null;
	//static �Ǹ�Class ���ص��ڴ������ִ�е�һ�δ���
	//��ֱ�ӳ�ʼ���ĺô��Ǳ���������ִ�д���ĵط��������Ա���������򣩣�ͨ�����ַ�ʽ�Ϳ���ִ�д����ˡ�
	static {//��̬���������ʺϸ���������ʼ��
		tankImgs = new Image[] { //����Ϊ��̬�����еı�ը��ʹ����ЩͼƬ�������̬��ôÿ��Explode����Ҫ����һ����ЩͼƬ
				tk.getImage(Explode.class.getClassLoader().getResource("images/MissileU.gif")),	
				tk.getImage(Explode.class.getClassLoader().getResource("images/MissileD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/MissileL.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/MissileR.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/MissileLU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/MissileRU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/MissileLD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/MissileRD.gif")),

			};
		imgs.put("U", tankImgs[0]);
		imgs.put("D", tankImgs[1]);
		imgs.put("L", tankImgs[2]);
		imgs.put("R", tankImgs[3]);
		imgs.put("LU", tankImgs[4]);
		imgs.put("RU", tankImgs[5]);
		imgs.put("LD", tankImgs[6]);
		imgs.put("RD", tankImgs[7]);
	}
	
	public Missile(int x, int y, Dir dir,boolean good) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.good = good;
		if(good)
			cMissile = Color.pink;
		else
			cMissile = Color.gray;
	}
	
	public Missile(int x,int y,Dir dir ,boolean good,TankClient tc){
		this(x,y,dir,good);
		this.tc =tc;
	}
	
	public void draw(Graphics g){
		if(!live){  //ÿ���ػ�֮ǰ���ж��ӵ��Ƿ��Ѿ���������������������ǻ���̹�ˣ�Ҳ�����Ƿɳ����ڣ���
					//���Ƚ�����������Ƴ���Ȼ���������ػ�Ĺ���
			tc.missiles.remove(this);
			return ;
		}	
		switch(dir){
			case U: 	
				g.drawImage(imgs.get("U"),x,y,null);
				break;
			case D: 	
				g.drawImage(imgs.get("D"),x,y,null);
				break;
			case L: 	
				g.drawImage(imgs.get("L"),x,y,null);
				break;
			case R: 	
				g.drawImage(imgs.get("R"),x,y,null);
				break;
			case LU: 	
				g.drawImage(imgs.get("LU"),x,y,null);
				break;
			case RU: 	
				g.drawImage(imgs.get("RU"),x,y,null);
				break;
			case LD: 	
				g.drawImage(imgs.get("LD"),x,y,null);
				break;
			case RD: 	
				g.drawImage(imgs.get("RD"),x,y,null);
				break;
		}	
		move();
	}
	
	public void move(){
			switch(dir){
				case U: 	
					y-=YSPEED;
					break;
				case D: 	
					y+=YSPEED;
					break;
				case L: 	
					x-=XSPEED;
					break;
				case R: 	
					x+=XSPEED;
					break;
				case LU: 	
					y-=YSPEED;
					x-=XSPEED;
					break;
				case RU: 	
					y-=YSPEED;
					x+=XSPEED;
					break;
				case LD: 	
					x-=XSPEED;
					y+=YSPEED;
					break;
				case RD: 	
					x+=XSPEED;
					y+=YSPEED;
					break;
				case STOP :break;
			}
			
			if(x < 0 ||y < 0 || x > TankClient.w || y > TankClient.h ){
				live = false;
			}
				
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,w,h);
	}
				
	public void hitTank(Tank t){ //�ж�һ���ض����ӵ��Ƿ����̹�ˣ�������Tank�����÷�������ΪҪ�ж�ÿһ���ӵ���̹�˵�λ�ù�ϵ������ȡ���ӵ���λ�û���鷳��
		
		if( t.isLive() &&this.live && this.getRect().intersects(t.getRect())  && this.good != t.isGood() ){ //���������˼ά���ԣ����ӵ�����̹�ˣ����е��ж��Ƿ��غϵķ�������Ҫ���������õ����ƶ��Ķ��󣬱����õĲ����ȴ�����ײ�ġ�
			if(t.isGood()){																				  //Ҫ��̹�˺��ӵ����ǻ��ŵģ�����Ͳ���ȥ�ж��Ƿ���ײ
				t.setHp(t.getHp() -20);
				if(t.getHp() <= 0 )
					t.setLive(false);
			}
			else
				t.setLive(false);//������ж�̹���Ƿ���������ô�����ӵ��ɵ��Ѿ�������̹�˵�λ������Ȼ����ʧ					
			this.live = false;										//����ӵ���̹�˲���ͬһ���Ĳſ��Դ���
			tc.explodes.add(new Explode(x,y, tc));			
		}	 		
	}
	
	public void hitTanks(ArrayList<Tank> enemyTanks){ //�����Ļ��Ͳ�����TankClient��ʹ��Ƕ��ѭ���ˣ�ֱ�ӵ��ø÷�������
		for(int i = 0; i < enemyTanks.size() ;i++){
			hitTank(enemyTanks.get(i));
		}
	}
	
	public void hitWall(Wall wall){
		if(this.live && this.getRect().intersects(wall.getRect())){
			this.live  = false;			
		}
	}
}

