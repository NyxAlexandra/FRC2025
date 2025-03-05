package frc.robot.subsystems.arm;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.Constants;
import frc.robot.Constants.ArmConstants;
import java.util.function.DoubleSupplier;
import org.littletonrobotics.junction.Logger;

public class Arm extends SubsystemBase {
    private final ArmIO io;
    private final ArmIOInputsAutoLogged inputs = new ArmIOInputsAutoLogged();
    private final Alert armAxisDisconnectedAlert;
    private final Alert armFlywheelDisconnectedAlert;
    private final SysIdRoutine sysId;

    public Arm(ArmIO io) {
        this.io = io;

        armAxisDisconnectedAlert = new Alert("Disconnected arm axis motor", Alert.AlertType.kError);
        armFlywheelDisconnectedAlert = new Alert("Disconnected arm flywheel motor", Alert.AlertType.kError);

        sysId = new SysIdRoutine(
                new SysIdRoutine.Config(
                        null, null, null, (state) -> Logger.recordOutput("Arm/SysIDState", state.toString())),
                new SysIdRoutine.Mechanism((voltage -> runCharacterization(voltage.in(Units.Volts))), null, this));
    }

    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Arm", inputs);

        if (DriverStation.isDisabled()) {
            io.stopArm();
        }
    }

    public void setArmFlywheelOpenLoop(double output) {
        io.setArmOpenLoopVoltage(output);
    }

    public void setArmAxisAngleDegrees(double angle) {
        io.setArmAngle(angle);
    }

    public double getArmAxisAngle() {
        return io.getArmAngleRotations();
    }


    private void runCharacterization(double output) {
        io.setArmOpenLoopVoltage(output);
    }

    // TODO: SysID routines
    public Command sysIdDynamic(Direction direction) {
        return sysId.dynamic(direction);
    }
    public Command sysIdQuasistatic(Direction direction) {
        return sysId.quasistatic(direction);
    }

    // TODO: Command Factories?

    public Command moveArm(DoubleSupplier output) {
        return run(() -> io.setArmOpenLoop(output.getAsDouble() * 0.1));
    }

    public Command moveArmToAngle(Double angle) {
        return run(() -> io.setArmAngle(angle)).until(() -> Math.abs(inputs.armAxisAngle - angle) < 1.0);
    }

}
