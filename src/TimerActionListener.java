import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import libsvm.svm_model;

class TimerActionListener implements ActionListener {
		
	private final svm_model model;
	private final svm_model modelLeft;
	private Label lblData;

	TimerActionListener(svm_model model, svm_model modelLeft, Label lblData) {
		this.model = model;
		this.modelLeft = modelLeft;
		this.lblData = lblData;
	}
	
	public String doubleToAction(double d) {
		String s = "None";
		
		if (d == 1.0)
			s = "Holding the steering wheel";
		else if (d == 2.0)
			s = "Resting";
		
		return s;
	}
	
	
	public void actionPerformed(ActionEvent arg0) {
		// Append all data for each set
		Controller controller = new Controller();
		Frame frame = controller.frame();
		SVMTrainer trainer = new SVMTrainer();
		
		final SimpleDateFormat formatForLines = new SimpleDateFormat("HH:mm ss.SSS");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String lineTime = formatForLines.format(timestamp);
		
		
		if (frame.hands().count() >= 1) {
			// Add time stamp
			
			if (frame.hands().get(0).isRight()) {
				final String output = lineTime + "\tRight: " + trainer.svmPredict(Main.getSample(0), model);
				System.out.println(output);
				final String additionalOutput;
				if (frame.hands().count() > 1 && frame.hands().get(1).isLeft()) {
					additionalOutput = lineTime + "\tLeft: " + doubleToAction(trainer.svmPredict(Main.getSample(1), modelLeft));
					System.out.println(additionalOutput);
				}
			}
			if (frame.hands().get(0).isLeft()) {
				final String output = lineTime + "\tLeft: " + doubleToAction(trainer.svmPredict(Main.getSample(0), modelLeft));
				System.out.println(output);
				final String additionalOutput;
				if (frame.hands().count() > 1 && frame.hands().get(1).isRight()) {
					additionalOutput = lineTime + "\tRight: " + doubleToAction(trainer.svmPredict(Main.getSample(1), model));
					System.out.println(additionalOutput);
				}
			}
				
			
			Display.getDefault().asyncExec(new Runnable() {
			    public void run() {
			    	//lblData.setText(output);
			    }
			});
		}
	}
}
