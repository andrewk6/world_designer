package data;

public class DataEnums
{
	public enum SettingStyle{
		FANTASY("Fantasy"),
		SCIFI("Sci-Fi"),
		REALISTIC("Realistic"),
		STEAMPUNK("Steampunk"),
		CYBERPUNK("Cyberpunk"),
		POSTAPOCALYPTIC("Post Apocalyptic");
		
		
		private String desc;
		SettingStyle(String desc){
			this.desc = desc;
		}
		
		public String toString() {
			return desc;
		}
	}
}