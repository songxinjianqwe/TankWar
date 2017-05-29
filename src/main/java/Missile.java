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
	//static 是该Class 加载到内存后首先执行的一段代码
	//比直接初始化的好处是本来不可以执行代码的地方（定义成员变量的区域），通过这种方式就可以执行代码了。
	static {//静态代码区，适合给变量做初始化
		tankImgs = new Image[] { //设置为静态，所有的爆炸都使用这些图片；如果动态那么每个Explode对象都要加载一遍这些图片
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
		if(!live){  //每次重绘之前先判断子弹是否已经死亡，如果死亡（可能是击中坦克，也可能是飞出窗口），
					//首先将其从容器中移除，然后跳过其重绘的过程
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
				
	public void hitTank(Tank t){ //判断一个特定的子弹是否击中坦克，不能在Tank类加入该方法，因为要判断每一发子弹和坦克的位置关系，否则取出子弹的位置会很麻烦。
		
		if( t.isLive() &&this.live && this.getRect().intersects(t.getRect())  && this.good != t.isGood() ){ //从面向对象思维而言，是子弹击中坦克；所有的判断是否重合的方法都需要参数，调用的是移动的对象，被调用的参数等待被碰撞的。
			if(t.isGood()){																				  //要求坦克和子弹都是活着的，否则就不必去判断是否相撞
				t.setHp(t.getHp() -20);
				if(t.getHp() <= 0 )
					t.setLive(false);
			}
			else
				t.setLive(false);//如果不判断坦克是否死亡，那么其他子弹飞到已经死亡的坦克的位置上仍然会消失					
			this.live = false;										//如果子弹和坦克不是同一方的才可以打死
			tc.explodes.add(new Explode(x,y, tc));			
		}	 		
	}
	
	public void hitTanks(ArrayList<Tank> enemyTanks){ //这样的话就不必在TankClient中使用嵌套循环了，直接调用该方法即可
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

