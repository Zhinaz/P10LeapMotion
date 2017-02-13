import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.leapmotion.leap.*;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class DataCollection {
	public Shell shell;
	private Label label;
	private Text labelText;
	private Button collect;
	private Label samplesCollected;
	
	private int numberOfSamples = 100; 
	private Boolean collectingBool = false;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		        
		try {
			DataCollection window = new DataCollection();
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
		
		// Must be running, else windows
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				//Controller controller = new Controller();
		        //Frame frame = controller.frame();
			}
		}
	}
	
	public void createContents() {
		shell = new Shell();
		shell.setSize(400, 400);
		shell.setText("Data collection");
		
		label = new Label(shell, SWT.NONE);
		label.setBounds(10, 25, 76, 21);
		label.setText("Set data label");
		
		labelText = new Text(shell, SWT.BORDER);
		labelText.setBounds(10, 50, 76, 21);
		
		collect = new Button(shell, SWT.BUTTON1);
		collect.setText("Start collecting");
		collect.setBounds(10, 75, 200, 50);
		collect.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				System.out.println("Start collecting");
				if (!collectingBool) {
					collectingBool = true;
					startCollecting();
				}
			}
		});
		
		samplesCollected = new Label(shell, SWT.NONE);
		samplesCollected.setBounds(10, 150, 100, 21);
		samplesCollected.setText("0 / " + numberOfSamples);
	}
	
	public void startCollecting() {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat df = new DecimalFormat("#.#####", otherSymbols);
		
		PrintWriter pw = null;
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.HH.mm.ss");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String filename = sdf.format(timestamp);
			File testfile = new File("files/test" + filename + ".csv");
			pw = new PrintWriter(testfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		int i = 0;
		
		while (collectingBool) {
			// Append al leap motion data vi skal bruge for hvert data sæt
			Controller controller = new Controller();
			Frame frame = controller.frame();
			
			if (frame.hands().count() >= 1) {
				Hand hand = frame.hands().get(0);
				// label
				sb.append(labelText.getText() + ",");	

				// hand center
				sb.append(df.format(hand.sphereCenter().normalized().getX()) + ",");
				sb.append(df.format(hand.sphereCenter().normalized().getY()) + ",");
				sb.append(df.format(hand.sphereCenter().normalized().getZ()) + ",");
				sb.append(df.format(hand.grabStrength()) + ",");
				sb.append(df.format(hand.pinchStrength()) + ",");
				// Palm
				sb.append(df.format(hand.palmNormal().getX()) + ",");
				sb.append(df.format(hand.palmNormal().getY()) + ",");
				sb.append(df.format(hand.palmNormal().getZ()) + ",");
				sb.append(df.format(hand.stabilizedPalmPosition().normalized().getX()) + ",");
				sb.append(df.format(hand.stabilizedPalmPosition().normalized().getY()) + ",");
				sb.append(df.format(hand.stabilizedPalmPosition().normalized().getZ()) + ",");
				// Direction
				sb.append(df.format(hand.direction().getX()) + ",");
				sb.append(df.format(hand.direction().getY()) + ",");
				sb.append(df.format(hand.direction().getZ()) + ",");
				sb.append(df.format(hand.direction().pitch()) + ",");
				sb.append(df.format(hand.direction().roll()) + ",");
				sb.append(df.format(hand.direction().yaw()) + ",");				
				
				sb.append(df.format(hand.fingers().get(0).direction().getX()) + ",");
				sb.append(df.format(hand.fingers().get(0).direction().getY()) + ",");
				sb.append(df.format(hand.fingers().get(0).direction().getZ()) + ",");
				sb.append(df.format(hand.fingers().get(0).stabilizedTipPosition().normalized().getX()) + ",");
				sb.append(df.format(hand.fingers().get(0).stabilizedTipPosition().normalized().getY()) + ",");
				sb.append(df.format(hand.fingers().get(0).stabilizedTipPosition().normalized().getZ()) + ",");
				sb.append(df.format(hand.fingers().get(1).direction().getX()) + ",");
				sb.append(df.format(hand.fingers().get(1).direction().getY()) + ",");
				sb.append(df.format(hand.fingers().get(1).direction().getZ()) + ",");
				sb.append(df.format(hand.fingers().get(1).stabilizedTipPosition().normalized().getX()) + ",");
				sb.append(df.format(hand.fingers().get(1).stabilizedTipPosition().normalized().getY()) + ",");
				sb.append(df.format(hand.fingers().get(1).stabilizedTipPosition().normalized().getZ()) + ",");
				sb.append(df.format(hand.fingers().get(2).direction().getX()) + ",");
				sb.append(df.format(hand.fingers().get(2).direction().getY()) + ",");
				sb.append(df.format(hand.fingers().get(2).direction().getZ()) + ",");
				sb.append(df.format(hand.fingers().get(2).stabilizedTipPosition().normalized().getX()) + ",");
				sb.append(df.format(hand.fingers().get(2).stabilizedTipPosition().normalized().getY()) + ",");
				sb.append(df.format(hand.fingers().get(2).stabilizedTipPosition().normalized().getZ()) + ",");
				sb.append(df.format(hand.fingers().get(3).direction().getX()) + ",");
				sb.append(df.format(hand.fingers().get(3).direction().getY()) + ",");
				sb.append(df.format(hand.fingers().get(3).direction().getZ()) + ",");
				sb.append(df.format(hand.fingers().get(3).stabilizedTipPosition().normalized().getX()) + ",");
				sb.append(df.format(hand.fingers().get(3).stabilizedTipPosition().normalized().getY()) + ",");
				sb.append(df.format(hand.fingers().get(3).stabilizedTipPosition().normalized().getZ()) + ",");
				sb.append(df.format(hand.fingers().get(4).direction().getX()) + ",");
				sb.append(df.format(hand.fingers().get(4).direction().getY()) + ",");
				sb.append(df.format(hand.fingers().get(4).direction().getZ()) + ",");
				sb.append(df.format(hand.fingers().get(4).stabilizedTipPosition().normalized().getX()) + ",");
				sb.append(df.format(hand.fingers().get(4).stabilizedTipPosition().normalized().getY()) + ",");
				sb.append(df.format(hand.fingers().get(4).stabilizedTipPosition().normalized().getZ()));
			}
						
			
			// Busy wait imellem data captures, for at undgå helt ens værdier
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	
			sb.append('\n');
			i++;
			
			samplesCollected.setText(i + " / " + numberOfSamples);
			if(i >= numberOfSamples) 
				collectingBool = false;
		}
		
		pw.write(sb.toString());
		samplesCollected.setText(i + " / " + numberOfSamples + " - Done!!");
		pw.close();
	}
}
