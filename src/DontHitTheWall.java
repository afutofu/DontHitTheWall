import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

public class DontHitTheWall implements ActionListener, KeyListener{
	
	public static DontHitTheWall dontHitTheWall;
	
	public Renderer renderer;
	
	public final int WIDTH = 600, HEIGHT = 800;
	
	public int ticks, xP1Speed = 0, xP2Speed = 0, xMotion = 13, score, maxScore;
	
	public float yWall = 5;
	
	public Rectangle p1, p2;
	
	public ArrayList<Rectangle> walls;
	
	public Timer timer;
	
	public Random rand;
	
	public boolean left, right, gameOver, started;
	
	public DontHitTheWall(){
		
		JFrame frame = new JFrame();
		
		renderer = new Renderer();
		
		timer = new Timer(20, this);
		
		rand = new Random();
		
		frame.add(renderer);
		frame.setSize(WIDTH, HEIGHT);
		frame.addKeyListener(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Random Game");
		//frame.setResizable(false);
		frame.setVisible(true);
		
		p1 = new Rectangle(WIDTH/2 - 70, HEIGHT/2 + 100, 30, 30);
		p2 = new Rectangle(WIDTH/2, HEIGHT/2 + 100, 30, 30);
		walls = new ArrayList<Rectangle>();
		
		//Start Timer
		timer.start();
		
	}
	
	public void reset(){
		
		walls.clear();
		
		p1 = new Rectangle(WIDTH/2 - 70, HEIGHT/2 + 100, 30, 30);
		
		if(p2 != null){
			p2 = new Rectangle(WIDTH/2, HEIGHT/2 + 100, 30, 30);
		}
		
		xP1Speed = 0;
		xP2Speed = 0;
		yWall = 5;
		score = 0;
		
		addWall(true);
		addWall(true);
		addWall(true);
		addWall(true);
		
		started = true;
		gameOver = false;
		
	}
	
	public void addWall(boolean start){
		
		int space = 100;
		int width = rand.nextInt(WIDTH-WIDTH/3);
		int height = 80;
		int spaceBetweenWall = 200;
		
		if(start){
			
			walls.add(new Rectangle(0, 0 - height - walls.size() * spaceBetweenWall, width, height));
			walls.add(new Rectangle(width + space, 0 - height - (walls.size() - 1) * spaceBetweenWall, WIDTH - width - space, height));                    
			
		}else{
			
			walls.add(new Rectangle(0, walls.get(walls.size() - 1).y - spaceBetweenWall * 2, width, height));
			walls.add(new Rectangle(width + space, walls.get(walls.size() - 1).y, WIDTH - width - space, height));
			
		}
		
	}
	
	public void paintWall(Graphics g, Rectangle wall){
		
		g.setColor(Color.darkGray);
		g.fillRect(wall.x, wall.y, wall.width, wall.height);
		
	}

	public void actionPerformed(ActionEvent arg0) {
		
		if(started){
			yWall += 0.01;
			ticks++;
			
			if(!gameOver){
				
				if(ticks % 1 == 0){
					
					if(p1 != null){
						
						p1.x += xP1Speed;
					
					}
					
					if(p2 != null){
						
						p2.x += xP2Speed;
						
					}
					
				}
				
				if(ticks % 100 == 0){
					System.out.println(yWall);
				}
				
				//Move walls down
				for(Rectangle wall : walls){
					
					wall.y += yWall;
					
				}
			
				//Remove walls and add new ones
				for(int i = 0; i < walls.size(); i++){
					
					Rectangle wall = walls.get(i);
					
					if(wall.y > HEIGHT){
						
						walls.remove(wall);
							
						addWall(false);
						
					}
					
				}
				
				//Checks for score and game over
				for(Rectangle wall : walls){
					
					//Get Score
					if(p1.y + p1.height/2 < wall.y + wall.height/2 + yWall/3 && p1.y + p1.height/2 > wall.y + wall.height/2 - yWall/3){
						
						score++;
						
						if(score > maxScore){
							
							maxScore++;
							
						}
						
					}
					
					if(wall.intersects(p1)){
						
						gameOver = true;
						
					}
					
					if(p2 != null){
						
						if(wall.intersects(p2)){
							
							gameOver = true;
							
						}
					}
				}
				
				if(p1.x < 0 || p1.x > WIDTH){
					gameOver = true;
				}
				
				if(p2 != null){
					
					if(p2.x < 0 || p2.x > WIDTH){
						
						gameOver = true;
						
					}
					
				}
				
			}
			
		}
		
		
		renderer.repaint();
		
	}
	
	public void repaint(Graphics g){
		
		//Paint Screen
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//Paint Player 2
			if(p2 != null){
				g.setColor(Color.blue);
				g.fillRect(p2.x, p2.y, p2.width, p2.height);
			}
		
		//Paint Player 1
		g.setColor(Color.red);
		g.fillRect(p1.x, p1.y, p1.width, p1.height);
				
		
		for(Rectangle wall: walls){
			
			paintWall(g, wall);
			
		}
		
		g.setColor(Color.black);
		g.setFont(new Font("Arial", 50, 50));
		
		if(!gameOver && !started){
			
			g.drawString("Click to Start", WIDTH/2 - WIDTH/3,HEIGHT/2);
			
		}
		
		if(gameOver){
			
			g.drawString("Game Over", WIDTH/2 - WIDTH/3, HEIGHT/2);
			
		}
		
		g.setColor(Color.black);
		g.setFont(new Font("Arial", 20, 20));
		g.drawString(maxScore/2 + "", WIDTH/2 - 20, 20);
		g.drawString(score/2 + "", WIDTH/2 - 20, 40);
		g.drawString((int)yWall + "", WIDTH/2 - 20, 60);
		
	}
	
	public static void main(String[] args){
		
		dontHitTheWall = new DontHitTheWall();
		
	}

	public void keyPressed(KeyEvent e) {
		
		int keyCode = e.getKeyCode();
		
		switch(keyCode){
		
		case KeyEvent.VK_D:
			xP1Speed = xMotion;
			break;
			
		case KeyEvent.VK_A:
			xP1Speed = -xMotion;
			break;
		
		}
		
		switch(keyCode){
		
		case KeyEvent.VK_RIGHT:
			xP2Speed = xMotion;
			break;
			
		case KeyEvent.VK_LEFT:
			xP2Speed = -xMotion;
			break;
		
		}
		
		if(keyCode == KeyEvent.VK_SPACE){
			
			reset();
			
		}
		
	}

	public void keyReleased(KeyEvent e) {
		
		int keyCode = e.getKeyCode();
		
		switch(keyCode){
		
		case KeyEvent.VK_D:
			xP1Speed = 0;
			break;
			
		case KeyEvent.VK_A:
			xP1Speed = 0;
			break;
		
		}
		
		switch(keyCode){
		
		case KeyEvent.VK_RIGHT:
			xP2Speed = 0;
			break;
			
		case KeyEvent.VK_LEFT:
			xP2Speed = 0;
			break;
		
		}
		
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}



