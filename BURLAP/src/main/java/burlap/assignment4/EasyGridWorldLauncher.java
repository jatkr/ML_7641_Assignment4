package burlap.assignment4;

import burlap.assignment4.util.AnalysisAggregator;
import burlap.assignment4.util.AnalysisRunner;
import burlap.assignment4.util.BasicRewardFunction;
import burlap.assignment4.util.BasicTerminalFunction;
import burlap.assignment4.util.MapPrinter;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.environment.SimulatedEnvironment;
import burlap.oomdp.singleagent.explorer.VisualExplorer;
import burlap.oomdp.visualizer.Visualizer;

public class EasyGridWorldLauncher {

	private static boolean visualizeInitialGridWorld = false; //Loads a GUI with the agent, walls, and goal
	
	//runValueIteration, runPolicyIteration, and runQLearning indicate which algorithms will run in the experiment
	private static boolean runValueIteration = true; 
	private static boolean runPolicyIteration = true;
	private static boolean runQLearning = true;
	
	//showValueIterationPolicyMap, showPolicyIterationPolicyMap, and showQLearningPolicyMap will open a GUI
	//you can use to visualize the policy maps. Consider only having one variable set to true at a time
	//since the pop-up window does not indicate what algorithm was used to generate the map.
	private static boolean showValueIterationPolicyMap = true; 
	private static boolean showPolicyIterationPolicyMap = true;
	private static boolean showQLearningPolicyMap = true;
	
	private static Integer MAX_ITERATIONS = 100;
	private static Integer NUM_INTERVALS = 10;
	private static double epsilon = 0.1;
	private static double lr = 0.99; //0.99
	protected static int[][] userMap = new int[][] { 
			{ 0, 0, 0, 0, 0},
			{ 0, 1, 1, 1, 0},
			{ 0, 1, 1, 1, 0},
			{ 1, 0, 1, 1, 0},
			{ 0, 0, 0, 0, 0}, };
	

	public static void main(String[] args) {

		int[][] map = MapPrinter.mapToMatrix(userMap);
		int maxX = map.length-1;
		int maxY = map[0].length-1;


		BasicGridWorld gen = new BasicGridWorld(map,maxX,maxY); 
		Domain domain = gen.generateDomain();

		State initialState = BasicGridWorld.getExampleState(domain);

		RewardFunction rf = new BasicRewardFunction(maxX,maxY,map); //Goal is at the top right grid
		TerminalFunction tf = new BasicTerminalFunction(maxX,maxY); 

		SimulatedEnvironment env = new SimulatedEnvironment(domain, rf, tf,
				initialState);

		System.out.println("/////Easy Grid World Analysis/////\n");
		MapPrinter.printMap(MapPrinter.matrixToMap(map));
		
		if (visualizeInitialGridWorld) {
			visualizeInitialGridWorld(domain, gen, env);
		}
		
		AnalysisRunner runner = new AnalysisRunner(MAX_ITERATIONS,NUM_INTERVALS);
		if(runValueIteration){
			runner.runValueIteration(gen,domain,initialState, rf, tf, showValueIterationPolicyMap);
		}
		if(runPolicyIteration){
			runner.runPolicyIteration(gen,domain,initialState, rf, tf, showPolicyIterationPolicyMap);
		}
		if(runQLearning){
			runner.runQLearning(gen,domain,initialState, rf, tf, env, epsilon,lr,showQLearningPolicyMap);
		}
		AnalysisAggregator.printAggregateAnalysis();
	}



	public static void visualizeInitialGridWorld(Domain domain,
			BasicGridWorld gen, SimulatedEnvironment env) {
		Visualizer v = gen.getVisualizer();
		VisualExplorer exp = new VisualExplorer(domain, env, v);

		exp.addKeyAction("w", BasicGridWorld.ACTIONNORTH);
		exp.addKeyAction("s", BasicGridWorld.ACTIONSOUTH);
		exp.addKeyAction("d", BasicGridWorld.ACTIONEAST);
		exp.addKeyAction("a", BasicGridWorld.ACTIONWEST);

		exp.setTitle("Easy Grid World");
		exp.initGUI();

	}
	

}
