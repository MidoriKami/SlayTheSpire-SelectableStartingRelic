import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.InfiniteSpeechBubble;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

public class NeowEventPatches {

    public static int pageNumber = 0;
    public static boolean optionSelected = false;
    public static int buttonIndex = -1;
    public static int maxNumRelics = -1;
    public static boolean firstScreenSelection = true;
    private static int lastNextButtonPosition = 0;
    private static int lastPage = -1;

    public static final Logger logger = LogManager.getLogger(SelectableStartingRelic.class.getName());

    @SpirePatch(clz = com.megacrit.cardcrawl.neow.NeowEvent.class, method = SpirePatch.CONSTRUCTOR, paramtypez = boolean.class)
    public static class AddSelectRelicButtons {

        @SpirePostfixPatch
        public static void Postfix(AbstractEvent e, boolean b) {

            e.roomEventText.addDialogOption("[ #rLose #rStarting #rRelic , #gSelect #gBoss #gRelic ]");
            buttonIndex = RoomEventDialog.optionList.size() - 1;
            maxNumRelics = RelicLibrary.bossList.size();

            logger.info("Found " + maxNumRelics + " Boss Relics");
        }
    }

    @SpirePatch(clz = com.megacrit.cardcrawl.neow.NeowEvent.class, method = "buttonEffect")
    public static class DisplaySelectableRelics {

        @SpirePostfixPatch
        public static void Postfix(AbstractEvent e, int buttonPressed) {

            if (pageNumber == 0 && buttonPressed == buttonIndex && !optionSelected && firstScreenSelection) {
                optionSelected = true;

                AbstractDungeon.player.loseRelic(AbstractDungeon.player.relics.get(0).relicId);
                AbstractDungeon.player.loseGold(AbstractDungeon.player.gold);
            }

            if (optionSelected) {
                SetPage(e, 1);
                ClearAllRoomEventTextOptions(e);
                DismissBubble();

                if (buttonPressed != lastNextButtonPosition && !firstScreenSelection) {
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), RelicLibrary.bossList.get(buttonPressed + (lastPage * 8)));
                    ClearAllRoomEventTextOptions(e);
                    OpenMap(e);
                }

                AddRelicsInRange(e, 8 * pageNumber, 8 * (pageNumber + 1));

                if ((8 * (pageNumber + 1)) < maxNumRelics) {
                    e.roomEventText.addDialogOption("[Next Page]");
                    lastNextButtonPosition = RoomEventDialog.optionList.size() - 1;
                    pageNumber++;
                } else {
                    e.roomEventText.addDialogOption("[Back to beginning of relic list]");
                    lastNextButtonPosition = RoomEventDialog.optionList.size() - 1;
                    lastPage = pageNumber;
                    pageNumber = 0;
                }
            }
            firstScreenSelection = false;
        }
    }

    private static void DismissBubble() {

        for (AbstractGameEffect e : AbstractDungeon.effectList) {
            if (e instanceof InfiniteSpeechBubble) {
                ((InfiniteSpeechBubble) e).dismiss();
            }
        }

    }

    private static void ClearAllRoomEventTextOptions(AbstractEvent e) {
        //e.roomEventText.clearRemainingOptions();
        e.roomEventText.clear();

        //e.roomEventText.removeDialogOption(0);
    }

    private static void AddRelicsInRange(AbstractEvent e, int startingIndex, int endingIndex) {
        if (endingIndex > maxNumRelics)
            endingIndex = maxNumRelics;

        for (int i = startingIndex; i < endingIndex; ++i) {
            e.roomEventText.addDialogOption("[Receive " + RelicLibrary.bossList.get(i).name + "]");
        }
    }

    private static void SetPage(AbstractEvent e, int page) {
        try {

            Field screenNumField = NeowEvent.class.getDeclaredField(("screenNum"));
            screenNumField.setAccessible(true);

            screenNumField.setInt(e, page);

        } catch (IllegalAccessException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }

    private static void OpenMap(AbstractEvent e) {
        try {

            Field screenNumField = NeowEvent.class.getDeclaredField(("screenNum"));
            screenNumField.setAccessible(true);

            screenNumField.setInt(e, 99);

        } catch (IllegalAccessException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }
}
