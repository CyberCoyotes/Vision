package org.usfirst.frc.team3603.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	
	int f_min = 37;
	int f_max = 43;
	double f_speed = 0.2;
	
	Vision vision = new Vision(); //The vision processor object
	
	//Drive stuff
	Victor backLeft = new Victor(1);
	Victor backRight = new Victor(2);
	Victor frontLeft = new Victor(3);
	Victor frontRight = new Victor(4);
	RobotDrive mainDrive = new RobotDrive(frontLeft, backLeft, frontRight, backRight);
	
	Joystick joy1 = new Joystick(0); //Controller
	
	@Override
	public void robotInit() {
		vision = new Vision(); //Begin the vision processor
		frontRight.setInverted(true); //Invert motor
		backRight.setInverted(true); //Invert motor
		
		mainDrive.setSafetyEnabled(false);
	}
	@Override
	public void autonomousInit() {
	}
	@Override
	public void autonomousPeriodic() {
	}
	@Override
	public void teleopPeriodic() {
		if(!vision.isWorking()) { //Test to see if this works for checking if it's working
			vision.retry(); //Restart the vision
		}
		
		//Testing to see the minimum turn speed
		if(Math.abs(joy1.getRawAxis(0)) >= 0.5 || Math.abs(joy1.getRawAxis(1)) >= 0.5 || Math.abs(joy1.getRawAxis(4)) >= 0.5) {
			mainDrive.mecanumDrive_Cartesian(joy1.getRawAxis(0), joy1.getRawAxis(1), joy1.getRawAxis(4), 0);
		} else if(joy1.getRawButton(1) && vision.getRotationSpeed() != -5) {
			mainDrive.mecanumDrive_Cartesian(0, vision.getForwardSpeed(f_min, f_max, f_speed), vision.getRotationSpeed(), 0);
		} else {
			mainDrive.mecanumDrive_Cartesian(0, 0, 0, 0);
		}
		
		read();
	}
	
	void read() {
		SmartDashboard.putString("Keys", vision.getKeys()); //Publish the NetworkTables keys
		SmartDashboard.putBoolean("Working", vision.isWorking());
		SmartDashboard.putNumber("Vision", vision.getX()); //Publish the center X
		SmartDashboard.putNumber("Height", vision.getHeight());
		SmartDashboard.putNumber("Distance", vision.getDistance());
		SmartDashboard.putNumber("Rotation Speed", vision.getRotationSpeed()); //Publish the turn speed
		SmartDashboard.putNumber("Forward Speed", vision.getForwardSpeed(f_min, f_max, f_speed));
	}
	
	@Override
	public void testPeriodic() {
	}
}