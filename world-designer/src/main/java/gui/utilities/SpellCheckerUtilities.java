package gui.utilities;

import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

import data.DataManager;
import io.github.geniot.jortho.FileUserDictionary;
import io.github.geniot.jortho.PopupListener;
import io.github.geniot.jortho.SpellChecker;
import io.github.geniot.jortho.SpellCheckerOptions;

public class SpellCheckerUtilities {

	public static void initSpellCheck() {
		SpellChecker.setUserDictionaryProvider(new FileUserDictionary(DataManager.appFolder.getPath()));
		SpellChecker.registerDictionaries(CompFactory.class.getClass().getResource("/dictionary"), "en");
	}

	public static void addPopup(JTextComponent tComp) {
		SpellCheckerOptions sco = buildOptions();
		JPopupMenu popup = SpellChecker.createCheckerPopup(sco);
		tComp.addMouseListener(new PopupListener(popup));
	}
	
	public static void setDocument(JTextComponent tComp) {
		SpellCheckerOptions sco = buildOptions();
		SpellChecker.enableAutoSpell(tComp, true, sco);
	}
	
	private static SpellCheckerOptions buildOptions() {
	    SpellCheckerOptions sco = new SpellCheckerOptions();
	    sco.setIgnoreCapitalization(true);
	    sco.setIgnoreAllCapsWords(true);
	    sco.setSuggestionsLimitMenu(15);
	    return sco;
	}
}