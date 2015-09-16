package entities;

public abstract class Command {
	
	private String type;  //type of command being processed      
	private String id;    //unique id of the command
	
	//this method returns the command given and its parameters
	public String describe() {
		return("Executing '" + id + "' with arguments: " + this.getArguments());
	}
	
	//this method executes a given command
	public void execute() {
	}
	
	//note parse() not included since its functionality is
	//encapsulated in the constructor methods of specific commands
	
	//getter and setter methods
	public void setCmdType(String type) { this.type = type; }
	public void setCmdId(String id) { this.id = id; }
	public String getCmdId(){ return id; }
	public abstract String getArguments();
}	
