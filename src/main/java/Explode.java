import java.awt.*;

public class Explode {
	private int x;
	private int y;
//	private int [] diaMeter = {4,7,12,18,26,32,49,30,14,5};
	private int step = 0;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image [] imgs = { //设置为静态，所有的爆炸都使用这些图片；如果动态那么每个Explode对象都要加载一遍这些图片
		tk.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),	
		tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/10.gif"))
	};
	private static boolean init = false;
	private boolean live = true;
	private TankClient tc = null; //拥有TankClient的引用是为了从容器中去除当前对象（爆炸）
	
	public Explode (int x,int y,TankClient tc){
		this.x= x;
		this.y = y;
		this.tc =tc;
	}
	
	public void draw(Graphics g){
		if(!init){
			for(int i = 0; i< imgs.length ;i++){
				g.drawImage(imgs[i], -100, -100, null);
			}
		}
		if(!live){
			tc.explodes.remove(this); //死掉之后从容器中取出，参照子弹类的做法，用来节省内存空间
			return ;
		}
		
		if(step == imgs.length){
			live = false;
			step = 0;
			return;
		
		}
		Color c = g.getColor();
		g.setColor(Color.black);
		g.drawImage(imgs[step], x, y,null);
		step++;
		g.setColor(c);
	}
}
