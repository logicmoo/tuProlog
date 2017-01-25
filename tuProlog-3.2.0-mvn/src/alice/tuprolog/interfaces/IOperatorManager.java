package alice.tuprolog.interfaces;
public interface IOperatorManager {
	
	
	void opNew(String name, String type, int prio);
	
	
	IOperatorManager clone();
	
}
