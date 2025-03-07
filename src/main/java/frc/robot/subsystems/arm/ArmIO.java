package frc.robot.subsystems.arm;

import org.littletonrobotics.junction.AutoLog;

public interface ArmIO {
  @AutoLog
  class ArmIOInputs {
    public double armAxisAngle = 0.0;
    public double armAppliedVolts = 0.0;
    public double armAppliedCurrentAmps = 0.0;
    public double armVelocity = 0.0;
    public double targetPosition = 0.0;
  }

  default void updateInputs(ArmIOInputs inputs) {}

  default void setArmOpenLoopVoltage(double output) {}

  default void setArmOpenLoop(double output) {}

  default void setArmAngle(double angle) {}

  default double getArmAngleRotations() {
    return 0;
  }

  default double getArmVelocity() {
    return 0;
  }

  default void stopArm() {}

  default void holdArm() {}
}
