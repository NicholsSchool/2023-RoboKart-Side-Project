package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;

public class BowserDriver{
    //declaring variables, for tweaking use sensitivity and top speed
    public DcMotor leftMotorF , leftMotorB, rightMotorF, rightMotorB;
    public RevBlinkinLedDriver blinkin = null;
    RevBlinkinLedDriver.BlinkinPattern pattern;
    ColorSensor colorSensor = null;
    double topSpeed = 0.7;
    double sensitivity = 0.4;
    double acceleration = 0.1;
    double accelOutput = 0.0;

    HardwareMap hwMap = null;

    private ElapsedTime runtime = new ElapsedTime();

    public void BowserHardwareMap(){
    }
    public void init( HardwareMap ahwMap ) 
    {
        // Save reference to Hardware map
        HardwareMap hwMap = ahwMap;  
        
        leftMotorF  = hwMap.get(DcMotor.class, "leftMotorF");
        leftMotorB = hwMap.get(DcMotor.class, "leftMotorB");
        rightMotorF = hwMap.get(DcMotor.class, "rightMotorF");
        rightMotorB = hwMap.get(DcMotor.class, "rightMotorB");
        blinkin = hwMap.get(RevBlinkinLedDriver.class,"blinkin");
        colorSensor = hwMap.get(ColorSensor.class,"colorSensor");
        
        leftMotorF.setDirection(DcMotor.Direction.FORWARD);
        leftMotorB.setDirection(DcMotor.Direction.FORWARD);
        rightMotorF.setDirection(DcMotor.Direction.FORWARD);
        rightMotorB.setDirection(DcMotor.Direction.FORWARD);
    }
        //default move method 
        public void move(double forward, double strafe, double turn, double power){
            double leftPowerF = power * forward - turn * sensitivity - strafe;
            double leftPowerB = power * forward - turn * sensitivity + strafe;
            double rightPowerF = -power * forward - turn * sensitivity - strafe;
            double rightPowerB = -power * forward - turn * sensitivity + strafe;
            leftMotorF.setPower(leftPowerF * topSpeed);
            rightMotorF.setPower(rightPowerF * topSpeed);
            leftMotorB.setPower(leftPowerB * topSpeed);
            rightMotorB.setPower(rightPowerB * topSpeed);
        }
        //takes status and makes lights those colors
        public void colorToLights(int status){
        if(status == 0){
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
        }
        if(status == 1){
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
        }
        if(status == 2){
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
        }
        if(status == 3){
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
        }
        if(status == 4){
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
        }
        if(status == 5){
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.VIOLET);
        }
    }
    //detects what color you are on
    //you will need to calibrate these values for your color sensor
    public int status(int red, int green, int blue){
        // (0 none) (1 red) (2 blue) (3 green) (4 yellow)
        int status = 0;
        if((red > blue && red > green) && red > 225){
            status = 1;
        }
        if((red < blue && blue > green)&& blue > 325){
            status = 2;
        }
        if((green > blue && red < green)&& green > 250 && red < blue && green < 500){
            status = 3;
        }
        if((green > blue && red > blue)&& red > 300 && green > 500){
            status = 4;
        }
        return status;
    }
    
    public double accel(double input){
        if(runtime.seconds() > 0.1){
            accelOutput = Range.clip(accelOutput + acceleration * input,-1,1);
            runtime.reset();
        }if(input ==0){
            accelOutput = 0;
        }
        
    return accelOutput;
    }
}
