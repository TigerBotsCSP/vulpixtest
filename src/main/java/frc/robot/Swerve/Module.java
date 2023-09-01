// ! Akram

package frc.robot.Swerve;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.RobotContainer;

public class Module {
        // ! Change these values! Ensure they're accurate
        private static final double kWheelRadius = RobotContainer.kWheelRadius;
        private static final int kEncoderResolution = RobotContainer.kEncoderResolution;

        private static final double kModuleMaxAngularVelocity = RobotContainer.m_rotationSpeed;
        private static final double kModuleMaxAngularAcceleration = 2 * RobotContainer.m_rotationSpeed;

        // ? The NEO motors have built-in encoders
        private final CANSparkMax m_driveMotor;
        private final CANSparkMax m_turningMotor;

        // ! Gain values need to be thoroughly tested, play around with values
        public PIDController m_drivePIDController = new PIDController(RobotContainer.driveP,
                        RobotContainer.driveI, RobotContainer.driveD);

        // ! Gain values need to be thoroughly tested, play around with values
        public ProfiledPIDController m_turningPIDController = new ProfiledPIDController(RobotContainer.turningP,
                        RobotContainer.turningI, RobotContainer.turningD,
                        new TrapezoidProfile.Constraints(
                                        kModuleMaxAngularVelocity, kModuleMaxAngularAcceleration));

        // ! Gain values need to be thoroughly tested, play around with values
        public SimpleMotorFeedforward m_driveFeedforward = new SimpleMotorFeedforward(RobotContainer.driveKS,
                        RobotContainer.driveKV);
        public SimpleMotorFeedforward m_turnFeedforward = new SimpleMotorFeedforward(RobotContainer.turnKS,
                        RobotContainer.turnKV);

        public Module(int driveMotor, int turningMotor) {
                m_driveMotor = new CANSparkMax(driveMotor, MotorType.kBrushless);
                m_turningMotor = new CANSparkMax(turningMotor, MotorType.kBrushless);

                // ? Sets the distance per pulse for the drive encoder
                m_driveMotor.getEncoder().setPositionConversionFactor(2 * Math.PI * kWheelRadius / kEncoderResolution);

                // ? Set the distance in radians per pulse for the turning encoder
                m_turningMotor.getEncoder().setPositionConversionFactor(2 * Math.PI / kEncoderResolution);

                // ? Limit turnings by the maximum turning speed
                m_turningPIDController.enableContinuousInput(-RobotContainer.m_rotationSpeed,
                                RobotContainer.m_rotationSpeed);
        }

        // ? Swerve testing: run this for every module to update variables with new
        // ? values that are from SmartDasboard
        public void loop() {
                // ! Gain values need to be thoroughly tested, play around with values
                m_drivePIDController = new PIDController(RobotContainer.driveP,
                                RobotContainer.driveI, RobotContainer.driveD);

                // ! Gain values need to be thoroughly tested, play around with values
                m_turningPIDController = new ProfiledPIDController(RobotContainer.turningP,
                                RobotContainer.turningI, RobotContainer.turningD,
                                new TrapezoidProfile.Constraints(
                                                kModuleMaxAngularVelocity, kModuleMaxAngularAcceleration));

                // ! Gain values need to be thoroughly tested, play around with values
                m_driveFeedforward = new SimpleMotorFeedforward(RobotContainer.driveKS,
                                RobotContainer.driveKV);
                m_turnFeedforward = new SimpleMotorFeedforward(RobotContainer.turnKS,
                                RobotContainer.turnKV);
        }

        // ? Sets a new state for the swerve module
        public void set(SwerveModuleState newState) {
                // ? Prevents spinning further than 90 degrees
                SwerveModuleState state = SwerveModuleState.optimize(newState,
                                new Rotation2d(m_turningMotor.getEncoder().getPosition()));

                // ? Calculates the drive output from the drive PID controller
                final double driveOutput = m_drivePIDController.calculate(m_driveMotor.getEncoder().getVelocity(),
                                state.speedMetersPerSecond);
                final double driveFeedforward = m_driveFeedforward.calculate(state.speedMetersPerSecond);

                // ? Calculate the turning motor output from the turning PID controller
                final double turnOutput = m_turningPIDController.calculate(m_turningMotor.getEncoder().getPosition(),
                                state.angle.getRadians());
                final double turnFeedforward = m_turnFeedforward
                                .calculate(m_turningPIDController.getSetpoint().velocity);

                // * Set the speeds!
                m_driveMotor.setVoltage(driveOutput + driveFeedforward);
                m_turningMotor.setVoltage(turnOutput + turnFeedforward);
        }

        // ? Get module's current position
        public SwerveModulePosition getPosition() {
                return new SwerveModulePosition(
                                m_driveMotor.getEncoder().getPosition(),
                                new Rotation2d(m_turningMotor.getEncoder().getPosition()));
        }
}
