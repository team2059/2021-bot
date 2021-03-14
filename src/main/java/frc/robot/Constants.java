// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final class ShooterConstants {
        public static final int ShooterFlywheelTalonPort = 24;
        public static final int HoodMotorSparkMaxPort = 17;
        public static final int HoodPotPort = 0;

        public static final double FlyWheelkF = 0;
        public static final double FlyWheelkP = .025;
        public static final double FlyWheelkI = 0.00009;
        public static final double FlyWheelkD = 0.4;

        public static final int LoopIdx = 0;
        public static final int MotorSlotIdx = 0;

        public static final int CtreTimeoutMs = 30;

        // PID coefficients
        public static final double HoodkP = .1;
        public static final double HoodkI = .001;
        public static final double HoodkD = .001;
        public static final double HoodkIz = 0;
        public static final double HoodkFF = 0;
        public static final double HoodkMaxOutput = .5;
        public static final double HoodkMinOutput = -.5;

        // Hood encoded setpoints
        public static final double zeroHoodPosition = 0;
        public static final double lowGoalHoodPosition = 10;

        // Smart Motion Coefficients
        public static final double HoodMaxVel = 750; // rpm
        public static final double HoodMinVel = 0;
        public static final double HoodMaxAcc = 500;

        public static final double HoodAllowedErr = .05;
    }
}
