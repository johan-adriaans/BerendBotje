package hackersNL;


import java.awt.Color;
import java.awt.Graphics2D;
import java.util.EmptyStackException;
import java.util.Stack;


import robocode.*;

public class BerendBotje extends AdvancedRobot 
{	
	protected DataContainer _data = new DataContainer();
	protected Stack<Strategy> strategies = new Stack<Strategy>();
	
	public void run()
	{	
		// Make me purdy
		setBodyColor( new Color( 0x93002F ) );
		setGunColor( new Color( 0xF16C97 ) );
		setRadarColor( new Color( 0xE20048 ) );
		setScanColor( Color.white );
		setBulletColor( new Color( 0xF13C76 ) );

		// Detach gun, radar and robot movement
		//setAdjustGunForRobotTurn( true );
		setAdjustRadarForGunTurn( true );
		
		// Initial strategy
		strategies.push( new DataStrategy() );
		strategies.push( new ScanStrategy() );
		strategies.push( new DefaultMovementStrategy() );
		//strategies.push( new ReactMovementStrategy() );
		
		// Main loop
		while (true) {
			for ( int i = 0; i < strategies.size() ; i++ ) {
				strategies.get( i ).onTick( this );
			}
		}
	}
	
	public void addStrategy( Strategy strategy )
	{
		System.out.println( "Adding strategy " + strategy.toString() );
		
		// Test for colliding strategy types and remove them from the strategy stack
		for ( int i = 0; i < strategies.size() ; i++ ) {
			if ( strategies.get( i ).toString() == strategy.toString() ) return; // Already there :)
			if ( strategies.get( i ).collidesWithType( strategy.getType() ) ) {
				System.out.println( "Removing colliding strategy " + strategies.get( i ).toString() );
				strategies.remove( i );
				i--; // Fix vector index shift caused by remove()
			}
		}
		strategies.push( strategy );
	}
	
	/**
	 * Get the active strategy of a specific strategy type
	 * 
	 * @param int	Strategy.TYPE_MOVE, Strategy.TYPE_AIM, Strategy.TYPE_SCAN
	 * @return
	 */
	public Strategy getActiveStrategy( int type )
	{
		for ( int i = 0; i < strategies.size() ; i++ ) {
			if ( strategies.get( i ).collidesWithType( type ) ) {
				Strategy strategy = strategies.get( i );
				return strategy;
			}
		}
		throw new EmptyStackException();
	}
	
	@Override
	public void onHitByBullet( HitByBulletEvent e )
	{
		for ( int i = 0; i < strategies.size() ; i++ ) {
			strategies.get( i ).onHitByBullet( this, e );
		}		
	}
	
	public DataContainer getData()
	{
		return _data;
	}
	
	public void setData ( DataContainer data )
	{
		_data = data;
	}

	public void onScannedRobot( ScannedRobotEvent e )
	{
		for ( int i = 0; i < strategies.size() ; i++ ) {
			strategies.get( i ).onScannedRobot( this, e );
		}
	}
	
	
	public void onRobotDeath( RobotDeathEvent e )
	{
		for ( int i = 0; i < strategies.size() ; i++ ) {
			strategies.get( i ).onRobotDeath( this, e );
		}
	}
	
	public void onHitWall( HitWallEvent e )
	{
		for ( int i = 0; i < strategies.size() ; i++ ) {
			strategies.get( i ).onHitWall( this, e );
		}
	}
	
	public void onHitRobot( HitRobotEvent e )
	{
		for ( int i = 0; i < strategies.size() ; i++ ) {
			strategies.get( i ).onHitRobot( this, e );
		}
	}
	
	public void onBulletHit( BulletHitEvent e )
	{
		for ( int i = 0; i < strategies.size() ; i++ ) {
			strategies.get( i ).onBulletHit( this, e );
		}
	}

	public void onPaint( Graphics2D g) 
	{
		for ( int i = 0; i < strategies.size() ; i++ ) {
			strategies.get( i ).onPaint( this, g );
		}
	}
	
	public void onWin( WinEvent e )
	{
		// Victory dance
		turnRight( 36000 );
		turnGunLeft(36000 );
	}
}
