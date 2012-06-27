package hackersNL;

import robocode.ScannedRobotEvent;

public class DefaultMovementStrategy extends Strategy
{
    int direction = 1;
    
	public DefaultMovementStrategy()
	{
		setType( TYPE_MOVE );
	}
	
	public void onScannedRobot(BerendBotje me, ScannedRobotEvent e)
	{
		int preferredDistance = 190; // Preferred distance from target
		
		// Toggle direction
		if (me.getDistanceRemaining() == 0) {
			direction *= -1;
			me.setAhead( 185 * direction );
		}
		
		// Get closer or move away?
		int side = e.getDistance() > preferredDistance ? 1 : -1;
		
		// Keep away from target, evade bullets
		me.setTurnRightRadians( e.getBearingRadians() + Math.PI/2 - 0.5123 * direction * side );
	}
}
