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
	
	public String getDescription(int keyCode) {
		return behaviors.get(keyCode).description;
	}
	
	public boolean getHasDuration(int keyCode) {
		return behaviors.get(keyCode).hasDuration;
	}
	
	public boolean hasBehavior(int keyCode) {
		return behaviors.containsKey(keyCode);
	}
	
	public int size() {
		return behaviors.size();
	}
	
	public void print() {
		System.out.println("-----------------------");
		for (Integer keyCode : behaviors.keySet()) {
			Behavior b = behaviors.get(keyCode);
			String str = String.format("%d => [%s, hasDuration: %s]",keyCode, b.description, b.hasDuration);
			System.out.println(str);
		}
		System.out.println("-----------------------");
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
