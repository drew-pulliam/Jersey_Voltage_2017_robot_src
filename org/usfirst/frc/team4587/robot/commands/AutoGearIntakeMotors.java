package org.usfirst.frc.team4587.robot.commands;

import org.usfirst.frc.team4587.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoGearIntakeMotors extends Command {


	int count;
	boolean finish;
	boolean x;
    public AutoGearIntakeMotors() {
    	requires(Robot.getGearIntake());
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	Robot.getGearIntake().setGearIntakeMotor(1.0);
    	count = 0;
    	finish = false;
    	Robot.getGearIntake().setLEDMode();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
    	if (Robot.getGearIntake().isStalling())
    	{
    		if(count >= 0){//50 = 1sec
            	Robot.getGearIntake().gearIntakeUp();
    		}
        	Robot.getGearIntake().setGearIsLoaded(true);
        	if(count == 0){
        		Robot.getGearIntake().setLEDMode();
        	}
        	count++;
    	}
    	if(count >= 25)
    	{
    		finish = true;
        	Robot.getGearIntake().setLEDMode();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return finish;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.getGearIntake().setGearIntakeMotor(0.0);
    	Robot.getGearIntake().setLEDMode();
    	//Robot.getGearIntake().gearIntakeUp();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
