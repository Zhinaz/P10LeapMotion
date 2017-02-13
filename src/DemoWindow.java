import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import java.io.IOException;
import java.text.DecimalFormat;

import com.leapmotion.leap.*;
import org.eclipse.swt.widgets.Label;

public class DemoWindow {

	public Shell shell;
	public static Text textTimeStamp;
	public static Text textFrame;
	public static Text textHands;
	public static Text textFingers;
	private Label lblHandType;
	private Text textLeftHandtype;
	private Text textLeftPalm;
	private Label lblPalmPosition;
	private Text textLeftHandID;
	private Text textLeftVector;
	private Text textLeftDirection;
	private Text textLeftPitch;
	private Text textLeftRoll;
	private Text textLeftYaw;
	private Text textLeftElbow;
	private Text textLeftArm;
	private Text textLeftWrist;
	private Text textLeftFinger0Type;
	private Text textLeftFinger1Type;
	private Text textLeftFinger2Type;
	private Text textLeftFinger3Type;
	private Text textLeftFinger4Type;
	private Text textLeftFinger0ID;
	private Text textLeftFinger1ID;
	private Text textLeftFinger2ID;
	private Text textLeftFinger3ID;
	private Text textLeftFinger4ID;
	private Label lblType;
	private Label lblId;
	private Text textLeftFinger0Length;
	private Text textLeftFinger1Length;
	private Text textLeftFinger2Length;
	private Text textLeftFinger3Length;
	private Text textLeftFinger4Length;
	private Label lblLength;
	private Text textLeftFinger0Extended;
	private Text textLeftFinger1Extended;
	private Text textLeftFinger2Extended;
	private Text textLeftFinger3Extended;
	private Text textLeftFinger4Extended;
	private Label lblWidth;
	private Text textLeftFinger0JS0;
	private Text textLeftFinger0JD0;
	private Text textLeftFinger0JE0;
	private Text textLeftFinger0JE1;
	private Text textLeftFinger0JD1;
	private Text textLeftFinger0JS1;
	private Text textLeftFinger0JE2;
	private Text textLeftFinger0JD2;
	private Text textLeftFinger0JS2;
	private Text textLeftFinger1JE2;
	private Text textLeftFinger1JD2;
	private Text textLeftFinger1JS2;
	private Text textLeftFinger1JE1;
	private Text textLeftFinger1JD1;
	private Text textLeftFinger1JS1;
	private Text textLeftFinger1JE0;
	private Text textLeftFinger1JD0;
	private Text textLeftFinger1JS0;
	private Text textLeftFinger2JE2;
	private Text textLeftFinger2JD2;
	private Text textLeftFinger2JS2;
	private Text textLeftFinger2JE1;
	private Text textLeftFinger2JD1;
	private Text textLeftFinger2JS1;
	private Text textLeftFinger2JE0;
	private Text textLeftFinger2JD0;
	private Text textLeftFinger2JS0;
	private Text textLeftFinger3JE2;
	private Text textLeftFinger3JD2;
	private Text textLeftFinger3JS2;
	private Text textLeftFinger3JE1;
	private Text textLeftFinger3JD1;
	private Text textLeftFinger3JS1;
	private Text textLeftFinger3JE0;
	private Text textLeftFinger3JD0;
	private Text textLeftFinger3JS0;
	private Text textLeftFinger4JE2;
	private Text textLeftFinger4JD2;
	private Text textLeftFinger4JS2;
	private Text textLeftFinger4JE1;
	private Text textLeftFinger4JD1;
	private Text textLeftFinger4JS1;
	private Text textLeftFinger4JE0;
	private Text textLeftFinger4JD0;
	private Text textLeftFinger4JS0;
	private Label lblJointStart;
	private Label lblDirection_1;
	private Label lblJoinEnd;
	private Label label_6;
	private Label label_7;
	private Label label_8;
	private Label label_9;
	private Label label_10;
	private Label label_11;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		        
		try {
			DemoWindow window = new DemoWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		DecimalFormat df = new DecimalFormat("#.##");
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				
				
				Controller controller = new Controller();
		        Frame frame = controller.frame();
				textFrame.setText(String.valueOf(frame.id()));
                textTimeStamp.setText(String.valueOf(frame.timestamp()));
                textHands.setText(String.valueOf(frame.hands().count()));
                textFingers.setText(String.valueOf(frame.fingers().count()));
				
                // Hand
                Hand hand0 = frame.hands().get(0);
                
                textLeftHandtype.setText(hand0.isLeft() ? "Left hand" : "Right hand");
                textLeftHandID.setText(String.valueOf(hand0.id()));
                
                textLeftPalm.setText("(" + String.valueOf(df.format(hand0.palmPosition().getX())) + ", " +
                		String.valueOf(df.format(hand0.palmPosition().getY())) + ", " +
                		String.valueOf(df.format(hand0.palmPosition().getZ())) + ")");
                
            	textLeftVector.setText("(" + String.valueOf(df.format(hand0.palmNormal().getX()))+ ", " +
            			String.valueOf(df.format(hand0.palmNormal().getY())) + ", " +
            			String.valueOf(df.format(hand0.palmNormal().getY())) + ")");
            	
            	textLeftDirection.setText("(" + String.valueOf(df.format(hand0.direction().getX()))+ ", " +
            			String.valueOf(df.format(hand0.direction().getY())) + ", " +
            			String.valueOf(df.format(hand0.direction().getY())) + ")");
            	
            	textLeftPitch.setText(String.valueOf(df.format(hand0.direction().pitch())));
            	textLeftRoll.setText(String.valueOf(df.format(hand0.palmNormal().roll())));
            	textLeftYaw.setText(String.valueOf(df.format(hand0.direction().yaw())));
            	
            	textLeftWrist.setText("(" + String.valueOf(df.format(hand0.arm().wristPosition().getX()))+ ", " +
            			String.valueOf(df.format(hand0.arm().wristPosition().getY())) + ", " +
            			String.valueOf(df.format(hand0.arm().wristPosition().getY())) + ")");
            	
            	textLeftArm.setText("(" + String.valueOf(df.format(hand0.arm().direction().getX()))+ ", " +
            			String.valueOf(df.format(hand0.arm().direction().getY())) + ", " +
            			String.valueOf(df.format(hand0.arm().direction().getY())) + ")");
            	
            	textLeftElbow.setText("(" + String.valueOf(df.format(hand0.arm().elbowPosition().getX()))+ ", " +
            			String.valueOf(df.format(hand0.arm().elbowPosition().getY())) + ", " +
            			String.valueOf(df.format(hand0.arm().elbowPosition().getY())) + ")");
            	
            	// Fingers
            	Finger finger00 = hand0.fingers().get(0);
            	Finger finger01 = hand0.fingers().get(1);
            	Finger finger02 = hand0.fingers().get(2);
            	Finger finger03 = hand0.fingers().get(3);
            	Finger finger04 = hand0.fingers().get(4);
            		
        		textLeftFinger0Type.setText(String.valueOf(finger00.type()));
        		textLeftFinger1Type.setText(String.valueOf(finger01.type()));
        		textLeftFinger2Type.setText(String.valueOf(finger02.type()));
        		textLeftFinger3Type.setText(String.valueOf(finger03.type()));
        		textLeftFinger4Type.setText(String.valueOf(finger04.type()));
        		
        		textLeftFinger0ID.setText(String.valueOf(finger00.id()));
        		textLeftFinger1ID.setText(String.valueOf(finger01.id()));
        		textLeftFinger2ID.setText(String.valueOf(finger02.id()));
        		textLeftFinger3ID.setText(String.valueOf(finger03.id()));
        		textLeftFinger4ID.setText(String.valueOf(finger04.id()));
        		
        		textLeftFinger0Length.setText(String.valueOf(finger00.length()));
        		textLeftFinger1Length.setText(String.valueOf(finger01.length()));
        		textLeftFinger2Length.setText(String.valueOf(finger02.length()));
        		textLeftFinger3Length.setText(String.valueOf(finger03.length()));
        		textLeftFinger4Length.setText(String.valueOf(finger04.length()));
        		
        		textLeftFinger0Extended.setText(String.valueOf(finger00.isExtended()));
        		textLeftFinger1Extended.setText(String.valueOf(finger01.isExtended()));
        		textLeftFinger2Extended.setText(String.valueOf(finger02.isExtended()));
        		textLeftFinger3Extended.setText(String.valueOf(finger03.isExtended()));
        		textLeftFinger4Extended.setText(String.valueOf(finger04.isExtended()));

                // Bones
        		textLeftFinger0JE0.setText(String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_PROXIMAL).nextJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_PROXIMAL).nextJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_PROXIMAL).nextJoint().getZ())));
        		textLeftFinger0JD0.setText(String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_PROXIMAL).direction().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_PROXIMAL).direction().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_PROXIMAL).direction().getZ())));
        		textLeftFinger0JS0.setText(String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_PROXIMAL).prevJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_PROXIMAL).prevJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_PROXIMAL).prevJoint().getZ())));
        		
        		textLeftFinger1JE0.setText(String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_PROXIMAL).nextJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_PROXIMAL).nextJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_PROXIMAL).nextJoint().getZ())));
        		textLeftFinger1JD0.setText(String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_PROXIMAL).direction().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_PROXIMAL).direction().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_PROXIMAL).direction().getZ())));
        		textLeftFinger1JS0.setText(String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_PROXIMAL).prevJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_PROXIMAL).prevJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_PROXIMAL).prevJoint().getZ())));
        		
        		textLeftFinger2JE0.setText(String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_PROXIMAL).nextJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_PROXIMAL).nextJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_PROXIMAL).nextJoint().getZ())));
        		textLeftFinger2JD0.setText(String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_PROXIMAL).direction().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_PROXIMAL).direction().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_PROXIMAL).direction().getZ())));
        		textLeftFinger2JS0.setText(String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_PROXIMAL).prevJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_PROXIMAL).prevJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_PROXIMAL).prevJoint().getZ())));
        		
        		textLeftFinger3JE0.setText(String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_PROXIMAL).nextJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_PROXIMAL).nextJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_PROXIMAL).nextJoint().getZ())));
        		textLeftFinger3JD0.setText(String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_PROXIMAL).direction().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_PROXIMAL).direction().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_PROXIMAL).direction().getZ())));
        		textLeftFinger3JS0.setText(String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_PROXIMAL).prevJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_PROXIMAL).prevJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_PROXIMAL).prevJoint().getZ())));
        		
        		textLeftFinger4JE0.setText(String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_PROXIMAL).nextJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_PROXIMAL).nextJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_PROXIMAL).nextJoint().getZ())));
        		textLeftFinger4JD0.setText(String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_PROXIMAL).direction().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_PROXIMAL).direction().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_PROXIMAL).direction().getZ())));
        		textLeftFinger4JS0.setText(String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_PROXIMAL).prevJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_PROXIMAL).prevJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_PROXIMAL).prevJoint().getZ())));
        		
        		
        		textLeftFinger0JE1.setText(String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_INTERMEDIATE).nextJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_INTERMEDIATE).nextJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_INTERMEDIATE).nextJoint().getZ())));
        		textLeftFinger0JD1.setText(String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_INTERMEDIATE).direction().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_INTERMEDIATE).direction().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_INTERMEDIATE).direction().getZ())));
        		textLeftFinger0JS1.setText(String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_INTERMEDIATE).prevJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_INTERMEDIATE).prevJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_INTERMEDIATE).prevJoint().getZ())));
        		
        		textLeftFinger1JE1.setText(String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_INTERMEDIATE).nextJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_INTERMEDIATE).nextJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_INTERMEDIATE).nextJoint().getZ())));
        		textLeftFinger1JD1.setText(String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_INTERMEDIATE).direction().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_INTERMEDIATE).direction().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_INTERMEDIATE).direction().getZ())));
        		textLeftFinger1JS1.setText(String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_INTERMEDIATE).prevJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_INTERMEDIATE).prevJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_INTERMEDIATE).prevJoint().getZ())));
        		
        		textLeftFinger2JE1.setText(String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_INTERMEDIATE).nextJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_INTERMEDIATE).nextJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_INTERMEDIATE).nextJoint().getZ())));
        		textLeftFinger2JD1.setText(String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_INTERMEDIATE).direction().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_INTERMEDIATE).direction().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_INTERMEDIATE).direction().getZ())));
        		textLeftFinger2JS1.setText(String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_INTERMEDIATE).prevJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_INTERMEDIATE).prevJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_INTERMEDIATE).prevJoint().getZ())));
        		
        		textLeftFinger3JE1.setText(String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_INTERMEDIATE).nextJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_INTERMEDIATE).nextJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_INTERMEDIATE).nextJoint().getZ())));
        		textLeftFinger3JD1.setText(String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_INTERMEDIATE).direction().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_INTERMEDIATE).direction().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_INTERMEDIATE).direction().getZ())));
        		textLeftFinger3JS1.setText(String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_INTERMEDIATE).prevJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_INTERMEDIATE).prevJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_INTERMEDIATE).prevJoint().getZ())));
        		
        		textLeftFinger4JE1.setText(String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_INTERMEDIATE).nextJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_INTERMEDIATE).nextJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_INTERMEDIATE).nextJoint().getZ())));
        		textLeftFinger4JD1.setText(String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_INTERMEDIATE).direction().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_INTERMEDIATE).direction().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_INTERMEDIATE).direction().getZ())));
        		textLeftFinger4JS1.setText(String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_INTERMEDIATE).prevJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_INTERMEDIATE).prevJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_INTERMEDIATE).prevJoint().getZ())));
        		
        		
        		textLeftFinger0JE2.setText(String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_DISTAL).nextJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_DISTAL).nextJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_DISTAL).nextJoint().getZ())));
        		textLeftFinger0JD2.setText(String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_DISTAL).direction().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_DISTAL).direction().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_DISTAL).direction().getZ())));
        		textLeftFinger0JS2.setText(String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_DISTAL).prevJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_DISTAL).prevJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(0).bone(Bone.Type.TYPE_DISTAL).prevJoint().getZ())));
        		
        		textLeftFinger1JE2.setText(String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_DISTAL).nextJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_DISTAL).nextJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_DISTAL).nextJoint().getZ())));
        		textLeftFinger1JD2.setText(String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_DISTAL).direction().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_DISTAL).direction().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_DISTAL).direction().getZ())));
        		textLeftFinger1JS2.setText(String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_DISTAL).prevJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_DISTAL).prevJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(1).bone(Bone.Type.TYPE_DISTAL).prevJoint().getZ())));
        		
        		textLeftFinger2JE2.setText(String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_DISTAL).nextJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_DISTAL).nextJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_DISTAL).nextJoint().getZ())));
        		textLeftFinger2JD2.setText(String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_DISTAL).direction().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_DISTAL).direction().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_DISTAL).direction().getZ())));
        		textLeftFinger2JS2.setText(String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_DISTAL).prevJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_DISTAL).prevJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(2).bone(Bone.Type.TYPE_DISTAL).prevJoint().getZ())));
        		
        		textLeftFinger3JE2.setText(String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_DISTAL).nextJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_DISTAL).nextJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_DISTAL).nextJoint().getZ())));
        		textLeftFinger3JD2.setText(String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_DISTAL).direction().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_DISTAL).direction().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_DISTAL).direction().getZ())));
        		textLeftFinger3JS2.setText(String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_DISTAL).prevJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_DISTAL).prevJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(3).bone(Bone.Type.TYPE_DISTAL).prevJoint().getZ())));
        		
        		textLeftFinger4JE2.setText(String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_DISTAL).nextJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_DISTAL).nextJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_DISTAL).nextJoint().getZ())));
        		textLeftFinger4JD2.setText(String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_DISTAL).direction().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_DISTAL).direction().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_DISTAL).direction().getZ())));
        		textLeftFinger4JS2.setText(String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_DISTAL).prevJoint().getX())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_DISTAL).prevJoint().getY())) + ", " +
        				String.valueOf(df.format(hand0.fingers().get(4).bone(Bone.Type.TYPE_DISTAL).prevJoint().getZ())));
        		

        		
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	public void createContents() {
		shell = new Shell();
		shell.setSize(712, 700);
		shell.setText("Hand Gesture Demonstration");
		
		textTimeStamp = new Text(shell, SWT.BORDER);
		textTimeStamp.setBounds(10, 25, 76, 21);
		
		Label lblTimestamp = new Label(shell, SWT.NONE);
		lblTimestamp.setBounds(10, 10, 76, 15);
		lblTimestamp.setText("TimeStamp");
		
		textFrame = new Text(shell, SWT.BORDER);
		textFrame.setBounds(92, 25, 76, 21);
		
		Label lblFrame = new Label(shell, SWT.NONE);
		lblFrame.setBounds(92, 10, 55, 15);
		lblFrame.setText("Frame");
		
		textHands = new Text(shell, SWT.BORDER);
		textHands.setBounds(174, 25, 76, 21);
		
		Label lblHands = new Label(shell, SWT.NONE);
		lblHands.setBounds(174, 10, 55, 15);
		lblHands.setText("# hands");
		
		textFingers = new Text(shell, SWT.BORDER);
		textFingers.setBounds(256, 25, 76, 21);
		
		Label lblFingers = new Label(shell, SWT.NONE);
		lblFingers.setBounds(256, 10, 55, 15);
		lblFingers.setText("# fingers");
		
		lblHandType = new Label(shell, SWT.NONE);
		lblHandType.setBounds(262, 67, 55, 15);
		lblHandType.setText("Hand type");
		
		textLeftHandtype = new Text(shell, SWT.BORDER);
		textLeftHandtype.setBounds(262, 83, 76, 21);
		
		textLeftPalm = new Text(shell, SWT.BORDER);
		textLeftPalm.setBounds(92, 495, 199, 21);
		
		lblPalmPosition = new Label(shell, SWT.NONE);
		lblPalmPosition.setBounds(10, 498, 76, 15);
		lblPalmPosition.setText("Palm position");
		
		textLeftHandID = new Text(shell, SWT.BORDER);
		textLeftHandID.setBounds(347, 83, 76, 21);
		
		Label lblHandId = new Label(shell, SWT.NONE);
		lblHandId.setBounds(347, 67, 55, 15);
		lblHandId.setText("Hand ID");
		
		textLeftVector = new Text(shell, SWT.BORDER);
		textLeftVector.setBounds(92, 468, 199, 21);
		
		Label lblNormalVector = new Label(shell, SWT.NONE);
		lblNormalVector.setBounds(10, 471, 76, 15);
		lblNormalVector.setText("Normal vector");
		
		textLeftDirection = new Text(shell, SWT.BORDER);
		textLeftDirection.setBounds(92, 522, 199, 21);
		
		Label lblDirection = new Label(shell, SWT.NONE);
		lblDirection.setBounds(31, 525, 55, 15);
		lblDirection.setText("Direction");
		
		textLeftPitch = new Text(shell, SWT.BORDER);
		textLeftPitch.setBounds(297, 485, 76, 21);
		
		textLeftRoll = new Text(shell, SWT.BORDER);
		textLeftRoll.setBounds(297, 509, 76, 21);
		
		textLeftYaw = new Text(shell, SWT.BORDER);
		textLeftYaw.setBounds(297, 533, 76, 21);
		
		Label lblPitchRoll = new Label(shell, SWT.NONE);
		lblPitchRoll.setBounds(297, 468, 97, 15);
		lblPitchRoll.setText("Pitch - Roll - Yaw");
		
		textLeftElbow = new Text(shell, SWT.BORDER);
		textLeftElbow.setBounds(92, 622, 199, 21);
		
		textLeftArm = new Text(shell, SWT.BORDER);
		textLeftArm.setBounds(92, 595, 199, 21);
		
		textLeftWrist = new Text(shell, SWT.BORDER);
		textLeftWrist.setText("");
		textLeftWrist.setBounds(92, 568, 199, 21);
		
		Label lblElbow = new Label(shell, SWT.NONE);
		lblElbow.setBounds(31, 625, 55, 15);
		lblElbow.setText("Elbow");
		
		Label lblArmDirection = new Label(shell, SWT.NONE);
		lblArmDirection.setBounds(31, 598, 55, 15);
		lblArmDirection.setText("Arm direction");
		
		Label lblWrist = new Label(shell, SWT.NONE);
		lblWrist.setBounds(31, 571, 55, 15);
		lblWrist.setText("Wrist");
		
		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 560, 670, 2);
		
		Label label_1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(10, 454, 670, 2);
		
		Label label_2 = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		label_2.setBounds(678, 619, 2, 42);
		
		Label lblArm = new Label(shell, SWT.NONE);
		lblArm.setAlignment(SWT.CENTER);
		lblArm.setBounds(649, 601, 55, 15);
		lblArm.setText("Arm");
		
		Label label_3 = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		label_3.setBounds(678, 525, 2, 78);
		
		Label lblHand = new Label(shell, SWT.NONE);
		lblHand.setAlignment(SWT.CENTER);
		lblHand.setBounds(649, 512, 55, 15);
		lblHand.setText("Hand");
		
		Label label_4 = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		label_4.setBounds(678, 351, 2, 160);
		
		textLeftFinger0Type = new Text(shell, SWT.BORDER);
		textLeftFinger0Type.setBounds(92, 427, 76, 21);
		
		textLeftFinger1Type = new Text(shell, SWT.BORDER);
		textLeftFinger1Type.setBounds(204, 427, 76, 21);
		
		textLeftFinger2Type = new Text(shell, SWT.BORDER);
		textLeftFinger2Type.setBounds(326, 427, 76, 21);
		
		textLeftFinger3Type = new Text(shell, SWT.BORDER);
		textLeftFinger3Type.setBounds(443, 427, 76, 21);
		
		textLeftFinger4Type = new Text(shell, SWT.BORDER);
		textLeftFinger4Type.setBounds(568, 427, 76, 21);
		
		textLeftFinger0ID = new Text(shell, SWT.BORDER);
		textLeftFinger0ID.setBounds(92, 402, 76, 21);
		
		textLeftFinger1ID = new Text(shell, SWT.BORDER);
		textLeftFinger1ID.setBounds(204, 402, 76, 21);
		
		textLeftFinger2ID = new Text(shell, SWT.BORDER);
		textLeftFinger2ID.setBounds(326, 402, 76, 21);
		
		textLeftFinger3ID = new Text(shell, SWT.BORDER);
		textLeftFinger3ID.setBounds(443, 402, 76, 21);
		
		textLeftFinger4ID = new Text(shell, SWT.BORDER);
		textLeftFinger4ID.setBounds(568, 402, 76, 21);
		
		lblType = new Label(shell, SWT.NONE);
		lblType.setBounds(10, 430, 55, 15);
		lblType.setText("Type");
		
		lblId = new Label(shell, SWT.NONE);
		lblId.setBounds(10, 405, 55, 15);
		lblId.setText("ID");
		
		textLeftFinger0Length = new Text(shell, SWT.BORDER);
		textLeftFinger0Length.setBounds(92, 375, 76, 21);
		
		textLeftFinger1Length = new Text(shell, SWT.BORDER);
		textLeftFinger1Length.setBounds(204, 375, 76, 21);
		
		textLeftFinger2Length = new Text(shell, SWT.BORDER);
		textLeftFinger2Length.setBounds(326, 375, 76, 21);
		
		textLeftFinger3Length = new Text(shell, SWT.BORDER);
		textLeftFinger3Length.setBounds(443, 375, 76, 21);
		
		textLeftFinger4Length = new Text(shell, SWT.BORDER);
		textLeftFinger4Length.setBounds(568, 375, 76, 21);
		
		lblLength = new Label(shell, SWT.NONE);
		lblLength.setBounds(10, 378, 55, 15);
		lblLength.setText("Length");
		
		textLeftFinger0Extended = new Text(shell, SWT.BORDER);
		textLeftFinger0Extended.setBounds(92, 348, 76, 21);
		
		textLeftFinger1Extended = new Text(shell, SWT.BORDER);
		textLeftFinger1Extended.setBounds(204, 348, 76, 21);
		
		textLeftFinger2Extended = new Text(shell, SWT.BORDER);
		textLeftFinger2Extended.setBounds(326, 348, 76, 21);
		
		textLeftFinger3Extended = new Text(shell, SWT.BORDER);
		textLeftFinger3Extended.setBounds(443, 348, 76, 21);
		
		textLeftFinger4Extended = new Text(shell, SWT.BORDER);
		textLeftFinger4Extended.setBounds(568, 348, 76, 21);
		
		lblWidth = new Label(shell, SWT.NONE);
		lblWidth.setBounds(10, 351, 55, 15);
		lblWidth.setText("Extended");
		
		Label lblFingers_1 = new Label(shell, SWT.NONE);
		lblFingers_1.setAlignment(SWT.CENTER);
		lblFingers_1.setBounds(649, 332, 55, 15);
		lblFingers_1.setText("Fingers");
		
		Label label_5 = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		label_5.setBounds(678, 88, 2, 239);
		
		textLeftFinger0JS0 = new Text(shell, SWT.BORDER);
		textLeftFinger0JS0.setBounds(67, 305, 115, 21);
		
		textLeftFinger0JD0 = new Text(shell, SWT.BORDER);
		textLeftFinger0JD0.setBounds(67, 284, 115, 21);
		
		textLeftFinger0JE0 = new Text(shell, SWT.BORDER);
		textLeftFinger0JE0.setBounds(67, 263, 115, 21);
		
		textLeftFinger0JE1 = new Text(shell, SWT.BORDER);
		textLeftFinger0JE1.setBounds(67, 194, 115, 21);
		
		textLeftFinger0JD1 = new Text(shell, SWT.BORDER);
		textLeftFinger0JD1.setBounds(67, 215, 115, 21);
		
		textLeftFinger0JS1 = new Text(shell, SWT.BORDER);
		textLeftFinger0JS1.setBounds(67, 236, 115, 21);
		
		textLeftFinger0JE2 = new Text(shell, SWT.BORDER);
		textLeftFinger0JE2.setBounds(67, 125, 115, 21);
		
		textLeftFinger0JD2 = new Text(shell, SWT.BORDER);
		textLeftFinger0JD2.setBounds(67, 146, 115, 21);
		
		textLeftFinger0JS2 = new Text(shell, SWT.BORDER);
		textLeftFinger0JS2.setBounds(67, 167, 115, 21);
		
		textLeftFinger1JE2 = new Text(shell, SWT.BORDER);
		textLeftFinger1JE2.setBounds(185, 125, 114, 21);
		
		textLeftFinger1JD2 = new Text(shell, SWT.BORDER);
		textLeftFinger1JD2.setBounds(185, 146, 114, 21);
		
		textLeftFinger1JS2 = new Text(shell, SWT.BORDER);
		textLeftFinger1JS2.setBounds(185, 167, 114, 21);
		
		textLeftFinger1JE1 = new Text(shell, SWT.BORDER);
		textLeftFinger1JE1.setBounds(185, 194, 114, 21);
		
		textLeftFinger1JD1 = new Text(shell, SWT.BORDER);
		textLeftFinger1JD1.setBounds(185, 215, 114, 21);
		
		textLeftFinger1JS1 = new Text(shell, SWT.BORDER);
		textLeftFinger1JS1.setBounds(185, 236, 114, 21);
		
		textLeftFinger1JE0 = new Text(shell, SWT.BORDER);
		textLeftFinger1JE0.setBounds(185, 263, 114, 21);
		
		textLeftFinger1JD0 = new Text(shell, SWT.BORDER);
		textLeftFinger1JD0.setBounds(185, 284, 114, 21);
		
		textLeftFinger1JS0 = new Text(shell, SWT.BORDER);
		textLeftFinger1JS0.setBounds(185, 305, 114, 21);
		
		textLeftFinger2JE2 = new Text(shell, SWT.BORDER);
		textLeftFinger2JE2.setBounds(305, 125, 118, 21);
		
		textLeftFinger2JD2 = new Text(shell, SWT.BORDER);
		textLeftFinger2JD2.setBounds(305, 146, 118, 21);
		
		textLeftFinger2JS2 = new Text(shell, SWT.BORDER);
		textLeftFinger2JS2.setBounds(305, 167, 118, 21);
		
		textLeftFinger2JE1 = new Text(shell, SWT.BORDER);
		textLeftFinger2JE1.setBounds(305, 194, 118, 21);
		
		textLeftFinger2JD1 = new Text(shell, SWT.BORDER);
		textLeftFinger2JD1.setBounds(305, 215, 118, 21);
		
		textLeftFinger2JS1 = new Text(shell, SWT.BORDER);
		textLeftFinger2JS1.setBounds(305, 236, 118, 21);
		
		textLeftFinger2JE0 = new Text(shell, SWT.BORDER);
		textLeftFinger2JE0.setBounds(305, 263, 118, 21);
		
		textLeftFinger2JD0 = new Text(shell, SWT.BORDER);
		textLeftFinger2JD0.setBounds(305, 284, 118, 21);
		
		textLeftFinger2JS0 = new Text(shell, SWT.BORDER);
		textLeftFinger2JS0.setBounds(305, 305, 118, 21);
		
		textLeftFinger3JE2 = new Text(shell, SWT.BORDER);
		textLeftFinger3JE2.setBounds(429, 125, 116, 21);
		
		textLeftFinger3JD2 = new Text(shell, SWT.BORDER);
		textLeftFinger3JD2.setBounds(429, 146, 116, 21);
		
		textLeftFinger3JS2 = new Text(shell, SWT.BORDER);
		textLeftFinger3JS2.setBounds(429, 167, 116, 21);
		
		textLeftFinger3JE1 = new Text(shell, SWT.BORDER);
		textLeftFinger3JE1.setBounds(429, 194, 116, 21);
		
		textLeftFinger3JD1 = new Text(shell, SWT.BORDER);
		textLeftFinger3JD1.setBounds(429, 215, 116, 21);
		
		textLeftFinger3JS1 = new Text(shell, SWT.BORDER);
		textLeftFinger3JS1.setBounds(429, 236, 116, 21);
		
		textLeftFinger3JE0 = new Text(shell, SWT.BORDER);
		textLeftFinger3JE0.setBounds(429, 263, 116, 21);
		
		textLeftFinger3JD0 = new Text(shell, SWT.BORDER);
		textLeftFinger3JD0.setBounds(429, 284, 116, 21);
		
		textLeftFinger3JS0 = new Text(shell, SWT.BORDER);
		textLeftFinger3JS0.setBounds(429, 305, 116, 21);
		
		textLeftFinger4JE2 = new Text(shell, SWT.BORDER);
		textLeftFinger4JE2.setBounds(551, 125, 118, 21);
		
		textLeftFinger4JD2 = new Text(shell, SWT.BORDER);
		textLeftFinger4JD2.setBounds(551, 146, 118, 21);
		
		textLeftFinger4JS2 = new Text(shell, SWT.BORDER);
		textLeftFinger4JS2.setBounds(551, 167, 118, 21);
		
		textLeftFinger4JE1 = new Text(shell, SWT.BORDER);
		textLeftFinger4JE1.setBounds(551, 194, 118, 21);
		
		textLeftFinger4JD1 = new Text(shell, SWT.BORDER);
		textLeftFinger4JD1.setBounds(551, 215, 118, 21);
		
		textLeftFinger4JS1 = new Text(shell, SWT.BORDER);
		textLeftFinger4JS1.setBounds(551, 236, 118, 21);
		
		textLeftFinger4JE0 = new Text(shell, SWT.BORDER);
		textLeftFinger4JE0.setBounds(551, 263, 118, 21);
		
		textLeftFinger4JD0 = new Text(shell, SWT.BORDER);
		textLeftFinger4JD0.setBounds(551, 284, 118, 21);
		
		textLeftFinger4JS0 = new Text(shell, SWT.BORDER);
		textLeftFinger4JS0.setBounds(551, 305, 118, 21);
		
		lblJointStart = new Label(shell, SWT.NONE);
		lblJointStart.setBounds(10, 308, 55, 15);
		lblJointStart.setText("Joint start");
		
		lblDirection_1 = new Label(shell, SWT.NONE);
		lblDirection_1.setBounds(10, 287, 55, 15);
		lblDirection_1.setText("Direction");
		
		lblJoinEnd = new Label(shell, SWT.NONE);
		lblJoinEnd.setBounds(10, 266, 55, 15);
		lblJoinEnd.setText("Join end");
		
		label_6 = new Label(shell, SWT.NONE);
		label_6.setText("Join end");
		label_6.setBounds(10, 197, 55, 15);
		
		label_7 = new Label(shell, SWT.NONE);
		label_7.setText("Direction");
		label_7.setBounds(10, 218, 55, 15);
		
		label_8 = new Label(shell, SWT.NONE);
		label_8.setText("Joint start");
		label_8.setBounds(10, 239, 55, 15);
		
		label_9 = new Label(shell, SWT.NONE);
		label_9.setText("Join end");
		label_9.setBounds(10, 128, 55, 15);
		
		label_10 = new Label(shell, SWT.NONE);
		label_10.setText("Direction");
		label_10.setBounds(10, 149, 55, 15);
		
		label_11 = new Label(shell, SWT.NONE);
		label_11.setText("Joint start");
		label_11.setBounds(10, 170, 55, 15);

	}
}
