import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class RunStartEndPatches {

    @SpirePatch(clz = AbstractPlayer.class, method = "playDeathAnimation")
    public static class EndOfRun {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer p) {
            NeowEventPatches.pageNumber = 0;
        }
    }
}
