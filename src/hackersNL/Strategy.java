package hackersNL;



import java.awt.Graphics2D;

import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.BulletHitEvent;

public class Strategy
{	
	static int TYPE_MOVE = 1;
	static int TYPE_AIM = 2;
	static int TYPE_SCAN = 4;
	
	protected int _type = 0;
	
	public Strategy()
	{
		// Children should set their type here
	}
	
	public boolean collidesWithType( int t )
	{
		return (_type & t) > 0;
	}
	
	public void setType( int t )
	{
		_type = t;
	}
	
	public int getType()
	{
		return _type;
	}
	
	public void onTick( BerendBotje me ) {}
	
	public void onScannedRobot( BerendBotje me, ScannedRobotEvent e ) {}
	
	public void onRobotDeath ( BerendBotje me, RobotDeathEvent e ) {}
	
	public void onHitWall ( BerendBotje me, HitWallEvent e ) {}
	
	public void onPaint ( BerendBotje me, Graphics2D g ) {}
	
	public void onHitRobot( BerendBotje me, HitRobotEvent e ) {}

	public void onBulletHit ( BerendBotje me, BulletHitEvent e ) {}
	
	public void onHitByBullet( BerendBotje me, HitByBulletEvent e ) {}
}
