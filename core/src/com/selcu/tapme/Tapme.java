package com.selcu.tapme;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Align;

import java.rmi.ConnectIOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Tapme extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture bird;
	Texture enemy1,enemy2,enemy3;
	float bx,by,bw,bh;
	float sw,sh,ex,ey;
	float gravity=0.3f;
	float v=0.0f;
	int state=0;
	int enumber=3;
	float enemyx[]=new float [enumber];
	float distance ;
	ShapeRenderer sr;
	float enemyy[][]=new float[3][enumber];

	Circle c_bird;
	Circle c_enemy1[] =new Circle[enumber];
	Circle c_enemy2[] =new Circle[enumber];
	Circle c_enemy3[] =new Circle[enumber];
	BitmapFont font;
	Sound sound;
	int i =0;
	Sound sound2;
	BitmapFont font1;
	BitmapFont font2;
	int score;
	boolean flag=true;
	boolean flag1=true;
	@Override
	public void create () {
		batch =new SpriteBatch();
		img =new Texture("back2.png");
		bird =new Texture("frame-1.png");
		enemy1 =new Texture("enemy.png");
		enemy2 =new Texture("enemy.png");
		enemy3 =new Texture("enemy.png");
        sh=Gdx.graphics.getHeight();
        sw=Gdx.graphics.getWidth();
		bx=sw/4;
		by=sh/3;
		bw=sw/13;
		bh=sh/10;
		ex=1200;
		ey=300;
		sr =new ShapeRenderer();
		c_bird= new Circle();
		c_enemy1=new Circle[enumber];
		c_enemy2=new Circle[enumber];
		c_enemy3=new Circle[enumber];
		distance=Gdx.graphics.getWidth()/2;
		font =new BitmapFont();
		font.setColor(Color.RED);
		font.getData().setScale(8);
		sound =Gdx.audio.newSound(Gdx.files.internal("lose.ogg"));
		sound2 =Gdx.audio.newSound(Gdx.files.internal("ss.mp3"));
		font1 =new BitmapFont();
		font2 =new BitmapFont();
		font1.setColor(Color.BLACK);
		font1.getData().setScale(10);
        font1.setColor(Color.BLACK);
        font2.getData().setScale(10);
        Color c=new Color(121/255f,0,0,1);
        font2.setColor(c);
		for (int i=0;i<enumber;i++){
			enemyx[i]=Gdx.graphics.getWidth()+i*distance;//Her bir x için bir sonraki enemy yarısı kadar yakında olacak
			Random r1=new Random();
			Random r2=new Random();
			Random r3=new Random();

			enemyy[0][i]=r1.nextFloat()*Gdx.graphics.getHeight();
			enemyy[1][i]=r2.nextFloat()*Gdx.graphics.getHeight();
			enemyy[2][i]=r3.nextFloat()*Gdx.graphics.getHeight();
			c_enemy1[i]=new Circle();
			c_enemy2[i]=new Circle();
			c_enemy3[i]=new Circle();
		}

	}

	@Override
	public void render () {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		Calendar cal = Calendar.getInstance();

        System.out.println("Render:"+i);
		System.out.println(dateFormat.format(cal.getTime())); //2016/11/16 12:08:43
        i++;
		batch.begin();
		batch.draw(img,0,0,sw,sh);
		batch.draw(bird,bx,by,bw,bh);
		if (state==1){
			flag1=true;
			if(Gdx.input.justTouched()){ //ekrana tıklandığında
				sound2.play();
				v=-9;
			}
			for (int i=0;i<enumber;i++){
				if (enemyx[i]<bw){
					enemyx[i]=enemyx[i]+enumber*distance; //sürekli enemy gelmesi için
					Random r1=new Random();
					Random r2=new Random();
					Random r3=new Random();

					enemyy[0][i]=r1.nextFloat()*Gdx.graphics.getHeight();
					enemyy[1][i]=r2.nextFloat()*Gdx.graphics.getHeight();
					enemyy[2][i]=r3.nextFloat()*Gdx.graphics.getHeight();
				}
				if (bx>enemyx[i] && flag){
					score++;
					System.out.println(score);
					flag=false;
				}
				if(enemyx[i]<bw+4) //enemy ekrandan çıkmışsa
				{
					flag=true;
				}


				enemyx[i]-=4;
				batch.draw(enemy1,enemyx[i],enemyy[0][i],bw,bh);//enemy alt alta yazdırma
				batch.draw(enemy2,enemyx[i],enemyy[1][i],bw,bh);
				batch.draw(enemy3,enemyx[i],enemyy[2][i],bw,bh);
			}
			if(enemyx[0]-bx<10){
				for(int y=0;y<enumber;y++){
					System.out.println("sdasd");
					//sr.setColor(Color.BLUE);
					//sr.circle(enemyx[y]+bw/2,enemyy[0][y]+bh/2,bw/2);
					//sr.circle(enemyx[y]+bw/2,enemyy[1][y]+bh/2,bw/2);
					//sr.circle(enemyx[y]+bw/2,enemyy[2][y]+bh/2,bw/2);
					c_enemy1[y].set(enemyx[y]+bw/2,enemyy[0][y]+bh/2,bw/2);
					c_enemy1[y].set(enemyx[y]+bw/2,enemyy[0][y]+bh/2,bw/2);
					c_enemy1[y].set(enemyx[y]+bw/2,enemyy[0][y]+bh/2,bw/2);
						if(Intersector.overlaps(c_bird,c_enemy1[y]) || Intersector.overlaps(c_bird,c_enemy2[y]) || Intersector.overlaps(c_bird,c_enemy3[y])){
							state=2;
							System.out.println("Overlap");
						}

					// çarpışmayı anlama
				}
			}
		    if(by<0)
            {
            	state=2;
            	by=sh/3;
            	v=0;
            }
            else{
                v=v+gravity;
                by= by-v;
            }

		}
		else if(state==2)
			{
				//System.out.println(Gdx.graphics.getWidth());
				//System.out.println(Gdx.graphics.getHeight());
				score=0;
			//	public GlyphLayout addText (CharSequence str, float x, float y, float targetWidth, int halign,
				BitmapFontCache	cache = new BitmapFontCache(font1);

			//	public GlyphLayout (BitmapFont font, CharSequence str, Color color, float targetWidth, int halign, boolean wrap) {
				GlyphLayout layout2 =new GlyphLayout(font1,"Game Over!");
				GlyphLayout layout3 =new GlyphLayout(font2,"Tap Me!");
				//GlyphLayout layout = cache.addText("Game Over!", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Align.center,true);
				//System.out.println(layout2.width);
				//System.out.println(layout2.height);
				font1.draw(batch,layout2,Gdx.graphics.getWidth()/2-(layout2.width/2),Gdx.graphics.getHeight()/2+(layout2.height/2));
				//font1.draw(batch,"Game Over!",Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),800,Align.center,true);
				font2.draw(batch,layout3,Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
				if (flag1)
				{
					sound.play();
					flag1=false;
				}
				sound.dispose();

				v=0;
				if(Gdx.input.justTouched()){
					state=1;
					for (int i=0;i<enumber;i++){
						enemyx[i]=Gdx.graphics.getWidth()+i*distance;//Her bir x için bir sonraki enemy yarısı kadar yakında olacak
						Random r1=new Random();
						Random r2=new Random();
						Random r3=new Random();

						enemyy[0][i]=r1.nextFloat()*Gdx.graphics.getHeight();
						enemyy[1][i]=r2.nextFloat()*Gdx.graphics.getHeight();
						enemyy[2][i]=r3.nextFloat()*Gdx.graphics.getHeight();
						c_enemy1[i]=new Circle();
						c_enemy2[i]=new Circle();
						c_enemy3[i]=new Circle();
					}

				}
			}
		else if (state==0)
			{
				flag1=true;
				font2.draw(batch,"Tap Me!",Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3 );
				//sound.play();
				score=0;
				v=0;
				if (Gdx.input.justTouched()){
					state=1;

			}

		}
		font.draw(batch,String.valueOf(score),Gdx.graphics.getWidth()-bw,bh);
		c_bird.set(bx+bw/2,by+bh/2,bw/2);
		//sr.begin(ShapeRenderer.ShapeType.Filled);

		//sr.circle(bx+bw/2,by+bh/2,bw/2);



		batch.end();
		//sr.end();


	}
	
	@Override
	public void dispose () {
	}
}
