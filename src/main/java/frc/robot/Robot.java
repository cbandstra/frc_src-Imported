package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

//Test

public class Robot extends TimedRobot {

    // Initialize variables
    private TalonSRX motor_1;                           // Motor controller for the robot
    private final int motorID_1 = 11;                   // CAN ID of the motor controller

    private Joystick joystick_1;                        // Joystick for controlling the robot
    private final int joystickPortNum_1 = 0;            // Port number of the joystick
    private final double driftMin = 0.008;              // Minimum joystick input to accomodate joystick drift
    private double stickInputValue;                     // Joystick input value

    private long powerPct;                              // Power percentage of the motor
    private boolean isMoving;                           // Flag to check if the motor is moving


    // This function is called when the robot is first started up
    @Override
    public void robotInit() {
        motor_1 = new TalonSRX(motorID_1);              // Initialize motor controller
        joystick_1 = new Joystick(joystickPortNum_1);   // Initialize joystick on port 0
    }


    // This function is called when the robot is in "TeleOperated" mode in the FRC Driver Station
    @Override
    public void teleopPeriodic() {

        // Read the value of the joystick's up/down position
        // Make forward stick a positive value by multiplying by -1
        stickInputValue = joystick_1.getRawAxis(1) * -1;

        // Check if the joystick input is outside the drift minimum
        // This will account for joystick drift
        if (stickInputValue > driftMin || stickInputValue < (driftMin * -1)) {

            // Set motor output based on joystick input
            // Do this first to reduce lag
            motor_1.set(ControlMode.PercentOutput, stickInputValue);

            // If motor was not already moving, log it and set the flag to true
            if (!isMoving) {
                System.out.println("Motor started.");
                isMoving = true;
            }

            // Calculate new power percentage to be displayed in the logs
            long newPowerPct = Math.round(stickInputValue * 100);

            // If the power percentage has changed, print the new value
            if (newPowerPct != powerPct) {
                System.out.println("Power at: " + newPowerPct + "%");
                powerPct = newPowerPct;
            }

        }

        // If the joystick input was inside the drift minimum and the motor is moving, 
        // stop the motor
        else if (isMoving) {

            // Set motor output to 0
            // Do this first to reduce lag
            motor_1.set(ControlMode.PercentOutput, 0);

            // Print that the motor has stopped and set the flag to false
            System.out.println("Motor stopped.");
            isMoving = false;
        }
    }
}
