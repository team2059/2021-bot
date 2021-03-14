package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.*;
import edu.wpi.first.wpilibj.AnalogInput;
import frc.robot.Constants.ShooterConstants;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.Optional;

public class Shooter extends SubsystemBase {
    WPI_TalonSRX FlywheelShooter = new WPI_TalonSRX(ShooterConstants.ShooterFlywheelTalonPort);

    CANSparkMax HoodMotor = new CANSparkMax(ShooterConstants.HoodMotorSparkMaxPort,
            CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANPIDController hoodPIDController;
    private CANEncoder hoodEncoder;
    private int smartMotionSlot = 1;

    AnalogInput hoodPot = new AnalogInput(ShooterConstants.HoodPotPort);

    private enum ShooterControlState {
        AUTO(4), MANUAL(3), CALIBRATION(2), HOLD(1);

        private final int stateCode;

        ShooterControlState(int stateCode) {
            this.stateCode = stateCode;
        }
    }

    private ShooterControlState currentState;
    private Optional<Integer> TARGET_VELOCITY = Optional.empty();
    private Optional<Double> TARGET_POSITION = Optional.empty();
    public void setShooterState(String state) {
        switch (state) {
        case ("AUTO"):
            this.currentState = ShooterControlState.AUTO;
            break;

        case ("MANUAL"):
            this.currentState = ShooterControlState.MANUAL;
            break;
        case ("CALIBRATION"):
            this.currentState = ShooterControlState.CALIBRATION;
            break;
        case ("HOLD"):
            this.currentState = ShooterControlState.HOLD;
            break;
        default:
            System.out.println("COULD NOT ASSIGN STATE - DEFAULTING TO HOLD");
            this.currentState = ShooterControlState.HOLD;
            break;
        }
    }

    public void setShooterState(int state) {
        switch (state) {
        case (1):
            this.currentState = ShooterControlState.HOLD;
            break;
        case (2):
            this.currentState = ShooterControlState.CALIBRATION;
            break;
        case (3):
            this.currentState = ShooterControlState.MANUAL;
            break;
        case (4):
            this.currentState = ShooterControlState.AUTO;
            break;
        default:
            System.out.println("COULD NOT ASSIGN STATE - DEFAULTING TO HOLD");
            this.currentState = ShooterControlState.HOLD;
            break;

        }
    }

    public Shooter() {
        // Configure FlyWheel Talon SRX
        FlywheelShooter.configFactoryDefault();
        FlywheelShooter.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, ShooterConstants.LoopIdx,
                ShooterConstants.CtreTimeoutMs);
        FlywheelShooter.setSensorPhase(true);

        FlywheelShooter.configNominalOutputForward(0, ShooterConstants.CtreTimeoutMs);
        FlywheelShooter.configNominalOutputReverse(0, ShooterConstants.CtreTimeoutMs);
        FlywheelShooter.configPeakOutputForward(1, ShooterConstants.CtreTimeoutMs);

        FlywheelShooter.configPeakOutputReverse(-1, ShooterConstants.CtreTimeoutMs);

        FlywheelShooter.selectProfileSlot(ShooterConstants.MotorSlotIdx, ShooterConstants.LoopIdx);
        FlywheelShooter.config_kF(ShooterConstants.MotorSlotIdx, ShooterConstants.FlyWheelkF,
                ShooterConstants.CtreTimeoutMs);
        FlywheelShooter.config_kP(ShooterConstants.MotorSlotIdx, ShooterConstants.FlyWheelkP,
                ShooterConstants.CtreTimeoutMs);
        FlywheelShooter.config_kI(ShooterConstants.MotorSlotIdx, ShooterConstants.FlyWheelkI,
                ShooterConstants.CtreTimeoutMs);
        FlywheelShooter.config_kD(ShooterConstants.MotorSlotIdx, ShooterConstants.FlyWheelkD,
                ShooterConstants.CtreTimeoutMs);

        // Configure Hood Spark Max
        HoodMotor.setInverted(true);
        hoodPIDController = HoodMotor.getPIDController();
        hoodEncoder = HoodMotor.getEncoder();

        hoodPIDController.setP(ShooterConstants.HoodkP);
        hoodPIDController.setI(ShooterConstants.HoodkI);
        hoodPIDController.setD(ShooterConstants.HoodkD);
        hoodPIDController.setIZone(ShooterConstants.HoodkIz);
        hoodPIDController.setFF(ShooterConstants.HoodkFF);
        hoodPIDController.setOutputRange(ShooterConstants.HoodkMinOutput, ShooterConstants.HoodkMaxOutput);

        hoodPIDController.setSmartMotionMinOutputVelocity(ShooterConstants.HoodMinVel, smartMotionSlot);
        hoodPIDController.setSmartMotionMaxAccel(ShooterConstants.HoodMaxAcc, smartMotionSlot);
        hoodPIDController.setSmartMotionAllowedClosedLoopError(ShooterConstants.HoodAllowedErr, smartMotionSlot);
    }

    public void update() {
        switch (currentState) {
        case AUTO:
            setWheelVelocity(TARGET_VELOCITY.get());
            setHoodPosition(TARGET_POSITION.get());
            break;
        case MANUAL:
            Optional.empty();
            break;
        case HOLD:
            break;
        case CALIBRATION:
            break;
        }
    }

    public void mapPotVoltageToNeoEncoder() {
        if (Math.abs(getHoodPotVoltage() - .7739) < .1) {
            HoodMotor.getEncoder().setPosition(0);
        }

        if (Math.abs(getHoodPotVoltage() - 2.546) < .1) {
            HoodMotor.getEncoder().setPosition(18.142);
        }
    }

    public double getHoodPotVoltage() {
        return hoodPot.getVoltage();
    }

    public double getFlyWheelVelocity() {
        return FlywheelShooter.getSelectedSensorVelocity();
    }

    public void setHoodPosition(double position) {
        System.out.println("Setting Hood to " + position);
        // hoodPIDController.setReference(position, ControlType.kSmartMotion);
        HoodMotor.getPIDController().setReference(position, ControlType.kSmartMotion);
    }

    public double getHoodPosition() {
        return hoodEncoder.getPosition();
    }

    public void setHoodMotor(double speed) {
        HoodMotor.set(speed);
    }

    public double getWheelSpeed() {
        return FlywheelShooter.getSelectedSensorVelocity();
    }

    /*
     * public void setWheelVelocity() { //double velocity =
     * Math.abs(RobotContainer.driveJS.getRawAxis(3) * 25000);
     * System.out.println("Setting " + velocity + " ticks velocity");
     * FlywheelShooter.set(ControlMode.Velocity, velocity); }
     */

    public void setWheelVelocity(int speed) {
        FlywheelShooter.set(ControlMode.Velocity, speed);
    }

    public void setFlywheelMotor(double speed) {
        FlywheelShooter.set(ControlMode.PercentOutput, speed);
    }
    public void setTargetVelocity(Optional<Integer> value){
        TARGET_VELOCITY = value;
    }
    public Optional<Integer> getTargetVelocity (){
        return TARGET_VELOCITY;
    }
    public void setTargetPosition(Optional<Double> value){
        TARGET_POSITION = value;
    }
    public Optional<Double> getTargetPosition(){
        return TARGET_POSITION;
    }
}
