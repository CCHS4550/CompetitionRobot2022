package frc.robot.deprecated;

import frc.helpers.*;
import frc.parent.*;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.DriverStation;

import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class oldChassis{

    public static void nothing(){
        double rumble = shift.on() ? .3 : 0;
        OI.joystickArray[0].setRumble(RumbleType.kLeftRumble, rumble);
        OI.joystickArray[0].setRumble(RumbleType.kRightRumble, rumble);
    }

    //Talon objects for the wheels
    //These control the main 4 motors on the robot
    public static CCSparkMax fLeft = new CCSparkMax("Front Left", "FL", RobotMap.FORWARD_LEFT, 
        MotorType.kBrushless, IdleMode.kCoast, RobotMap.FORWARD_LEFT_REVERSE, true);

    public static CCSparkMax fRight = new CCSparkMax("Front Right", "FR", RobotMap.FORWARD_RIGHT, 
        MotorType.kBrushless, IdleMode.kCoast, RobotMap.FORWARD_RIGHT_REVERSE, true);

    public static CCSparkMax bLeft = new CCSparkMax("Back Left", "BL",RobotMap.BACK_LEFT, 
        MotorType.kBrushless, IdleMode.kCoast, RobotMap.BACK_LEFT_REVERSE, true);

    public static CCSparkMax bRight = new CCSparkMax("Back Right", "BR", RobotMap.BACK_RIGHT, 
        MotorType.kBrushless, IdleMode.kCoast, RobotMap.BACK_RIGHT_REVERSE, true);

    public static PneumaticsSystem shift = new PneumaticsSystem(PneumaticsModuleType.CTREPCM, RobotMap.SHIFT_SOLENOID_ONE, RobotMap.SHIFT_SOLENOID_TWO);

    //AHRS gyro measures the angle of the bot
    public static AHRS gyro = new AHRS(SPI.Port.kMXP);    

    //To be used in TeleOP
    //Takes in two axises, most likely the controller axises
    //Optimized for a west coast or standard chassis
    //DO NOT USE THIS FOR SWERV DRIVE 
    public static void axisDrive(double yAxis, double xAxis, double max){
        fLeft.set(-OI.normalize((yAxis - xAxis), -max, max));
        fRight.set(-OI.normalize((yAxis + xAxis), -max, max));
        bLeft.set(-OI.normalize((yAxis - xAxis), -max, max));
        bRight.set(-OI.normalize((yAxis + xAxis), -max, max));
    }

    

    //To be used on Auto/PIDs
    //Simply sets the motor controllers to a certain percent output
    public static void driveSpd(double lSpeed, double rSpeed){
        fLeft.set(OI.normalize(lSpeed, -1.0, 1.0));
        fRight.set(OI.normalize(rSpeed, -1.0, 1.0));
        bLeft.set(OI.normalize(lSpeed, -1.0, 1.0));
        bRight.set(OI.normalize(rSpeed, -1.0, 1.0));
    }

    public static void setFactor(double factor){
        //0.048 slow, 0.109 fast
        fLeft.setPositionConversionFactor(factor);
        fRight.setPositionConversionFactor(factor);
        bLeft.setPositionConversionFactor(factor);
        bRight.setPositionConversionFactor(factor);

    }

   
    //Sets the gyro and encoders to zero
    public static void reset(){
        gyro.reset();
        fLeft.reset();
        fRight.reset();
        bLeft.reset();
        bRight.reset();   
        
   }

    public static double getLDist(){
        double dist = (fLeft.getPosition() + bLeft.getPosition())/2;
        return dist;
    }

    public static double getRDist(){
        double dist = (fRight.getPosition() + bRight.getPosition())/2;
        return dist;
    }

    public static double getAngle(){
        return gyro.getAngle();
    }

    /*
        "Whosever holds these loops, if he be worthy, shall posses the power of AJ"
    */
    public static void setFastMode(boolean on){
        shift.set(on);
    }

    /** 
     * Toggles fast mode.
    */
    public static void toggleFastMode(){
        shift.toggle();
    }

    /** 
     * Toggles fast mode. Will only trigger again after trigger is false
     *@param trigger what will trigger the toggle. Suggest passing in a button or axis input.
    */
    public static void toggleFastMode(boolean trigger){
        shift.triggerSystem(trigger);
    }

    public static boolean triggered2 = false;
    public static boolean fast = false;
    public static void toggleFast(boolean trigger){
        if(trigger){
            if(!triggered2){
                fast = !fast;
                triggered2 = true;
            }
        } else {
            triggered2 = false;
        }
    }
    
    /**
     * Toggles fast mode. Will only trigger again after trigger is false
     * 
     * @param trigger what will trigger the toggle. Suggest passing in a button or axis input.
     * @param cont which controller will rumble to indicate speed
     */
    public static boolean triggered = false;
    public static void toggleFastMode(boolean trigger, Joystick cont){
        shift.triggerSystem(trigger);
        if(trigger){
            if(!triggered){
                double rumble = shift.on() ? .3 : 0;
                cont.setRumble(RumbleType.kLeftRumble, rumble);
                cont.setRumble(RumbleType.kRightRumble, rumble);
                // if(shift.on()) Face.angry();
                // else Face.neutral();
                triggered = true;
            }
        } else {
            triggered = false;
        }
    }

    //Drives the robot to a certain distance
    //Kinda complex -> DO NOT TOUCH
    public static void driveDist(double goal, double accepted, double kp, double max, boolean debug){
        setFactor(0.048);
        //double aError = goal*aPer;

        double lPos = getLDist();
        double lError = goal-lPos;
        double lSpd = 0;

        double rPos = getRDist();
        double rError = goal-rPos;
        double rSpd = 0; 

        while(true){
            lPos = getLDist();
            lError = goal-lPos;
            lSpd = lError*kp;
            lSpd = OI.normalize(lSpd, -max, max);

            rPos = getRDist();
            rError = goal-rPos;
            rSpd = rError*kp;
            rSpd = OI.normalize(rSpd, -max, max);

            driveSpd(lSpd, rSpd);

            if(debug){
                System.out.println("Left - Left Speed: " + lSpd + 
                                        " Left Error: " + lError + 
                                        " Left Position: " + lPos);
                System.out.println("Right - Right Speed: " + rSpd + 
                                        " Right Error: " + rError + 
                                        " Right Position" + rPos);
            }

            if((Math.abs(lError) <= accepted && Math.abs(rError) <= accepted) || !DriverStation.isAutonomous()){
                driveSpd(0.0, 0.0);
                System.out.println("YOINK, ya made it");
                break; 
            }
        }
    }

    //Turns the robot to a certain angle, a positive angle will turn right
    //Kinda complex -> DO NOT TOUCH
    public static void turnToAngle(double goal, double aPer, double kp, double max, boolean debug){

        double aError = goal*aPer;

        double angl = gyro.getAngle();
        double error = goal-angl;
        double input = 0;

        while(true){
            angl = gyro.getAngle();
            error = goal-angl;
            input = error*kp;
            input = OI.normalize(input, -max, max);

            driveSpd(input, -input);

            if(debug){
                System.out.println("Input: " + input);
                System.out.println("Error: " + error);
                System.out.println("Angle: " + angl);
            }
            if(Math.abs(error) <= aError || !DriverStation.isAutonomous()){
                driveSpd(0.0, 0.0);
                System.out.println("YOINK, ya made it");
                break; 
            }
        }
    }

    /**
    Turns to an angle 
    @param goal the target angle
    @param kp the proportional constant\
    @param max the maximum speed
    @param aError the accepted error
    @param step what step in autonomous this is
    @return if this step is completed or not
     */
    public static boolean turnToAnglePeriodic(double goal, double kp, double max, double aError, int step){
        if(step < oldRobot.autoStep) return true;
        double angl = gyro.getAngle();
        double error = goal-angl;
        double input = error*kp;
        input = OI.normalize(input, -max, max);

        driveSpd(input, -input);

        if(Math.abs(error) <= aError){
            driveSpd(0.0, 0.0);
            System.out.println("YOINK, ya made it");
            oldRobot.autoStep++;
            return true;
        }
        return false;
    }

    /**
    drives to a set distance
    @param goal the target distance
    @param kp the proportional constant\
    @param max the maximum speed
    @param aError the accepted error
    @param step what step in autonomous this is
    @return if this step is completed or not
     */
    public static boolean driveDistPeriodic(double goal, double kp, double max, double aError, int step){
        if(step < oldRobot.autoStep) return true;
        setFactor(0.048);
        double lPos = getLDist();
        double lError = goal-lPos;
        double lSpd = lError*kp;
        lSpd = OI.normalize(lSpd, -max, max);

        double rPos = getRDist();
        double rError = goal-rPos;
        double rSpd = rError*kp;
        rSpd = OI.normalize(rSpd, -max, max);

        driveSpd(lSpd, rSpd);

        if(Math.abs(lError) <= aError && Math.abs(rError) <= aError){
            driveSpd(0.0, 0.0);
            System.out.println("YOINK, ya made it");
            oldRobot.autoStep++;
            return true;
        }
        return false;
    }

    // public static DifferentialDrive frontDrive = new DifferentialDrive(fLeft, fRight);
    // public static DifferentialDrive backDrive = new DifferentialDrive(bLeft, bRight);
    // /**
    //  * it's about drive it's about power we stay hungry we devour
    // */
    // public static void arcadeDrive(double forward, double side){
    //     frontDrive.arcadeDrive(forward, side);
    //     backDrive.arcadeDrive(forward, side);
    // }
    
}
/*


 */