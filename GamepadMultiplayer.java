package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="GamepadMultiplayer", group="Linear Opmode")

public class GamepadMultiplayer extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor driveLeft = null;
    private DcMotor driveRight = null;
    private DcMotor omniLeft = null;
    private DcMotor omniRight = null;
    private DcMotor arm = null;
    private DcMotor carousel = null;
    private Servo claw = null;
    private Servo claw2 = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        driveLeft  = hardwareMap.get(DcMotor.class, "drive_left");
        driveRight = hardwareMap.get(DcMotor.class, "drive_right");
        omniLeft  = hardwareMap.get(DcMotor.class, "omni_left");
        omniRight = hardwareMap.get(DcMotor.class, "omni_right");
        arm = hardwareMap.get(DcMotor.class, "arm");
        carousel = hardwareMap.get(DcMotor.class, "carousel");
        //claw = hardwareMap.get(Servo.class, "claw");
        claw2 = hardwareMap.get(Servo.class, "claw2");



        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        
        driveLeft.setDirection(DcMotor.Direction.FORWARD);
        driveRight.setDirection(DcMotor.Direction.REVERSE);
        omniLeft.setDirection(DcMotor.Direction.FORWARD);
        omniRight.setDirection(DcMotor.Direction.REVERSE);
        arm.setDirection(DcMotor.Direction.REVERSE);
        carousel.setDirection(DcMotor.Direction.FORWARD);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;
            double armPower = 0;

            // POV Mode uses left stick to go forward, and right stick to turn.
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;
            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
            
            // Send calculated power to wheels
            driveLeft.setPower(leftPower);
            driveRight.setPower(rightPower);
            omniLeft.setPower(leftPower);
            omniRight.setPower(rightPower);
           
            
            //if "a" is pressed, grab Object
            if(gamepad2.a)
            {
                claw2.setPosition(0.15);
                
            }
            //if "b" is pressed, release object
            if(gamepad2.b)
            {
                claw2.setPosition(0.9);
                
            }
            
            
            //binds arm to triggers
            if(gamepad2.right_trigger <= 0.3)
            {   
                //sets a deadzone of 0.3, where the arm doesnt move at 0.3.
                //if trigger is less than or equal to 0.3, keep the arm in place.
                
                armPower = 0.08;
                arm.setPower(armPower);

            }
            
               
            //right trigger moves arm up
            if(gamepad2.right_trigger >= 0.25)
            {
                armPower = gamepad1.right_trigger;
                arm.setPower(armPower);
            }
            else
            {
                //left trigger moves arm down
                if(gamepad2.left_trigger >= 0.25)
                {
                    armPower = gamepad1.left_trigger;
                    arm.setPower(-armPower);
                }
            }
            
            if(gamepad1.right_bumper||gamepad1.left_bumper)
            {
                carousel.setPower(1);
                sleep(1000);
                carousel.setPower(0);                                
            }
            
            
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }
}
