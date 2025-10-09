package data;

import java.util.ArrayList;

import data.DataEnums.ArticleCategory;

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
	
	public enum ArticleCategory{
		BUILDINGS("Buildings"),
		CHARACTERS("Characters"),
		CONDITIONS("Conditions"),
		DOCUMENTS("Documents"),
		ETHNICITIES("Ethnicities"),
		GEOGRAPHICAL_LOCATIONS("Geographical Locations"),
		GENERIC("Generic"),
		ITEMS("Items"),
		NATURALLAWS("Laws (Natural and Metaphysical)"),
		LANGUAGES("Languages"),
		MATERIALS("Materials"),
		MILITARYCONFLICTS("Military Conflicts"),
		MILITARYFORMATIONS("Military Formations"),
		MYTHS_LEGENDS("Myths & Legends"),
		ORGANIZATIONS("Organizatins"),
		PROFESSIONS("Professions"),
		PROSE("Prose"),
		RANKS_TITLES("Ranks & Titles"),
		SETTLEMENTS("Settlements"),
		SPECIES("Species"),
		SPELLS_ABILITIES("Spells and Abilities"),
		STORY_PLOTS("Story Plots"),
		TECHNOLOGIES_SCIENCES("Technologies and Sciences"),
		TRADITION_RITUALS("Traditions and Rituals"),
		VEHICLES("Vehicles");
		
		private String desc;
		ArticleCategory(String desc){
			this.desc = desc;
		}
		
		public String toString() {
			return desc;
		}
	}
	
	public static String[] getDefaultFoldersArray() {
		ArrayList<String> artNames = new ArrayList<String>();
		for(ArticleCategory cat : ArticleCategory.values())
			artNames.add(cat.toString());
		return artNames.toArray(new String[0]);
	}
}