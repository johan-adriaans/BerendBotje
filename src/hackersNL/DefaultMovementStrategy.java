package hackersNL;

import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;

public class DefaultMovementStrategy extends Strategy
{
    int direction = 1;
    protected ScannedRobotEvent scanEvent;
    
	public DefaultMovementStrategy()
	{
		setType( TYPE_MOVE );
	}
	
	public void onScannedRobot(BerendBotje me, ScannedRobotEvent e)
	{
		// Catch initial call
		if ( scanEvent == null ) {
			scanEvent = e;
			return;
		}
				
		int preferredDistance = 20; // Preferred distance from target
		
		// Get closer or move away?
		int side = e.getDistance() > preferredDistance ? 1 : -1;
		
		// Detect bullet energy drop
		if ( e.getTime() - scanEvent.getTime() < 5 && scanEvent.getEnergy() - e.getEnergy() <= 3 && scanEvent.getEnergy() - e.getEnergy() > 0 ) {
			System.out.println( "Shot detected: " + (scanEvent.getEnergy() - e.getEnergy()) );
			// steer towards enemy on bullet detection
			me.setTurnRightRadians( e.getBearingRadians() * direction * side );
		}
		
		// Toggle direction
		if (me.getDistanceRemaining() == 0) {
			direction *= -1;
			me.setAhead( ( e.getDistance() / 4 + 60 ) * direction );
		}
		
		// Keep away from target, evade bullets
		me.setTurnRightRadians( e.getBearingRadians() + Math.PI/2 - 0.6 * direction * side );
		
		scanEvent = e;
	}
	
	public void onHitWall( BerendBotje me, HitWallEvent e )
	{
		direction *= -1;
		me.setAhead( Math.random() * 300 * direction );
	}
}
