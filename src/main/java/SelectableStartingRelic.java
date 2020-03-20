import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModPanel;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class SelectableStartingRelic implements PostInitializeSubscriber {

    public static final Logger logger = LogManager.getLogger(SelectableStartingRelic.class.getName());
    public static final String MODNAME = "Selectable Starting Relic";
    public static final String AUTHOR = "MidoriKami";
    public static final String DESCRIPTION = "Allows you to select what relic you start with";

    public SelectableStartingRelic() {
        logger.info("Created Class Object");
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        @SuppressWarnings("unused")
        SelectableStartingRelic mod = new SelectableStartingRelic();
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = new Texture("badge_img.jpg");
        ModPanel settingsPanel = new ModPanel();
        ModLabel label = new ModLabel("My mod does not have any settings (yet)!", 400.0f, 700.0f, settingsPanel, (me) -> {
        });
        settingsPanel.addUIElement(label);
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel); // once the game has initialized we want to set up a **mod badge** which is a little icon on the main menu screen that tells users that our mod has been loaded
    }

}

