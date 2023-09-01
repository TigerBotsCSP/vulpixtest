package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
  @Override
  public void robotInit() {
    // ? Init SmartDashboard
    RobotContainer.init();
  }

  @Override
  public void robotPeriodic() {
    // ? Update SmartDashboard
    RobotContainer.loop();
  }

  @Override
  public void autonomousPeriodic() {
    // ? When running paths, don't forget this
    RobotContainer.m_drive.updateOdometry();
  }

  @Override
  public void teleopPeriodic() {
    RobotContainer.m_drive.swerve(RobotContainer.m_controller.getLeftY(), RobotContainer.m_controller.getLeftX(),
        RobotContainer.m_controller.getRightX());
  }
}
