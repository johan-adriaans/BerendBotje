package hackersNL;

import java.util.EmptyStackException;
import robocode.ScannedRobotEvent;

public class ScanStrategy extends Strategy
{
	public int ticks = 0;
	
	public ScanStrategy()
	{
		setType( TYPE_SCAN );
	}
	
	/**
	 * Do a full 360 scan, store all bots in the DataContainer, select new target and set new strategy
	 */
	public void onTick( BerendBotje me )
	{
		if ( ticks == 0 ) {
		  me.setTurnRadarLeft( 360 );
		}
		
		if ( ticks > 0 && me.getRadarTurnRemaining() == 0 ) {
			try {
				me.getData().setTarget( me.getData().getClosestEnemy() );
				me.addStrategy( new AggressiveStrategy() );
			} catch ( EmptyStackException e ) {
				System.out.println( "No more enemies.." );
			}
		}
		
		me.scan(); // Do something
		
		ticks++;
		super.onTick( me );
	}
	
	@Override
	public void onScannedRobot( BerendBotje me, ScannedRobotEvent e )
	{
		me.getData().addEnemy( new Enemy( me, e ) );
		super.onScannedRobot(me, e);
	}
}
