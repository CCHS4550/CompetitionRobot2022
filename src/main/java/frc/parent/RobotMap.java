package frc.parent;

// import com.revrobotics.CANSparkMax.IdleMode;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// import frc.robot.CCSparkMax;

/*
    RobotMap holds all the ports involved in the robot.
    This ranges from talon ports, all the way to the ports
    on the controllers. This also contains the polarity for the wheels
    and the various ports assoiated with sensors

    If you wish to create your own port, here is the syntax:
        public static final returnType name = value;
    Notes on creating ports:
        1. Ports must be integers or booleans
        2. they MUST be public static final;
        3. If the port is not plugged in, make int values -1, and boolean values false


*/
public interface RobotMap {

    // Wheel Talons
    public static final int FORWARD_LEFT = 1;
    public static final int FORWARD_RIGHT = 3;
    public static final int BACK_LEFT = 2;
    public static final int BACK_RIGHT = 4;
    public static final int CLIMBER = 5;
    public static final int SHOOTER = 9;
    public static final int SHOOTER2 = 10; // only on 2020 bot
    public static final int LOADER = 7;
    public static final int SPINDEXER = 5;
    public static final int SUCKY = 6;


    //Wheel Polarities
    public static final boolean FORWARD_LEFT_REVERSE = true;
    public static final boolean FORWARD_RIGHT_REVERSE = false;
    public static final boolean BACK_LEFT_REVERSE = true;
    public static final boolean BACK_RIGHT_REVERSE = false;
    public static final boolean CLIMBER_LEFT_REVERSE = false;
    public static final boolean CLIMBER_RIGHT_REVERSE = false;
    public static final boolean SHOOTER_REVERSE = false;
    public static final boolean LOADER_REVERSE = false;
    public static final boolean SUCKY_REVERSE = false;

    
    //Pneumatic Stuff 
    public static final boolean COMPRESSOR_ENABLE = true;
    public static final int ARM_SOLENOID_ONE = 3;
    public static final int ARM_SOLENOID_TWO = 2;
    public static final int SHIFT_SOLENOID_ONE = 0;
    public static final int SHIFT_SOLENOID_TWO = 1;
    public static final int INTAKE_SOLENOID_ONE = 2;
    public static final int INTAKE_SOLENOID_TWO = 3;

    //Other Constancts 
    public static final int ROBOT_Y_DIR_SIGN = 1;
    public static final int ROBOT_X_DIR_SIGN = 1;
}