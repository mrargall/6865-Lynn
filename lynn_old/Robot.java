
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
/* test
*/
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public Robot() {
    LiveWindow.disableAllTelemetry();
  }

  // Talon Controls

 WPI_TalonSRX polocord = new WPI_TalonSRX(1);
  WPI_TalonSRX intake = new WPI_TalonSRX(3);

   WPI_TalonSRX shoot = new WPI_TalonSRX(2);
   Spark Climb1 = new Spark(8);
   Spark Climb2 = new Spark(9);
  /*
   * private Talon leftMotor = new Talon(7); private Talon rightMotor = new
   * Talon(6);
   */
  // Joystick Controls

  private final Joystick bigJ = new Joystick(1);
  private final XboxController xBox = new XboxController(0);
  // Timer
  private final Timer m_timer = new Timer();


  // Constraints for the Joystick

  private final double deadZone = 0.05;

  // Diffrential Drive

  WPI_TalonSRX _rghtFront = new WPI_TalonSRX(4);
  WPI_TalonSRX _rghtFollower = new WPI_TalonSRX(6);
  WPI_TalonSRX _leftFront = new WPI_TalonSRX(5);
  WPI_TalonSRX _leftFollower = new WPI_TalonSRX(7);

  DifferentialDrive _diffDrive = new DifferentialDrive(_leftFront, _rghtFront);

 
  // Safety offline
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
 /*   intake.setSafetyEnabled(true);
    shoot.setSafetyEnabled(true);
    Polocord.setSafetyEnabled(true);
    m_drive.setSafetyEnabled(true);
    // Chooser Code
*/
    m_chooser.setDefaultOption("Backword Auto", kDefaultAuto);

    m_chooser.addOption("Foward Auto", kCustomAuto);

    

    SmartDashboard.putData("Auto choices", m_chooser);

    // Camera Server
    CameraServer.getInstance().startAutomaticCapture(0);
    CameraServer.getInstance().startAutomaticCapture(1);

    // Data for motos

    SmartDashboard.putNumber("DrivePower", 0.6);
    SmartDashboard.putNumber("ShootPower", -0.5);
    SmartDashboard.putNumber("IntakePower", .2);
    SmartDashboard.putNumber("PolyCord", 0.7);
    SmartDashboard.putNumber("AutoPower_Foward",-.34);
    SmartDashboard.putNumber("AutoPower_Backword",0.35);
    
    // Stuff for talons
    _rghtFront.configFactoryDefault();
    _rghtFollower.configFactoryDefault();
    _leftFront.configFactoryDefault();
    _leftFollower.configFactoryDefault();

    /* set up followers */
    _rghtFollower.follow(_rghtFront);
    _leftFollower.follow(_leftFront);

    /* [3] flip values so robot moves forward when stick-forward/LEDs-green */
    _rghtFront.setInverted(false); // !< Update this
    _leftFront.setInverted(true); // !< Update this

    /*
     * set the invert of the followers to match their respective master controllers
     */
    _rghtFollower.setInverted(InvertType.FollowMaster);
    _leftFollower.setInverted(InvertType.FollowMaster);

    /*
     * [4] adjust sensor phase so sensor moves positive when Talon LEDs are green
     */
    

    /*
     * WPI drivetrain classes defaultly assume left and right are opposite. call
     * this so we can apply + to both sides when moving forward. DO NOT CHANGE
     */
    _diffDrive.setRightSideInverted(false);

        /*
         * [4] adjust sensor phase so sensor moves positive when Talon LEDs are green
         */
        _rghtFront.setSensorPhase(true);
        _leftFront.setSensorPhase(true);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
     m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    m_timer.reset();
    m_timer.start();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
 
  //_diffDrive.arcadeDrive(.34,0);
if (m_timer.get() < 6){
  _diffDrive.arcadeDrive(.45, 0);

} else if(m_timer.get() > 6){
  _diffDrive.arcadeDrive(0,0);
  polocord.set(0.7);
  shoot.set(-.9);
}
  if (m_timer.get() > 9){
    polocord.set(0);
    shoot.set(0);
    _diffDrive.arcadeDrive(-.6,0);
  }


double AutoPower_Foward = SmartDashboard.getNumber("AutoPower_Foward",-0.34);
  double AutoPower_Backword = SmartDashboard.getNumber("AutoPower_Backword",.34);
 /* if(time.get() <= 6) {
  _diffDrive.arcadeDrive(.40,0);
  
  
  shoot.set(-.7);
  polocord.set(.15);
  }
*/
 /* if(time.get() <= 13) {
_diffDrive.arcadeDrive(.34, 0);
      _diffDrive.arcadeDrive(.34, 0);

      
        Thread.sleep(7000); // 10 Seconds 
        System.out.println("Starting Auto");
        _diffDrive.arcadeDrive(.34, 0); shoot.set(9); polocord.set(-4);
       

      System.out.println("GO GO GO");
  }*/
/*
  switch (m_autoSelected) {

  
  case kCustomAuto:
   
    break;
  case kDefaultAuto:
  default:/*
    _diffDrive.arcadeDrive(AutoPower_Foward, 0);
    shoot.set(.2);
      polocord.set(.2);
    
    break;
    */
}


  

  /**
   * This function is called periodically during operator control.
   * 
   * 
   */
public void teleopInit() {
  
}


  @Override
  public void teleopPeriodic() {

    // ALL THE POWER
    double drivePower = SmartDashboard.getNumber("DrivePower", 0.85);
    double intakePower = SmartDashboard.getNumber("IntakePower", 0.9);
    double shootPower = SmartDashboard.getNumber("ShootPower", -0.9);
    double polocordPower = SmartDashboard.getNumber("PolocordPower", 0.7);
     Scheduler.getInstance().run();

    // The Safety Captian should not look at this section

   
   

    if (xBox.getYButton() == true) {
      shoot.set(shootPower);
    } else {
      shoot.set(0);
    }

    if (xBox.getBButton() == true ) {
      polocord.set(polocordPower);
    } else {
      polocord.set(0);
    }

    if (xBox.getAButton() == true) {
      intake.set(intakePower);
    } else if(xBox.getXButton() == true) {
      intake.set(-.7);
      shoot.set(0.6);
      polocord.set(-0.4);
    }else {
      intake.set(0);
     
    }

    if (Math.abs(bigJ.getY()) > deadZone || Math.abs(bigJ.getX()) > deadZone) {
      _diffDrive.arcadeDrive(bigJ.getY()*drivePower,bigJ.getX()*-drivePower);

   }else{
    _diffDrive.arcadeDrive(0,0);
   }
  



  }

  

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  
}
