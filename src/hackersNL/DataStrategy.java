package hackersNL;

import robocode.RobotDeathEvent;

public class DataStrategy extends Strategy
{
	/**
	 * Remove bot from enemyStack
	 */
	public void onRobotDeath( BerendBotje me, RobotDeathEvent e )
	{
		me.getData().removeEnemy( e.getName() );
		super.onRobotDeath( me, e );
	}
}
