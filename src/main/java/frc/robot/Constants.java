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
    public static final class TurretConstants {
        public static final int TurretRotatorTalonPort = 20;
        public static final int LeftLimitHallEffectPort = 0;
        public static final int RightLimitHallEffectPort = 2;

        public static final int LeftLimit = -2170;
        public static final int RightLimit = 35165;

        public static final double RotatekP = .075;
        public static final double RotatekI = 0.0;
        public static final double RotatekD = 0;

        public static final double EncoderRotatekP = .001;
        public static final double EncoderRotatekI = 0.0;
        public static final double EncoderRotatekD = 0.0;

        public static final double offsetTolerance = .5;
        public static final double offsetVelocityTolerance = 0;
        
        public static final String shootAuto = "Auto";
        public static final String shootMan = "Manual";
        public static final String ShootCal = "Calabaration";
        public static final String shootHold = "Hold";

    }
}
