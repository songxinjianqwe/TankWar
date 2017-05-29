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
	public static final Random r = new Random();//随机数生成器
	int step = r.nextInt(12)+3;//随机移动3~14步
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
	//static 是该Class 加载到内存后首先执行的一段代码
	//比直接初始化的好处是本来不可以执行代码的地方（定义成员变量的区域），通过这种方式就可以执行代码了。
	static {//静态代码区，适合给变量做初始化
		tankImgs = new Image[] { //设置为静态，所有的爆炸都使用这些图片；如果动态那么每个Explode对象都要加载一遍这些图片
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
	public Tank(int x,int y,boolean good){ //重载的构造方法，可以适用于不同的情况，有的时候需要传另外一个参数，有的时候则不需要，重载可以满足不同需要。
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
		oldX = x; //先记录上一次位置，再移动
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
		
		if(x < 0) x = 0; //控制位置
		if(y < 30) y = 30;
		if(x + Tank.w > TankClient.w) x = TankClient.w - Tank.w;
		if(y + Tank.h > TankClient.h) y = TankClient.h - Tank.h;
		
		if(!good){ //实现方向和移动步数的随机
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



	
	public void keyPressed(KeyEvent e) { //键盘的按键在按下的瞬间会有时差(跟键长有关系),不可能是你同时按下ABCD时,
										 //键盘传出的值就是ABCD 有可能是ACBD ,ADBC等等所以只要判断你要的几个键是否被按下就行		
		switch(e.getKeyCode()){			//从面向对象而言，是坦克打子弹，而不是子弹打子弹
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

	public void fire(){ //如果在子弹类中new 对象 加到数组中很不方便，一是需要单独写接听方法，另一个是需要获得Tank类的成员变量
		if(!live ) return ;
		int xMissile = x + Tank.w/2- Missile.w/2;//确定子弹的位置
		int yMissile = y + Tank.h/2- Missile.h/2;
		
		Missile m = new Missile(xMissile,yMissile,barrelDir,this.good,this.tc);//而在Tank类引用其成员变量很方便
		tc.missiles.add(m);												 //将Tank中拥有的tc作为Missile的参数传递过去
	}
	
	public void fire(Dir dire){ //重载fire方法
		if(!live ) return ;
		int xMissile = x + Tank.w/2- Missile.w/2;//确定子弹的位置
		int yMissile = y + Tank.h/2- Missile.h/2;
		
		Missile m = new Missile(xMissile,yMissile,dire,this.good,this.tc);//而在Tank类引用其成员变量很方便
		tc.missiles.add(m);	
	}
	
	public void superFire(){ //，调用fire的第二个重载方法，不必将相同代码再写一遍。向8个方向发射炮弹
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
			stay();		//	this.dir = Dir.STOP; 如果STOP的话，那么下次随机移动时会发现仍然与墙相撞，会再次设置为STOP
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
