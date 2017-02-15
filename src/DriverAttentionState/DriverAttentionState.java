package DriverAttentionState;
import io.github.adrianulbona.hmm.*;
import io.github.adrianulbona.hmm.probability.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public enum DriverAttentionState {
	INSTANCE;
	
	public final Model<DriverState, Activity> model;
	
	DriverAttentionState() {
		model = new Model<>(probabilityCalculator(), reachableStatesFinder());
	}
	
	public enum DriverState implements State {
		ATTENTIVE,
		INATTENTIVE;
	}
	
	public enum Activity implements Observation {
		ONWHEEL,
		RESTING,
		NONE;
	}
	
	private ProbabilityCalculator<DriverState, Activity> probabilityCalculator() {
		return new ProbabilityCalculator<>(StartProbabilities.INSTANCE.data::get,
				EmissionProbabilities.INSTANCE.data::get,
				TransitionProbabilities.INSTANCE.data::get);
	}
	
	private ReachableStateFinder<DriverState, Activity> reachableStatesFinder() {
        return observation -> asList(DriverState.values());
    }
	
	private enum StartProbabilities {
        INSTANCE;

        public final Map<DriverState, Double> data;

        StartProbabilities() {
            data = new HashMap<>();
            data.put(DriverState.ATTENTIVE, 0.6);
            data.put(DriverState.INATTENTIVE, 0.4);
        }
    }
	
	private enum TransitionProbabilities {
        INSTANCE;

        public final Map<Transition<DriverState>, Double> data;

        TransitionProbabilities() {
            data = new HashMap<>();
            data.put(new Transition<>(DriverState.ATTENTIVE, DriverState.ATTENTIVE), 0.7);
            data.put(new Transition<>(DriverState.ATTENTIVE, DriverState.INATTENTIVE), 0.3);
            data.put(new Transition<>(DriverState.INATTENTIVE, DriverState.ATTENTIVE), 0.6);
            data.put(new Transition<>(DriverState.INATTENTIVE, DriverState.INATTENTIVE), 0.4);
        }
    }
	
	private enum EmissionProbabilities {
        INSTANCE;

        public final Map<Emission<DriverState, Activity>, Double> data;

        EmissionProbabilities() {
            data = new HashMap<>();
            data.put(new Emission<>(DriverState.ATTENTIVE, Activity.ONWHEEL), 0.8);
            data.put(new Emission<>(DriverState.ATTENTIVE, Activity.RESTING), 0.1);
            data.put(new Emission<>(DriverState.ATTENTIVE, Activity.NONE), 0.1);
            data.put(new Emission<>(DriverState.INATTENTIVE, Activity.ONWHEEL), 0.3);
            data.put(new Emission<>(DriverState.INATTENTIVE, Activity.RESTING), 0.4);
            data.put(new Emission<>(DriverState.INATTENTIVE, Activity.NONE), 0.3);
        }
    }
	
}

