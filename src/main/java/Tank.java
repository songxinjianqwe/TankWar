import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class Tank {
	
	private int x ; 
	private int y ;
	private int hp = 100;
	private boolean good = true;	
	private boolean live = true;
	private int oldX ;
	private int oldY ;
	public static final int w = 30;
	public static final int h = 30;
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	public static final Random r = new Random();//�����������
	int step = r.nextInt(12)+3;//����ƶ�3~14��
	Color cTank = null;//Color.cyan;
	private boolean bR = false;
	private boolean bL = false;
	private boolean bU = false;
	private boolean bD = false;
	private Dir dir = Dir.STOP;
	private Dir barrelDir = Dir.U;
	TankClient tc = null;
	BloodBar bb = new BloodBar();
	private static Map<String ,Image> imgs = new HashMap<String ,Image>();
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image [] tankImgs = null;
	//static �Ǹ�Class ���ص��ڴ������ִ�е�һ�δ���
	//��ֱ�ӳ�ʼ���ĺô��Ǳ���������ִ�д���ĵط��������Ա���������򣩣�ͨ�����ַ�ʽ�Ϳ���ִ�д����ˡ�
	static {//��̬���������ʺϸ���������ʼ��
		tankImgs = new Image[] { //����Ϊ��̬�����еı�ը��ʹ����ЩͼƬ�������̬��ôÿ��Explode����Ҫ����һ����ЩͼƬ
				tk.getImage(Explode.class.getClassLoader().getResource("images/tankU.gif")),	
				tk.getImage(Explode.class.getClassLoader().getResource("images/tankD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/tankL.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/tankR.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/tankLU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/tankRU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/tankLD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("images/tankRD.gif")),

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
	public Tank(int x,int y,boolean good){ //���صĹ��췽�������������ڲ�ͬ��������е�ʱ����Ҫ������һ���������е�ʱ������Ҫ�����ؿ������㲻ͬ��Ҫ��
		this.x = x;
		this.y = y;
		this.good = good;
		this.oldX = x;
		this.oldY = y;
		if(good)
			cTank = Color.cyan;
		else
			cTank = Color.green;
	}	
	
	public Tank(int x,int y,boolean good,TankClient tc){
		this(x,y,good);
		this.tc =tc ;
	}
	
	public Tank(int x,int y,boolean good,Dir dir,TankClient tc){
		this(x,y,good);
		this.dir = dir;
		this.tc =tc ;
	}
	
	public void draw(Graphics g){
		if(!live) {
			if(!good)
				tc.enemyTanks.remove(this);
			return ;
		}
		if(good)
			bb.draw(g);
		Color c = g.getColor();		
		switch(barrelDir){
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
		g.setColor(c);		
		move();
	}
		
	public void move(){
		if(dir != Dir.STOP)
			barrelDir = dir;
		oldX = x; //�ȼ�¼��һ��λ�ã����ƶ�
		oldY = y;
/*		if(good){
			switch(dir){
				case U: 	
					y-=2*YSPEED;
					break;
				case D: 	
					y+=2*YSPEED;
					break;
				case L: 	
					x-=2*XSPEED;
					break;
				case R: 	
					x+=2*XSPEED;
					break;
				case LU: 	
					y-=2*YSPEED;
					x-=2*XSPEED;
					break;
				case RU: 	
					y-=2*YSPEED;
					x+=2*XSPEED;
					break;
				case LD: 	
					x-=2*XSPEED;
					y+=2*YSPEED;
					break;
				case RD: 	
					x+=2*XSPEED;
					y+=2*YSPEED;
					break;
				case STOP :break;
			}
		}else{
*/
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
//		}
		
		if(x < 0) x = 0; //����λ��
		if(y < 30) y = 30;
		if(x + Tank.w > TankClient.w) x = TankClient.w - Tank.w;
		if(y + Tank.h > TankClient.h) y = TankClient.h - Tank.h;
		
		if(!good){ //ʵ�ַ�����ƶ����������
			if(step == 0){
				dir = randomDir();
				if(r.nextInt(40) > 20)
					fire();
				step = r.nextInt(12)+3;
				
			}
			step --;
			
		}
	}
	
	public static Dir  randomDir() {
		Dir []dirs = Dir.values();
		int randomIndex = r.nextInt(dirs.length);	
		Dir dir = dirs[randomIndex];
		return dir;
	}
	
	public void locateDirection(){
		

		if(bU && !bD && ! bL && !bR) dir = Dir.U;
		else if(!bU && bD && ! bL && !bR) dir = Dir.D;
		else if(!bU && !bD &&  bL && !bR) dir = Dir.L;
		else if(!bU && !bD && ! bL && bR) dir = Dir.R;
		else if(bU && !bD &&  bL && !bR) dir =  Dir.LU;
		else if(bU && !bD && ! bL && bR) dir =  Dir.RU;
		else if(!bU && bD &&  bL && !bR) dir = Dir.LD;
		else if(!bU && bD &&  ! bL && bR) dir = Dir.RD;
		else if(!bU && !bD && ! bL && !bR) dir = Dir.STOP;
	}



	
	public void keyPressed(KeyEvent e) { //���̵İ����ڰ��µ�˲�����ʱ��(�������й�ϵ),����������ͬʱ����ABCDʱ,
										 //���̴�����ֵ����ABCD �п�����ACBD ,ADBC�ȵ�����ֻҪ�ж���Ҫ�ļ������Ƿ񱻰��¾���		
		switch(e.getKeyCode()){			//�����������ԣ���̹�˴��ӵ����������ӵ����ӵ�
		case KeyEvent.VK_UP: 	
			bU= true;
			break;
		case KeyEvent.VK_DOWN: 	
			bD= true;
			break;
		case KeyEvent.VK_LEFT: 	
			bL= true;
			break;
		case KeyEvent.VK_RIGHT: 
			bR= true;
			break;
		case KeyEvent.VK_Z:
			TelePort();
			break;
		}
		locateDirection();

	}	
	


	public void keyReleased(KeyEvent e){
		
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP: 	
			bU= false;
			break;
		case KeyEvent.VK_DOWN: 	
			bD= false;
			break;
		case KeyEvent.VK_LEFT: 	
			bL= false;
			break;
		case KeyEvent.VK_RIGHT: 
			bR= false;
			break;
		case KeyEvent.VK_X:
			fire();
			break;
		case KeyEvent.VK_C:
			superFire();
			break;
		
		}
		locateDirection();
		
	}

	public void fire(){ //������ӵ�����new ���� �ӵ������кܲ����㣬һ����Ҫ����д������������һ������Ҫ���Tank��ĳ�Ա����
		if(!live ) return ;
		int xMissile = x + Tank.w/2- Missile.w/2;//ȷ���ӵ���λ��
		int yMissile = y + Tank.h/2- Missile.h/2;
		
		Missile m = new Missile(xMissile,yMissile,barrelDir,this.good,this.tc);//����Tank���������Ա�����ܷ���
		tc.missiles.add(m);												 //��Tank��ӵ�е�tc��ΪMissile�Ĳ������ݹ�ȥ
	}
	
	public void fire(Dir dire){ //����fire����
		if(!live ) return ;
		int xMissile = x + Tank.w/2- Missile.w/2;//ȷ���ӵ���λ��
		int yMissile = y + Tank.h/2- Missile.h/2;
		
		Missile m = new Missile(xMissile,yMissile,dire,this.good,this.tc);//����Tank���������Ա�����ܷ���
		tc.missiles.add(m);	
	}
	
	public void superFire(){ //������fire�ĵڶ������ط��������ؽ���ͬ������дһ�顣��8���������ڵ�
		Dir [] dirs = Dir.values();
		for(int i = 0; i< 8;i++){
			fire(dirs[i]);										 
		}
	}
	private void TelePort() {
		x = r.nextInt(800);
		y = r.nextInt(600);
	}
	public void collidesWithWall(Wall wall){
		if(live && this.getRect().intersects(wall.getRect()) ){
			stay();		//	this.dir = Dir.STOP; ���STOP�Ļ�����ô�´�����ƶ�ʱ�ᷢ����Ȼ��ǽ��ײ�����ٴ�����ΪSTOP
/*		if(wall.getRect().contains(this.getRect()) && (!this.good)){
				x +=30;
				y +=30;
			}
*/
		}
	}
	public void collidesWithTank(Tank t){
		if(live && t.isLive() && this.getRect().intersects(t.getRect())){
			stay();
		//	t.stay();
		}
	}
	
	public void collidesWithTanks(ArrayList <Tank>tanks){
		for(int i = 0 ;i<tanks.size() ;i++){
			if(!this.equals(tanks.get(i)))
				collidesWithTank(tanks.get(i));
		}
	}
	public void stay(){
		x = oldX;
		y = oldY;
	}
	public void eatBlood(Blood blood){
		if(live && blood.isLive() && this.getRect().intersects(blood.getRect())){
			hp = 100;
			blood.setLive(false);
		}
	}
	class BloodBar {

		public void draw(Graphics g){
			if(!live) return ;
			Color c = g.getColor();
			g.setColor(Color.red);
			g.drawRect(x, y-10, w, 10);			
			g.fillRect(x, y-10, w*hp/100, 10);
			g.setColor(c);
		}		
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,w,h);
	}
	
	public boolean isGood() {
		return good;
	}
	
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	public Dir getDir() {
		return dir;
	}
	
	public void setDir(Dir dir){
		this.dir = dir;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
