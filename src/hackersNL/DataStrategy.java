package hackersNL;

import java.util.EmptyStackException;

import robocode.BulletHitEvent;
import robocode.HitByBulletEvent;
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
	
	public void onBulletHit( BerendBotje me, BulletHitEvent e )
	{
		// Get enemy from datacontainer and add hit
		try {
			Enemy enemy = me.getData().getEnemy( e.getName() );
			enemy.addHit(); // Update hit counter to detect strategy effectiveness
		} catch ( EmptyStackException event ) {}
	}
	
	public void onHitByBullet ( BerendBotje me, HitByBulletEvent e ) {
		// Get enemy from datacontainer and add hit
		try {
			Enemy enemy = me.getData().getEnemy( e.getName() );
			enemy.addPain( e.getPower() ); // Update hit counter to detect strategy effectiveness
		} catch ( EmptyStackException event ) {}		
	}
}
