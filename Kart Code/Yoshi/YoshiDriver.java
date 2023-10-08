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

public class YoshiDriver{
    //declaring variables, for tweaking use sensitivity and top speed
    public DcMotor rightMotor , leftMotor, backMotor;
    public RevBlinkinLedDriver blinkin = null;
    RevBlinkinLedDriver.BlinkinPattern pattern;
    ColorSensor colorSensor = null;
    double topSpeed = 0.7;
    double sensitivity = 0.2;
    double acceleration = 0.1;
    double accelOutput = 0.0;

    HardwareMap hwMap = null;

    private ElapsedTime runtime = new ElapsedTime();

    public void xDriveHardwareMap(){
    }
    public void init( HardwareMap ahwMap ) 
    {
        // Save reference to Hardware map
        HardwareMap hwMap = ahwMap;  
        
        rightMotor  = hwMap.get(DcMotor.class, "rightMotor");
        leftMotor  = hwMap.get(DcMotor.class, "leftMotor");
        backMotor = hwMap.get(DcMotor.class, "backMotor");
        blinkin = hwMap.get(RevBlinkinLedDriver.class,"blinkin");
        colorSensor = hwMap.get(ColorSensor.class, "colorSensor");
    }
        //default move method 
        public void move(double forward, double strafe, double turn, double power){
            double backPower =-turn * sensitivity;
            double leftPower = -power * forward - turn * sensitivity;
            double rightPower = power * forward - turn * sensitivity;
            backMotor.setPower(backPower * topSpeed);
            leftMotor.setPower(leftPower * topSpeed);
            rightMotor.setPower(rightPower * topSpeed);
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
        // (0 none) (1 red) (2 blue) (3 green) (4 yellow) (5 purple)
        int status = 0;
        if((red > blue && red > green) && red > 200 && red < 300){
            status = 1;
        }
        if((red < blue && blue > green)&& blue > 500){
            status = 2;
        }
        if((green > blue && red < green)&& green > 300 && red < blue && green < 400){
            status = 3;
        }
        if((green > blue && red > blue)&& red > 300 && green > 550){
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
