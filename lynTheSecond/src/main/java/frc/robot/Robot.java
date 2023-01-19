// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public Robot()
  {
    
  }
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  WPI_TalonSRX leftFront = new WPI_TalonSRX(0);
  WPI_TalonSRX leftFollower = new WPI_TalonSRX(2);
  WPI_TalonSRX rightFront = new WPI_TalonSRX(1);
  WPI_TalonSRX rightFollower = new WPI_TalonSRX(3);
  
  double drivePower = 0.6;

  XboxController xBoxDrive = new XboxController(0);
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    leftFollower.follow(leftFront);
    rightFollower.follow(rightFront);
  
    rightFront.setInverted(true);
    rightFollower.setInverted(true);
    leftFront.setInverted(false);
    leftFollower.setInverted(false);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() 
  {
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() 
  {
    if (Math.abs(xBoxDrive.getLeftX()) > 0.06 || Math.abs(xBoxDrive.getLeftY()) > 0.06) 
    {
      if (Math.abs(xBoxDrive.getLeftX()) > 0.06)
      {
        if (Math.abs(xBoxDrive.getLeftY()) > 0.06)
        {
          rightFront.set(drivePower * ((xBoxDrive.getLeftY()-xBoxDrive.getLeftX()/2)));
          leftFront.set(drivePower * ((xBoxDrive.getLeftY()+xBoxDrive.getLeftX()/2)));
        }
        else
        {
          rightFront.set(drivePower * (-xBoxDrive.getLeftX()/2));
          leftFront.set(drivePower * (xBoxDrive.getLeftX()/2));
        }
      }
      else if (Math.abs(xBoxDrive.getLeftY()) > 0.06)
      {
        leftFront.set(drivePower * xBoxDrive.getLeftY());
        rightFront.set(drivePower * xBoxDrive.getLeftY());
      }
    }
    else
    {
      rightFront.set(0);
      leftFront.set(0);
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() 
  { 
    if (xBoxDrive.getXButton() == true) 
    {
      leftFront.set(1);
      rightFront.set(0);
    } else if (xBoxDrive.getBButton() == true) 
    {
      leftFront.set(1);
      rightFront.set(1);
    } else
    {
      leftFront.set(0);
      rightFront.set(0);
    } 
  }
  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
