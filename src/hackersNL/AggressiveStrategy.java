package hackersNL;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;


import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class AggressiveStrategy extends Strategy
{
	protected int lastScannedX = Integer.MIN_VALUE;
	protected int lastScannedY = Integer.MIN_VALUE;
	protected double predictedX = Integer.MIN_VALUE;
	protected double predictedY = Integer.MIN_VALUE;
	
	protected int targetLessTurns = 0;
	protected int aggressiveTurns = 0;
	protected int shotlessTurns = 0;
	
	public AggressiveStrategy()
	{
		setType( TYPE_AIM + TYPE_SCAN );
	}
	
	@Override
	public void onTick( BerendBotje me )
	{
		targetLessTurns++;
		aggressiveTurns++;
		
		// Initial radar movement
		double radarMovement = 10;
		
		me.turnRadarLeft( radarMovement );
		
		if ( targetLessTurns > 5 ) {
			radarMovement = -10;
		}
		if ( targetLessTurns > 10 ) {
			radarMovement = 10;
		}
		
		super.onTick(me);
	}
	
	@Override
	public void onScannedRobot( BerendBotje me, ScannedRobotEvent e )
	{
		// Update this enemy's data in the enemy stack
		Enemy enemy = me.getData().processScanEvent( e, me );

		// Only attack current target
		if ( me.getData().hasTarget() ) {
			if ( e.getName().equals( me.getData().getTarget().getName() ) ) {
				targetLessTurns = 0;
				me.setDebugProperty("lastScannedRobot", e.getName() + " at " + e.getBearing() + " degrees at time " + me.getTime());
				
				// Get firepower based on remaining energy
				double firePower = Math.min( 3, Math.max( .1, me.getEnergy() / 25 ) );
				me.setDebugProperty( "lastFirePower", "Power level " + firePower );
				
				// Calculate bullet speed
				double bulletSpeed = 20 - 3 * firePower;
					
				// Calculate the angle to the scanned robot
				double angle = Math.toRadians((me.getHeading() + e.getBearing()) % 360);
		
				// Calculate the coordinates of the robot
				lastScannedX = (int) ( me.getX() + Math.sin( angle ) * e.getDistance() );
				lastScannedY = (int) ( me.getY() + Math.cos( angle ) * e.getDistance() );
				
				double myX = me.getX();
				double myY = me.getY();
				double absoluteBearing = me.getHeadingRadians() + e.getBearingRadians();
				double enemyHeading = e.getHeadingRadians();
				double enemyVelocity = e.getVelocity();
				 
				double time = 0;
				double battleFieldHeight = me.getBattleFieldHeight(),
				       battleFieldWidth = me.getBattleFieldWidth();
				
				predictedX = lastScannedX;
				predictedY = lastScannedY;
				while( ++time * bulletSpeed < Point2D.Double.distance( myX, myY, predictedX, predictedY ) ){		
					predictedX += Math.sin(enemyHeading) * enemyVelocity;
					predictedY += Math.cos(enemyHeading) * enemyVelocity;
					//double sanitycheck = predictedX / predictedY;
					predictedX = Math.min( Math.max( 18.0, predictedX ), battleFieldWidth - 18.0 );	
					predictedY = Math.min( Math.max( 18.0, predictedY ), battleFieldHeight - 18.0 );
					//if ( sanitycheck != predictedX / predictedY ) break;
				}
				
				double theta = Utils.normalAbsoluteAngle( Math.atan2( predictedX - me.getX(), predictedY - me.getY() ) );
				me.setTurnGunRightRadians( Utils.normalRelativeAngle( theta - me.getGunHeadingRadians() ) );
				me.setTurnRadarRightRadians( Utils.normalRelativeAngle( absoluteBearing - me.getRadarHeadingRadians() ) );
				
				if ( shotlessTurns > 90 | ( e.getDistance() < 200 && firePower > Rules.MIN_BULLET_POWER && me.getGunTurnRemaining() <= 1 && me.getGunHeat() == 0 ) ) {
					enemy.addShot(); // Update shot counter to detect strategy effectiveness
					me.setFire( firePower );
					shotlessTurns = 0;
				} else {
					shotlessTurns++;
				}
				
				me.setDebugProperty("Target position prediction", predictedX + "x" + predictedY );
			}
		} else {
			// No target? Find one!
			System.out.println( "I dont have a target.. Switching strategies" );
			me.addStrategy( new ScanStrategy() );
		}
		super.onScannedRobot( me, e );
	}
	
	@Override
	public void onHitByBullet( BerendBotje me, HitByBulletEvent e )
	{
		if ( !me.getData().hasTarget() ) {
			me.addStrategy( new ScanStrategy() );
			return;
		}
		
		// Get at least some agressive turns in, preventing a loop ;)
		if ( aggressiveTurns > 50 ) {
			// Calculate the angles
			double bulletAngle = ( me.getHeading() + e.getBearing() ) % 360;
			double targetAngle = ( me.getHeading() + me.getData().getTarget().getBearing() ) % 360;
			
			me.getData().setLastBulletAngle( bulletAngle );
			
			me.setDebugProperty("Bullet/target angle diff", bulletAngle + " - " + targetAngle + " = " + Math.abs( bulletAngle - targetAngle ) );	
			
			// Check bullet source, is it our current target?
			if ( Math.abs( Math.abs( bulletAngle - targetAngle ) ) > 30 ) {
				// If not, have a look at the other bots
				me.addStrategy( new ScanStrategy() );
			}
		}
		super.onHitByBullet(me, e);
	}
	
	@Override
	public void onHitRobot(BerendBotje me, HitRobotEvent e) {
		if ( !me.getData().hasTarget() || !e.getName().equals( me.getData().getTarget().getName() ) ) {
			// Someone is ramming us, look around and target him (if he is still closest)
			me.addStrategy( new ScanStrategy() );
		}
	}

	/**
	 * Paint debug info
	 */
	@Override
	public void onPaint( BerendBotje me, Graphics2D g) 
	{
		g.setColor(new Color(0xff, 0x00, 0x00, 0x80));
		g.drawLine(lastScannedX, lastScannedY, (int) me.getX(), (int) me.getY());
		g.fillRect(lastScannedX - 20, lastScannedY - 20, 40, 40);
		g.fillRect((int) me.getX() - 20, (int) me.getY() - 20, 40, 40);

		g.setColor(new Color(0xff, 0x00, 0xff, 0x80));
		g.drawLine( (int) predictedX, (int) predictedY, (int) me.getX(), (int) me.getY());
		g.fillRect( (int) predictedX - 20, (int) predictedY - 20, 40, 40);
		
		super.onPaint( me, g );
	}
}
