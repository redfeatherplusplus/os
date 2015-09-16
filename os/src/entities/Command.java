package entities;

public abstract class Command {
	
	private String type;  //type of command being processed      
	private String id;    //unique id of the command
	
	//this method returns the command given and its parameters
	public String describe() {
		return(this.toString());
	}
	
	//this method executes a given command
	public void execute() {
	}
	
	//note parse() not included since it is a static command
	
	//setter methods
	public void setCmdType(String type) {this.type = type;}
	public void setCmdId(String id) {this.id = id;}
	
	public String getCmdId(){return id;}
}	
