package hackersNL;

import java.util.EmptyStackException;
import java.util.Stack;

import robocode.ScannedRobotEvent;

public class DataContainer
{
	protected Stack<Enemy> enemies = new Stack<Enemy>();
	protected Enemy _target;
	protected double lastBulletAngle;
	
	public void addEnemy( Enemy e )
	{
		for ( int i = 0; i < enemies.size(); i++ ) {
			if ( enemies.get( i ).getName().equals( e.getName() ) ) {
				// Update existing and exit
				//System.out.println( "Updating enemy data for " + e.getName() );
				enemies.set( i, e );
				return;
			}
		}
		// No existing bot found, push new onto stack
		System.out.println( "Adding new enemy " + e.getName() );
		enemies.push( e );
	}
	
	/**
	 * Detect existing data and update, if no matching Enemy was found, a new enemy is added
	 * @param scanEvent
	 */
	public Enemy processScanEvent( ScannedRobotEvent scanEvent, BerendBotje me )
	{
		for ( int i = 0; i < enemies.size(); i++ ) {
			if ( enemies.get( i ).getName().equals( scanEvent.getName() ) ) {
				// Update existing and exit
				//System.out.println( "Updating enemy data for " + scanEvent.getName() );
				enemies.get( i ).setScanEvent( scanEvent );
				return enemies.get( i );
			}
		}
		
		// Nothing found, create new Enemy and add to stack
		Enemy enemy = new Enemy( me, scanEvent );
		addEnemy( enemy );
		return enemy;
	}
	
	public void removeEnemy( String name )
	{
		// Has our target died?
		if ( hasTarget() && name.equals( _target.getName() ) ) {
			_target = null;
		}
		for ( int i = 0; i < enemies.size(); i++ ) {
			if ( enemies.get( i ).getName().equals( name ) ) {
				// Remove and exit
				System.out.println( "Removing enemy " + name );
				enemies.remove( i );
				return;
			}
		}		
	}
	
	public Enemy getEnemy( String name )
	{
		for ( int i = 0; i < enemies.size(); i++ ) {
			if ( enemies.get( i ).getName().equals( name ) ) {
				Enemy e = enemies.get( i );
				return e;
			}
		}
		throw new EmptyStackException();
	}
	
	public void resetEnemyStack()
	{
		System.out.println( "Resetting all enemy knowledge" );
		enemies.clear();
	}
	
	public Enemy getClosestEnemy()
	{
		if ( enemies.size() == 0 ) throw new EmptyStackException();
		
		double closest = 1000000;
		Enemy closestEnemy = enemies.firstElement(); // 2bsure
		
		for ( int i = 0; i < enemies.size(); i++ ) {
			Enemy e = enemies.get( i );
			if ( e.getDistance() < closest ) {
				closest = e.getDistance();
				closestEnemy = e;
			}
		}
		return closestEnemy;
	}
	
	public Stack<Enemy> getEnemies()
	{
		return enemies;
	}
	
	public void setTarget( Enemy e )
	{
		System.out.println( "New target: " + e.getName() );
		_target = e;
	}
	
	public Enemy getTarget()
	{
		return _target;
	}
	
	public Boolean hasTarget()
	{
		return  _target != null;
	}
	
	public void setLastBulletAngle ( double angle )
	{
		lastBulletAngle = angle;
	}
	
	public double getLastBulletAngle ()
	{
		return lastBulletAngle;
	}
}
