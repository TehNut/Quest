package tehnut.quest.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import tehnut.quest.api.IPlayerQuestData;
import tehnut.quest.api.IQuest;
import tehnut.quest.api.QuestAPI;
import tehnut.quest.gui.button.ButtonAbandon;
import tehnut.quest.network.MessageAbandonQuest;
import tehnut.quest.network.QuestPacketHandler;

import java.awt.*;
import java.io.IOException;

public class GuiActiveQuests extends GuiBase {

    private IPlayerQuestData questData;

    public GuiActiveQuests(EntityPlayer player) {
        questData = QuestAPI.playerHandler.getPlayerQuestData(player);
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonList.clear();

        guiLeft = (this.width - this.xSize) / 2;
        guiTop = (this.height - this.ySize) / 2;

        int off = 0;
        int id = 0;

        for (IQuest quest : questData.getActiveQuests()) {
            buttonList.add(new ButtonAbandon(++id, guiTop / 2, guiLeft / 2 + off, this, quest));
            off += 20;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int off = 0;
        for (GuiButton button : buttonList) {
            if (button instanceof ButtonAbandon) {
                ButtonAbandon abandon = (ButtonAbandon) button;
                fontRendererObj.drawString(abandon.getQuest().getTitle().getFormattedText(), guiTop / 2, guiLeft / 2 + off, Color.BLACK.getRGB());
                off += 20;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button instanceof ButtonAbandon) {
            ButtonAbandon abandon = (ButtonAbandon) button;
            QuestPacketHandler.INSTANCE.sendToServer(new MessageAbandonQuest(abandon.getQuest()));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
