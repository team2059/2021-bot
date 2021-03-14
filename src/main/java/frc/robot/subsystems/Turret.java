package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax;
import frc.robot.Constants.TurretConstants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.Optional;

public class Turret extends SubsystemBase {
    private TalonSRX driver;

    private DigitalInput leftHFX;
    private boolean isAllLeft;

    private DigitalInput rightHFX;
    private boolean isAllRight;

    private enum TurretControlState{
        DISABLED (5),
        AUTO (4),
        MANUAL (3),
        CALIBRATION (2),
        HOLD (1);

        private final int stateCode;
        TurretControlState (int stateCode){
            this.stateCode = stateCode;
        }
    }
    private TurretControlState currentState;

    private Optional<Integer> TURRET_LEFT_POSITION = Optional.empty();
    private Optional<Integer> TURRET_RIGHT_POSITION = Optional.empty();

    private Optional<Integer> TURRET_TARGET_POSITION = Optional.empty();

    public void setTurretState(String state){
        switch(state){
            case("HOLD"):
                this.currentState = TurretControlState.HOLD;
                break;
            case("CALIBRATION"):
                this.currentState = TurretControlState.CALIBRATION;
                break;
            case("MANUAL"):
                this.currentState = TurretControlState.MANUAL;
                break;
            case("AUTO"):
                this.currentState = TurretControlState.AUTO;
                break;
            case("DISABLED"):
                this.currentState = TurretControlState.DISABLED;
                break;
            default:
                System.out.println("COULD NOT ASSIGN STATE - DEFAULTING TO HOLD");
                this.currentState = TurretControlState.HOLD;
        }
    }
    public void setTurretState(int state){
        switch(state){
            case(1):
                this.currentState = TurretControlState.CALIBRATION;
                break;
            case(2):
                this.currentState = TurretControlState.DISABLED;
                break;
            case(3):
                this.currentState = TurretControlState.AUTO;
                break;
            case(4):
                this.currentState = TurretControlState.MANUAL;
                break;
            case(5):
                this.currentState = TurretControlState.HOLD;
                break;
            default:
                System.out.println("COULD NOT ASSIGN STATE - DEFAULTING TO HOLD");
                this.currentState = TurretControlState.HOLD;
        }
    }
    public Turret() {
    }
    public void update(){
        // System.out.println(currentState);
        switch(currentState){
            case CALIBRATION:
                TURRET_LEFT_POSITION = Optional.of(getTurretPosition());
                System.out.println("Beginning Auto Calibration, please clear area surrounding elevator");

                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    System.out.print("Could not interrupt thread " + e);
                }

                while (getTopHFX()){
                    MainMotor.set(.25);
                }

                ELEVATOR_TOP_POSITION = Optional.of(getElevatorPosition());
                System.out.println("Auto Calibration Complete");
                System.out.printf("Top Value: %d \n Bottom Value %d", ELEVATOR_TOP_POSITION.get(), ELEVATOR_BOTTOM_POSITION.get());
                break;
            case DISABLED:
                // TODO: motion magic timing
                break;
            case AUTO: break; // Do nothing
            case MANUAL: break;
            case HOLD:
                if (!getBottomHFX()) {
                    MainMotor.set(0);
                } else {
                    if (ELEVATOR_TARGET_POSITION.isPresent()) {
                        MainMotor.set(ControlMode.Position, ELEVATOR_TARGET_POSITION.get());
                    } else {
                        int currentLoc = getElevatorPosition();
                        MainMotor.set(ControlMode.Position, currentLoc);
                    }
                }
                break;
            default: break; // Do nothing
        }
    }
//         public WPI_TalonSRX getMainMotor() {
//         return MainMotor;
//     }

//     public Boolean getTopHFX() {
//         return topHFX.get();
//     }

//     public Boolean getBottomHFX() {
//         return bottomHFX.get();
//     }

    public int getTurretPosition() {
        try {
            return driver.getSelectedSensorPosition(0);
        } catch (NullPointerException e) {
            return -1;
        }
    }

//     public ElevatorControlState getState() {
//         return currentState;
//     }

//     public void Calibrate() {
//         if(getBottomHFX()) {
//             ELEVATOR_BOTTOM_POSITION = Optional.of(getElevatorPosition());
//             if(!ELEVATOR_TOP_POSITION.isPresent()) {
//                 ELEVATOR_TOP_POSITION = Optional.of(getElevatorPosition() + RobotMap.elevatorHeight * RobotMap.elevatorTicksPerInch);
//             }
//         } else if(getTopHFX()) {
//             ELEVATOR_TOP_POSITION = Optional.of(getElevatorPosition());
//             if(!ELEVATOR_BOTTOM_POSITION.isPresent()) {
//                 ELEVATOR_BOTTOM_POSITION = Optional.of(getElevatorPosition() - RobotMap.elevatorHeight * RobotMap.elevatorTicksPerInch);
//             }
//         }
//     }

//     public void setBottomPosition(Optional<Integer> value) {
//         ELEVATOR_BOTTOM_POSITION = value;
//     }

//     public Optional<Integer> getBottomPosition() {
//         return ELEVATOR_BOTTOM_POSITION;
//     }

//     public void setTopPosition(Optional<Integer> value) {
//         ELEVATOR_TOP_POSITION = value;
//     }

//     public Optional<Integer> getTopPosition() {
//         return ELEVATOR_TOP_POSITION;
//     }

//     public void setELEVATOR_TARGET_POSITION(Optional<Integer> value) {
//         ELEVATOR_TARGET_POSITION = value;
//     }

//     public Optional<Integer> getELEVATOR_TARGET_POSITION() {
//         return ELEVATOR_TARGET_POSITION;
//     }

//     public void setElevatorMotor(double power) {
//         if (!getBottomHFX() && power > 0) {
//             MainMotor.set(power);
//         }
//         else if (!getBottomHFX() && power <= 0) {
//             MainMotor.set(0);
//         }
//         else if (!getTopHFX() && power < 0) {
//             MainMotor.set(power);
//         }
//         else if (!getTopHFX() && power >= 0) {
//             MainMotor.set(0);
//         }
//         else{
//             MainMotor.set(power);
//         }
//     }

//     public Boolean isCalibrated() {
//         return ELEVATOR_BOTTOM_POSITION.isPresent() && ELEVATOR_TOP_POSITION.isPresent();
//     }

//     @Override
//     public void initDefaultCommand() {
//         setDefaultCommand(new Elevate());
//     }

//     public void MagicSetMotor(double setpoint){
//         MainMotor.set(ControlMode.MotionMagic, setpoint);
//     }
}
