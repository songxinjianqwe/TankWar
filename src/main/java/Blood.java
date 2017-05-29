import java.awt.*;
public class Blood {
	private int x ;
	private int y;
	private boolean live = true;
	private int [][] position = {
			{150,400},{200,200},{300,300},{400,400},{500,500},{700,550}
	};
	int step = 0;
	int count = 0;
	public static final int w = 15;
	public static final int h = 15;
	TankClient tc = null;
	
	public Blood(){
		x = position[0][0];
		y = position[0][0];
		
	}
	public void draw(Graphics g){
		if(!live ) {
			count++;
			if(count >= 300){
				live  = true;
				move();
				count = 0;
			}			
			return;
		}
		
		if(count < 100){
			Color c = g.getColor();
			g.setColor(Color.red);
			g.fillRect(x,y, w, h);		
			g.setColor(c);
		}
		else{
			move();
			count = 0;
		}			
		count++;
	}
	
	public void move(){
		step ++;
		if(step == position.length )
			step = 0;	
		x = position[step][0];
		y = position[step][1];
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	public Rectangle getRect(){
		return new Rectangle(position[step][0], position[step][1],w,h);
	}

}
