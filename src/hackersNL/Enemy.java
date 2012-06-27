package hackersNL;

import java.awt.geom.Point2D;

import robocode.ScannedRobotEvent;

public class Enemy
{
	protected ScannedRobotEvent scanEvent;
	protected Point2D.Double position;
	
	public Enemy ( BerendBotje me, ScannedRobotEvent e )
	{
		scanEvent = e;
		
		// Calculate the angle to the enemy
		double angle = Math.toRadians((me.getHeading() + e.getBearing()) % 360);

		// Calculate the coordinates of the robot
		double x = ( me.getX() + Math.sin( angle ) * e.getDistance() );
		double y = ( me.getY() + Math.cos( angle ) * e.getDistance() );
		position = new Point2D.Double( x, y );
	}
	
	public String getName()
	{
		return scanEvent.getName();
	}
	
	public double getDistance()
	{
		return scanEvent.getDistance();
	}
	
	public double getBearing()
	{
		return scanEvent.getBearing();
	}
	
	public double getHeading()
	{
		return scanEvent.getHeading();
	}
	
	public double getBearingRadians()
	{
		return scanEvent.getBearingRadians();
	}
	
	public double getEnergy()
	{
		return scanEvent.getEnergy();
	}
	
	public Point2D.Double getPosition()
	{
		 return position;
	}
}
