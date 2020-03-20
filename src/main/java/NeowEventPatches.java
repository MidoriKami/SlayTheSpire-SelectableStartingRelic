import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.neow.NeowEvent;

import java.lang.reflect.Field;

public class NeowEventPatches {

    public static int pageNumber = 0;

    @SpirePatch(clz = com.megacrit.cardcrawl.neow.NeowEvent.class, method = SpirePatch.CONSTRUCTOR, paramtypez = boolean.class)
    public static class AddSelectRelicButtons {


        @SpirePostfixPatch
        public static void Postfix(AbstractEvent e, boolean b) {

            e.roomEventText.addDialogOption("[ #rLose #rStarting #rRelic , #gSelect #gBoss #gRelic ]");
        }
    }

    @SpirePatch(clz = com.megacrit.cardcrawl.neow.NeowEvent.class, method = "buttonEffect")
    public static class DisplaySelectableRelics {

        @SpirePostfixPatch
        public static void Postfix(AbstractEvent e, int buttonPressed) {

            switch (pageNumber)
            {
                case 0:
                    if( buttonPressed == 1)
                    {
                        SetPage(e, 0);
                        ClearAllRoomEventTextOptions(e);

                        AbstractDungeon.player.loseRelic(AbstractDungeon.player.relics.get(0).relicId);
                        AbstractDungeon.player.loseGold(AbstractDungeon.player.gold);

                        AddRelicsInRange(e, 0, 8);
                        e.roomEventText.addDialogOption("[Next Page]");

                        pageNumber++;
                    }
                    break;

                case 1:
                    if( buttonPressed >= 0 && buttonPressed < 8)
                    {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain( (Settings.WIDTH / 2), (Settings.HEIGHT / 2), RelicLibrary.bossList.get(buttonPressed));
                        ClearAllRoomEventTextOptions(e);
                        OpenMap(e);
                    }
                    else if (buttonPressed == 8)
                    {
                        SetPage(e, 0);
                        ClearAllRoomEventTextOptions(e);

                        AddRelicsInRange(e, 9, 17);
                        e.roomEventText.addDialogOption("[Next Page]");

                        pageNumber++;
                    }
                    break;

                case 2:
                    if( buttonPressed >= 0 && buttonPressed < 8)
                    {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain( (Settings.WIDTH / 2), (Settings.HEIGHT / 2), RelicLibrary.bossList.get(buttonPressed + 8));
                        ClearAllRoomEventTextOptions(e);
                        OpenMap(e);
                    }
                    else if (buttonPressed == 8)
                    {
                        SetPage(e, 0);
                        ClearAllRoomEventTextOptions(e);

                        AddRelicsInRange(e, 18, 26);
                        e.roomEventText.addDialogOption("[Next Page]");

                        pageNumber++;
                    }
                    break;

                case 3:
                    if( buttonPressed >= 0 && buttonPressed < 8)
                    {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain( (Settings.WIDTH / 2), (Settings.HEIGHT / 2), RelicLibrary.bossList.get(buttonPressed + 8));
                        ClearAllRoomEventTextOptions(e);
                        OpenMap(e);
                    }
                    else if (buttonPressed == 8)
                    {
                        SetPage(e, 0);
                        ClearAllRoomEventTextOptions(e);

                        AddRelicsInRange(e, 27, 30);
                        e.roomEventText.addDialogOption("[Back to beginning of relic list]");

                        pageNumber++;
                    }
                    break;

                case 4:
                    if( buttonPressed >= 0 && buttonPressed < 3)
                    {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain( (Settings.WIDTH / 2), (Settings.HEIGHT / 2), RelicLibrary.bossList.get(buttonPressed + 8));
                        ClearAllRoomEventTextOptions(e);
                        OpenMap(e);
                    }
                    else if (buttonPressed == 3)
                    {
                        SetPage(e, 0);
                        ClearAllRoomEventTextOptions(e);

                        AddRelicsInRange(e, 0, 8);
                        e.roomEventText.addDialogOption("[Next Page]");

                        pageNumber = 1;
                    }
                    break;
            }
        }
    }

    private static void ClearAllRoomEventTextOptions(AbstractEvent e) {
        e.roomEventText.clearRemainingOptions();
        e.roomEventText.removeDialogOption(0);
    }

    private static void AddRelicsInRange(AbstractEvent e, int startingIndex, int endingIndex) {
        for (int i = startingIndex; i < endingIndex; ++i) {
            e.roomEventText.addDialogOption("[Receive " + RelicLibrary.bossList.get(i).name + "]");
        }
    }

    private static void SetPage(AbstractEvent e, int page)
    {
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
