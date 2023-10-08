package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp(name="BowserKart", group="Iterative Opmode")
public class BowserTeleop extends OpMode
{
    // Declare OpMode members
    //make instances of elapsed time for acceleration and power up
    //also bring hardware map from XHardwareMap

    private BowserDriver robot = new BowserDriver();
    ColorSensor colorSensor = null;
    int status;
    double forward;
    double strafe;
    double turn;
    double speed;
    double acceleration = 0.1;
    
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        robot.init(hardwareMap);
         colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }
    
    @Override
    public void loop() {
        double input;
        if (gamepad2.a || gamepad2.b) {
            double a = gamepad2.a ? -1.0 : 0.0;
            double b = gamepad2.b ? -1.0 : 0.0;
            input = b - a;
        } else {
            input = gamepad1.left_trigger - gamepad1.right_trigger;
        }
            forward = robot.accel(input);

        strafe = 0.7 * Math.abs(forward) * gamepad1.left_stick_x + 0.7 * Math.abs(forward) * gamepad2.right_stick_x;
        turn = gamepad1.right_stick_x + gamepad2.left_stick_x;
        

        //status tells us what color the bot is on
        status = robot.status(colorSensor.red(), colorSensor.green(), colorSensor.blue());
        
        //all of this is giving the bot different drive modes based on the color (go to XHardwareMap for config of colors)
        switch(status){
        case 0:
            robot.move(forward, strafe, turn, 1);
            break;
        case 1:
            robot.move(forward, strafe, turn, 0.5);
            break;
        case 2:
            robot.move(forward,strafe, -turn, 1);
            break;
        case 3:
            robot.move(forward, strafe, turn, 2);
            break;
        case 4: 
            robot.move(forward, strafe, 13 * turn + 10, 1);
            break;
        }
        
        
        //Makes status effect the light color
        robot.colorToLights(status);
        
        telemetry.addData("status",status);
    
        telemetry.addData("red",colorSensor.red());
        telemetry.addData("green",colorSensor.green());
        telemetry.addData("blue",colorSensor.blue());
        telemetry.addData("forward", forward);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}