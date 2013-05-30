package hackersNL;

import java.awt.geom.Point2D;

import robocode.ScannedRobotEvent;
import java.util.HashMap;
import java.util.Iterator;

public class Enemy
{
	protected ScannedRobotEvent scanEvent;
	protected Point2D.Double position;
	protected HashMap<String, Integer> shots = new HashMap<String, Integer>();
	protected HashMap<String, Integer> hits = new HashMap<String, Integer>();
	protected HashMap<String, Double> pain = new HashMap<String, Double>();
	protected BerendBotje me;
	
	public Enemy ( BerendBotje localMe, ScannedRobotEvent e )
	{
		me = localMe;
		setScanEvent( e );
	}
	
	public void setScanEvent( ScannedRobotEvent e )
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
	
	public void addShot()
	{
		// Detect current strategy
		Strategy currentStrategy = me.getActiveStrategy( Strategy.TYPE_AIM );
		String strategyName = currentStrategy.getClass().getName();
		
		int currentValue = 0;
		if ( shots.containsKey( strategyName ) ) {
			currentValue = shots.get( strategyName );
		}
		currentValue++;
		//me.setDebugProperty("[" + this.getName() + "] Shots for ", strategyName + ": " + currentValue );
		
		shots.put( strategyName, currentValue );
		
		criticaster(); // Are we still on the right track?
	}
	
	public void addHit()
	{
		// Detect current strategy
		Strategy currentStrategy = me.getActiveStrategy( Strategy.TYPE_AIM );
		String strategyName = currentStrategy.getClass().getName();
		
		int currentValue = 0;
		if ( hits.containsKey( strategyName ) ) {
			currentValue = hits.get( strategyName );
		}
		currentValue++;
		//me.setDebugProperty("[" + this.getName() + "] Hits for ", strategyName + ": " + currentValue );

		hits.put( strategyName, currentValue );
	}
	
	public void addPain( double power )
	{
		// Detect current strategy
		Strategy currentStrategy = me.getActiveStrategy( Strategy.TYPE_MOVE );
		String strategyName = currentStrategy.getClass().getName();
		
		double currentValue = 0;
		if ( pain.containsKey( strategyName ) ) {
			currentValue = pain.get( strategyName );
		}
		currentValue += power;
		//me.setDebugProperty("[" + this.getName() + "] Pain for ", strategyName + ": " + currentValue );

		pain.put( strategyName, currentValue );
	}
	
	public void criticaster() // Cool name, no? :)
	{
		// Get current strategies
		//Strategy currentAimStrategy = me.getActiveStrategy( Strategy.TYPE_AIM );
		//String aimStrategyName = currentAimStrategy.getClass().getName();
		
		Strategy currentMoveStrategy = me.getActiveStrategy( Strategy.TYPE_MOVE );
		String moveStrategyName = currentMoveStrategy.getClass().getName();
		
		// Get shots and hits and get a ratio that represents the effectiveness of the aim strategy
		Iterator<String> iter = shots.keySet().iterator();
		while ( iter.hasNext() ) {
			String strategyName = (String) iter.next();
			double shotCount = shots.get( strategyName );
			double hitCount = 0;
			if ( hits.containsKey( strategyName ) ) {
				hitCount = hits.get( strategyName );
			}
			double aimRatio = hitCount / shotCount;
			me.setDebugProperty("[" + this.getName() + "] Ratio for ", strategyName + ": " + aimRatio );
			
			// Switch strategies
			if ( shotCount > 0 && aimRatio < 0.1 ) {
				// TODO Aim ratio bad, switch to other Aim strategy
			}
		}
		
		// Get pain and decide what move strategy could be best.
		if ( pain.containsKey( moveStrategyName ) && pain.get(moveStrategyName) > 5 ) {
			// TODO Too much damage, switch to other strategy
		}
	}
}