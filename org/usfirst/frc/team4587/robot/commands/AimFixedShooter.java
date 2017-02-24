package org.usfirst.frc.team4587.robot.commands;

import org.usfirst.frc.team4587.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import utility.Gyro;

/**
 *
 */
public class AimFixedShooter extends Command {
	private double m_centerline;
	private double m_lastCenterline;
	double m_startAngle;
	double m_desiredAngle;
	PIDController turnControl;
	myPIDSource m_myPIDSource;
	myPIDOutput m_myPIDOutput;
	int count;

    public AimFixedShooter() {
    	
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	count = 0;
    	requires(Robot.getDriveBaseSimple());
    	m_myPIDSource = new myPIDSource();
    	m_myPIDOutput = new myPIDOutput();
    	turnControl = new PIDController(0.03, 0.0, 0.0, m_myPIDSource, m_myPIDOutput);
    	turnControl.setAbsoluteTolerance(1);
    	turnControl.setInputRange(-180, 180);
    	turnControl.setContinuous(true);
    }

    class myPIDOutput implements PIDOutput{

		@Override
		public void pidWrite(double output) {
			// TODO Auto-generated method stub
			Robot.getDriveBaseSimple().setLeftMotor(output);
			Robot.getDriveBaseSimple().setRightMotor(output*-1);
		}
    	
    }
    void setSetpoint(double setpoint)
    {
    	while (setpoint < -180)
    	{
    		setpoint += 360;
    	}
    	while (setpoint > 180)
    	{
    		setpoint -= 360;
    	}
    	turnControl.setSetpoint(setpoint);
    }
    
    class myPIDSource implements PIDSource{

		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public PIDSourceType getPIDSourceType() {
			// TODO Auto-generated method stub
			return PIDSourceType.kDisplacement;
		}

		@Override
		public double pidGet() {
			// TODO Auto-generated method stub
			return Gyro.getYaw();
		}
    	
    }
    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("hello");
    	m_lastCenterline = -5000;
    	turnControl.reset();
    	turnControl.setSetpoint(Gyro.getYaw());;
    	turnControl.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    		System.out.println("Execute");
	    	m_centerline = SmartDashboard.getNumber("pi.centerline", -1);
	    	if (m_centerline >= 0)
	    	{
	    		System.out.println("centerline >= 0");
	    		if(Math.abs(m_centerline - 320) > 20)
	    		{
	    			Robot.writeToArduino((byte)65);
	    			SmartDashboard.putNumber("byte sent to arduino", 65);
	    			SmartDashboard.putNumber("last centerline", m_lastCenterline);
	    			if(m_lastCenterline != m_centerline)
	    			{
		    			double error = (m_centerline - 320) / 16.0;
		    			//turnControl.setSetpoint(turnControl.getSetpoint() + error);
		    			setSetpoint(turnControl.getSetpoint() + error);
		    			//setSetpoint(error);
		    			//Robot.getTurret().setSetpoint(120);
		    			m_lastCenterline = m_centerline;
		    			SmartDashboard.putNumber("Desired Setpoint", turnControl.getSetpoint());
	    			}
	    		}
	    		else
	    		{
	    			Robot.writeToArduino((byte)64);
	    			SmartDashboard.putNumber("byte sent to arduino", 64);
	    		}
	    	}
	    	else
	    	{
	    		System.out.println("centerline < 0");
	    		//turnControl.setSetpoint(-135);
	    		setSetpoint(-135);
	    	}
	    	System.out.println("Setpoint: " + turnControl.getSetpoint());
    	}

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	turnControl.disable();
    	//Robot.getDriveBaseSimple().arcadeDrive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
