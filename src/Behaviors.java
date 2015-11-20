import java.util.HashMap;


public class Behaviors {
	
	HashMap<Integer, Behavior> behaviors;
	
	public Behaviors() {
		behaviors = new HashMap<Integer, Behavior>();
	}
	
	public void addBehavior(int key, String desc, boolean hasDur) {
		Behavior b = new Behavior(desc, hasDur);
		behaviors.put(key, b);
	}
	
	class Behavior {
		String description;
		boolean hasDuration;
		
		public Behavior(String desc, boolean hasDur) {
			description = desc;
			hasDuration = hasDur;
		}
	}

}
