import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;

import libsvm.svm_model;

class Test {
	public static double[] getSample() {
		Controller controller = new Controller();
		Frame frame = controller.frame();
		double[] temp = { 1.0 };
		
		if (frame.hands().count() == 1) {
			temp = new double[]{ 0.0, frame.hands().get(0).grabStrength(), frame.hands().get(0).palmNormal().getX(), 
					frame.hands().get(0).palmNormal().getY(), frame.hands().get(0).palmNormal().getZ(), frame.hands().get(0).pinchStrength(),
					frame.hands().get(0).direction().getX(), frame.hands().get(0).direction().getY(), frame.hands().get(0).direction().getZ(),
					frame.hands().get(0).direction().pitch(), frame.hands().get(0).direction().roll(), frame.hands().get(0).direction().yaw() };
			}
		return temp;
	}
	
	public static void main(String[] args) {
		DataReader reader = new DataReader("src/testdata.csv");
		ArrayList<double[]> data = reader.getParsedData();
		SVMTrainer trainer = new SVMTrainer();
		svm_model model = trainer.svmTrain(data);
		
		final SimpleDateFormat formatForLines = new SimpleDateFormat("HH:mm ss.SSS");
		
		PrintWriter pw = null;
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.HH.mm");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String filename = sdf.format(timestamp);
			File testfile = new File("files/test" + filename + ".csv");
			pw = new PrintWriter(testfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		int j = 0;
		
		while (j < 20) {
			// Append all data for each set
			Controller controller = new Controller();
			Frame frame = controller.frame();
			
			if (frame.hands().count() == 1) {
				// Timestamp
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				String lineTime = formatForLines.format(timestamp);
				sb.append(lineTime + "\t");
				// Prediction
				sb.append(SVMTrainer.svmPredict(getSample(), model));
				
				System.out.println(lineTime + "\t" + SVMTrainer.svmPredict(getSample(), model));
			} 
			
			// Busy wait between data - avoids similar data sets
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	
			sb.append('\n');
			j++;
		}
		
		pw.write(sb.toString());
		pw.close();
	}
}
