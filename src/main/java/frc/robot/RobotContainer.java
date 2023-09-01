package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Swerve.Drive;
import edu.wpi.first.wpilibj.I2C;

public class RobotContainer {
    // Core
    public static AHRS m_gyro = new AHRS(I2C.Port.kMXP);
    public static Drive m_drive = new Drive();
    public static NyaDS m_nyads = new NyaDS();

    // Controllers
    public static XboxController m_controller = new XboxController(0);

    // Constants
    public static final double m_driveSpeed = 3;
    public static final double m_rotationSpeed = Math.PI;

    // Swerve Variables
    // ! = should change it
    public static double kWheelRadius = 0.0381; // 1.5 inch radius wheels
    public static int kEncoderResolution = 4096; // !
    public static double kModuleMaxAngularVelocity = 0.5; // !
    public static double kModuleMaxAngularAcceleration = 1.0; // !
    public static double driveP = 0.1; // !
    public static double driveI = 0.0; // !
    public static double driveD = 0.01; // !
    public static double turningP = 0.1; // !
    public static double turningI = 0.0; // !
    public static double turningD = 0.01; // !
    public static double driveKS = 1.0; // !
    public static double driveKV = 0.05; // !
    public static double turnKS = 0.5; // !
    public static double turnKV = 0.02; // !
    public static double translation = 0; // ! For Translation2D, see Drive.java

    public static void init() {
        // Initialize SmartDashboard with initial values
        SmartDashboard.putNumber("Wheel Radius", kWheelRadius);
        SmartDashboard.putNumber("Encoder Resolution", kEncoderResolution);
        SmartDashboard.putNumber("Max Angular Velocity", kModuleMaxAngularVelocity);
        SmartDashboard.putNumber("Max Angular Acceleration", kModuleMaxAngularAcceleration);
        SmartDashboard.putNumber("Drive P", driveP);
        SmartDashboard.putNumber("Drive I", driveI);
        SmartDashboard.putNumber("Drive D", driveD);
        SmartDashboard.putNumber("Turning P", turningP);
        SmartDashboard.putNumber("Turning I", turningI);
        SmartDashboard.putNumber("Turning D", turningD);
        SmartDashboard.putNumber("Drive kS", driveKS);
        SmartDashboard.putNumber("Drive kV", driveKV);
        SmartDashboard.putNumber("Turn kS", turnKS);
        SmartDashboard.putNumber("Turn kV", turnKV);

        // Start NyaDS
        m_nyads.start();
    }

    public static void loop() {
        // Get updated values from SmartDashboard
        kWheelRadius = SmartDashboard.getNumber("Wheel Radius", kWheelRadius);
        kEncoderResolution = (int) SmartDashboard.getNumber("Encoder Resolution", kEncoderResolution);
        kModuleMaxAngularVelocity = SmartDashboard.getNumber("Max Angular Velocity", kModuleMaxAngularVelocity);
        kModuleMaxAngularAcceleration = SmartDashboard.getNumber("Max Angular Acceleration",
                kModuleMaxAngularAcceleration);
        driveP = SmartDashboard.getNumber("Drive P", driveP);
        driveI = SmartDashboard.getNumber("Drive I", driveI);
        driveD = SmartDashboard.getNumber("Drive D", driveD);
        turningP = SmartDashboard.getNumber("Turning P", turningP);
        turningI = SmartDashboard.getNumber("Turning I", turningI);
        turningD = SmartDashboard.getNumber("Turning D", turningD);
        driveKS = SmartDashboard.getNumber("Drive kS", driveKS);
        driveKV = SmartDashboard.getNumber("Drive kV", driveKV);
        turnKS = SmartDashboard.getNumber("Turn kS", turnKS);
        turnKV = SmartDashboard.getNumber("Turn kV", turnKV);

        // Put the updated values back on SmartDashboard
        SmartDashboard.putNumber("Wheel Radius", kWheelRadius);
        SmartDashboard.putNumber("Encoder Resolution", kEncoderResolution);
        SmartDashboard.putNumber("Max Angular Velocity", kModuleMaxAngularVelocity);
        SmartDashboard.putNumber("Max Angular Acceleration", kModuleMaxAngularAcceleration);
        SmartDashboard.putNumber("Drive P", driveP);
        SmartDashboard.putNumber("Drive I", driveI);
        SmartDashboard.putNumber("Drive D", driveD);
        SmartDashboard.putNumber("Turning P", turningP);
        SmartDashboard.putNumber("Turning I", turningI);
        SmartDashboard.putNumber("Turning D", turningD);
        SmartDashboard.putNumber("Drive kS", driveKS);
        SmartDashboard.putNumber("Drive kV", driveKV);
        SmartDashboard.putNumber("Turn kS", turnKS);
        SmartDashboard.putNumber("Turn kV", turnKV);

        // ? Refresh swerve modules' variables
        RobotContainer.m_drive.m_frontLeft.loop();
        RobotContainer.m_drive.m_frontRight.loop();
        RobotContainer.m_drive.m_backLeft.loop();
        RobotContainer.m_drive.m_backRight.loop();
    }
}
